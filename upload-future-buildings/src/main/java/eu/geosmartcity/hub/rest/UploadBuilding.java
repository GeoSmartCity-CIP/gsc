package eu.geosmartcity.hub.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.xml.sax.InputSource;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import de.micromata.opengis.kml.v_2_2_0.Kml;
import eu.geosmartcity.hub.parser.CityGmlParser;
import eu.geosmartcity.hub.parser.GmlParser;
import eu.geosmartcity.hub.parser.KmlParser;
import eu.geosmartcity.hub.parser.ShapeParser;
import eu.geosmartcity.hub.pojo.OutputDetails;
import eu.geosmartcity.hub.utils.Functions;
import eu.geosmartcity.hub.utils.HibernateUtil;
import eu.geosmartcity.hub.utils.Httprequest;
import eu.geosmartcity.hub.utils.ReadFromConfig;
import eu.geosmartcity.hub.utils.Uploadservices;
import eu.geosmartcity.hub.velocity.SetupInsertSensorTemplate;

@SwaggerDefinition(info = @Info(description = "Upload service", version = "V0.1", title = "Upload Service", termsOfService = "http://todo.html"

, contact = @Contact(name = "Sinergis", email = "piergiorgio.cipriano@sinergis.it", url = "http://www.sinergis.it")))
@Path("/file")
@Api(value = "/file", description = "Upload service to ingestion of geometry")
public class UploadBuilding {
	private static final Logger LOGGER = Logger.getLogger(UploadBuilding.class);
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Upload dataset", notes = "You can upload shp(zip package)/gsm/kml/cityGml")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Format not supported"),
			@ApiResponse(code = 404, message = "Resource not foud"),
			@ApiResponse(code = 200, message = "Upload successfully") })
	public Response uploadFile(
			/* @ApiParam(value = "Stream", required = true) */@FormDataParam("file") InputStream uploadedInputStream,
			/* @ApiParam(value = "File Deatail", required = true) */@FormDataParam("file") FormDataContentDisposition fileDetail,
			/* @ApiParam(value = "epsg of data", required = true) */@FormDataParam("epsg") String epsg,
			/* @ApiParam(value = "Height for 2d geometry", required = false) */@FormDataParam("fieldHeight") String fieldHeight,
			/* @ApiParam(value = "Inspire ID", required = false) */@FormDataParam("fieldInspireIdLoc") String fieldInspireIdLoc,
			@FormDataParam("fieldInspireIdName") String fieldInspireIdName) throws Exception {
		
		String extension = Functions.getExtension(fileDetail.getFileName());
		String timestamp = String.valueOf(System.currentTimeMillis());
		//		Files.copy(uploadedInputStream, path);
		ExtensionFile ext = ExtensionFile.getField(extension.toUpperCase());
		if (ext == null) {
			LOGGER.info("Wrong format");
			Response.status(400).header("error", "Wrong format").build();
		}
		if (ext.getExtdName().equalsIgnoreCase("package")) {
			String nameShape = Functions.writeZipShapeOnDisk(uploadedInputStream);
			ShapeParser shape = new ShapeParser();
			shape.getDetailFromShape(nameShape, fieldHeight, (String) epsg, timestamp);
			
		}
		if (ext.getExtdName().equalsIgnoreCase("gml")) {
			LOGGER.info("Try to parsing gml/cityGml");
			GmlParser file = new GmlParser(epsg, timestamp);
			file.parseFile(uploadedInputStream, fieldHeight, fieldInspireIdLoc, fieldInspireIdName,
					fileDetail.getFileName());
		}
		
		String pathKml = null;
		try {
			if (ext.getExtdName().equalsIgnoreCase("kml")) {
				pathKml = Functions.writeFileOnDisk(uploadedInputStream, fileDetail.getFileName());
				final Kml kml = Kml.unmarshal(new File(pathKml));
				KmlParser parser = new KmlParser();
				parser.parseKml(kml, (String) epsg, timestamp);
			}
			
		}
		catch (Exception e) {
			LOGGER.error("errore nella gestione del file kml " + pathKml);
		}
		finally {
			if (pathKml != null && new File(pathKml).exists()) {
				Functions.deleteTmpFile(pathKml);
			}
		}
		
		Uploadservices up = new Uploadservices();
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		List result = up.checkTable("wps_solar", "buildings_" + epsg, tx, session);
		Boolean trovata = (Boolean) result.get(0);
		OutputDetails output = new OutputDetails();
		SetupInsertSensorTemplate template = new SetupInsertSensorTemplate();
		
		Httprequest http = new Httprequest();
		String url = ReadFromConfig.loadByName("uriGeoserver");
		String workspaceGeo = ReadFromConfig.loadByName("workspaceGeoserver");
		String dataStoreGeo = ReadFromConfig.loadByName("datastoreGeoserver");
		String contentType = "text/xml";
		
		//se non trovata 
		if (!trovata) {
			LOGGER.info("pubblicazione su geoserver di " + "buildings_" + epsg);
			boolean state = up.createTable(epsg, tx, session);
			tx.commit();
			//<featureType><name>lakes</name></featureType>
			if (state) {
				String payload = "<featureType><name>" + "buildings_" + epsg + "</name></featureType>";
				
				http.postPublishPostgisTable(url, workspaceGeo, dataStoreGeo, payload, contentType);
			}
		}
		else {
			//ricalcolo il bounding box in modo che venga aggiornato con le nuove features
			String payload = "<featureType><name>" + "buildings_" + epsg + "</name><enabled>true</enabled></featureType>";
			http.putReloadBbox(url, workspaceGeo, dataStoreGeo, "buildings_" + epsg, payload, contentType);
		}
		
		LOGGER.info("fine inserimento");
		output.setShapeUri(template.getShapeRequest(timestamp, "buildings_" + epsg));
		output.setWfsUri(template.getWfsRequest(timestamp, "buildings_" + epsg));
		output.setJsonUri(template.getJsonRequest(timestamp,"buildings_" + epsg));
		return Response.ok(output).build();
		
	}
}
