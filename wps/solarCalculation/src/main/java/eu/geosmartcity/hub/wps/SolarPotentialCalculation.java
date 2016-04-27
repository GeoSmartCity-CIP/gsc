package eu.geosmartcity.hub.wps;

import java.io.File;

import org.apache.log4j.Logger;
import org.geoserver.catalog.Catalog;
import org.geoserver.wps.WPSException;
import org.geoserver.wps.jts.SpringBeanProcessFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.opengis.util.ProgressListener;

import eu.geosmartcity.hub.utils.Constants;
import eu.geosmartcity.hub.utils.DateSolarUtils;
import eu.geosmartcity.hub.utils.Email;
import eu.geosmartcity.hub.utils.GeoserverUtils;
import eu.geosmartcity.hub.utils.ProcessUtils;
import eu.geosmartcity.hub.utils.ProjectProperties;

@DescribeProcess(title = "solarPotentialCalculation", description = "Service that compute solar potential")
public class SolarPotentialCalculation extends SpringBeanProcessFactory {
	
	private static final Logger LOGGER = Logger.getLogger(SolarPotentialCalculation.class);
	
	private Catalog catalog;
	
	public SolarPotentialCalculation(Catalog catalog) {
		super(Constants.WPS_TITLE, Constants.WPS_NAMESPACE, SolarPotentialCalculation.class);
		this.catalog = catalog;
		
	}
	
	@DescribeResult(name = "result", description = "output result")
	public String execute(
			@DescribeParameter(name = "dsm", description = "The DSM") GridCoverage2D dsm,
			@DescribeParameter(name = "dtm", description = "The DTM") GridCoverage2D dtm,
			@DescribeParameter(name = "lin", description = "A single value of the Linke atmospheric turbidity coefficient (default: 3.0)", min = 0, defaultValue = "3.0") Float lin,
			@DescribeParameter(name = "alb", description = "A single value of the ground albedo coefficient (default: 0.2)", min = 0, defaultValue = "0.2") Float alb,
			@DescribeParameter(name = "coefbh", description = "A single value of the real-sky beam radiation coefficient (thick cloud) (default: 0.55)", min = 0, defaultValue = "0.55") String coefbh,
			@DescribeParameter(name = "start_day", description = "Start day of the year (in this format dd/MM)") String startDate,
			@DescribeParameter(name = "end_day", description = "End day of the year (in this format dd/MM)", min = 0) String endDate,
			@DescribeParameter(name = "step", description = "Time step when computing all-day radiation sums [decimal hours] (default: 0.5)", min = 0, defaultValue = "0.5") Float step,
			@DescribeParameter(name = "dist", description = "Sampling distance step coefficient (0.5-1.5) (default: 1.0)", min=0, defaultValue = "1.0") Float dist,
			@DescribeParameter(name = "buildings", description = "Buildings") SimpleFeatureCollection buildings,
			@DescribeParameter(name = "bufferArea", description = "Meters for buffered area", min = 0, defaultValue = "1") Integer buffer,
			@DescribeParameter(name = "email", description = "Email address to send the solar calculation", min = 0) String addressMail,
			ProgressListener progressListener) throws Exception {
		
		progressListener.started();
		
		checkInputParameter(dsm, dtm, lin, alb, coefbh, startDate, step, dist, buildings);
		LOGGER.info("numero di edifici " + buildings.size());
		
		//ricavo i giorni su cui calcolare rsun
		String day = DateSolarUtils.getRangeDay(startDate, endDate);
		
		String dsmFile = null;
		String dtmFile = null;
		String buildingFile = null;
		String dtmFileClipped = null;
		String dsmFileClipped = null;
		String getMap = null;
		
		try {
			
			ReferencedEnvelope bboxBuildings = buildings.getBounds();
			bboxBuildings.expandBy(buffer);
			
			//cerco il dtm sul catalog di geoserver, se non c'è lo creo
			dtmFile = GeoserverUtils.getObjPath(catalog, dtm);
			LOGGER.info("dtm in " + dtmFile);
			
			dtmFileClipped = ProcessUtils.clip(dtmFile, bboxBuildings);
			LOGGER.info("dtm clipped in " + dtmFileClipped);
			
			progressListener.progress(10);
			
			//cerco il dsm sul catalog di geoserver, se non c'è lo creo
			dsmFile = GeoserverUtils.getObjPath(catalog, dsm);
			LOGGER.info("dsm in " + dtmFile);
			
			dsmFileClipped = ProcessUtils.clip(dsmFile, bboxBuildings);
			LOGGER.info("dsm clipped in " + dsmFileClipped);
			
			progressListener.progress(20);
			
			//cerco il layer degli edifici su geoserver, se non c'è lo creo
			buildingFile = GeoserverUtils.getObjPath(catalog, buildings);
			LOGGER.info("buildingFile in " + buildingFile);
			
			progressListener.progress(30);
			
			String epsg = getEpsg(dtm, dsm, buildings);
			
			progressListener.progress(40);
			
			String command = Constants.PATH_SCRIPT + " ";
			command += Constants.MAPSET + " ";
			command += Constants.LOCATION + " ";
			command += dtmFileClipped + " ";
			command += buildingFile + " ";
			command += epsg + " ";
			command += dsmFileClipped + " ";
			command += buffer + " ";
			command += lin + " ";
			command += alb + " ";
			command += day + " ";
			command += step + " ";
			command += dist + " ";
			command += coefbh + " ";
			command += bboxBuildings.getMaxY() + " ";
			command += bboxBuildings.getMinY() + " ";
			command += bboxBuildings.getMinX() + " ";
			command += bboxBuildings.getMaxX();

			LOGGER.info("comando eseguito " + command);
			
			String output = ProcessUtils.exec(command);
			LOGGER.info("comando eseguito " + command);
			progressListener.progress(90);
			
			File f = new File(output);
			if (!f.exists()) {
				LOGGER.error("file " + output + " non trovato");
				throw new WPSException("error while creating file " + output);
			}
			
			//ReferencedEnvelope envelope = buildings.getBounds();
			
			getMap = GeoserverUtils.publisherLayerOnGeoserver(output, Constants.RASTER_WIDTH, Constants.RASTER_HEIGHT,
					catalog, output, Constants.GEOSERVER_WS_TEMP, Constants.EPSG + epsg, bboxBuildings);
			
			if (addressMail != null) {
				Email.sendEmail(ProjectProperties.loadByName("smtpFrom"), addressMail, "",
						ProjectProperties.loadByName("smtpSubject"), getMap);
			}
			
		}
		catch (Exception e) {
			LOGGER.error("errore nel calcolo della procedura", e);
			throw new WPSException("error while calculating procedure ", e);
		}
		finally {
			if (dsmFile != null && dsmFile.startsWith(ProjectProperties.loadByName(Constants.TMP_PATH))) {
				ProcessUtils.deleteTmpFile(dsmFile);
			}
			
			if (dtmFile != null && dtmFile.startsWith(ProjectProperties.loadByName(Constants.TMP_PATH))) {
				ProcessUtils.deleteTmpFile(dtmFile);
			}
			
			if (buildingFile != null && buildingFile.startsWith(ProjectProperties.loadByName(Constants.TMP_PATH))) {
				ProcessUtils.deleteTmpFile(buildingFile);
			}
			
			if (dtmFileClipped != null && dtmFileClipped.startsWith(ProjectProperties.loadByName(Constants.TMP_PATH))) {
				ProcessUtils.deleteTmpFile(dtmFileClipped);
			}
			
			if (dsmFileClipped != null && dsmFileClipped.startsWith(ProjectProperties.loadByName(Constants.TMP_PATH))) {
				ProcessUtils.deleteTmpFile(dsmFileClipped);
			}
			
			progressListener.complete();
		}
		
		return getMap;
		
	}
	
	/**
	 * controlla i parametri in input
	 * 
	 * @param dsm
	 * @param dtm
	 * @param lin
	 * @param alb
	 * @param coefbh
	 * @param horizon
	 * @param startDate
	 * @param step
	 * @param dist
	 * @param buildings
	 */
	private void checkInputParameter(GridCoverage2D dsm, GridCoverage2D dtm, Float lin, Float alb, String coefbh,
			String startDate, Float step, Float dist, SimpleFeatureCollection buildings) {
		
		if (dsm == null || dtm == null) {
			throw new WPSException("specify the dsm/dtm, it is mandatory");
		}
		
		if (lin == null) {
			throw new WPSException("specify the Linke atmospheric turbidity coefficient, it is mandatory");
		}
		
		if (alb == null) {
			throw new WPSException("specify the ground albedo coefficient, it is mandatory");
		}
		
		if (coefbh == null) {
			throw new WPSException("specify the real-sky beam radiation coefficient, it is mandatory");
		}
		
		if (startDate == null) {
			throw new WPSException("specify the start day of the year for solar calculation, it is mandatory");
		}
		
		if (step == null) {
			throw new WPSException("specify the time step when computing all-day radiation sums, it is mandatory");
		}
		
		if (dist == null) {
			throw new WPSException("specify the sampling distance step coefficient, it is mandatory");
		}
		
		if (buildings == null) {
			throw new WPSException("specify the building, it is mandatory");
		}
		
		if (buildings.size() <= 0) {
			throw new WPSException("no building defined");
		}
		
	}
	
	/**
	 * controllo che gli epsg dei dati in input siano uguali e restituisce l'epsg
	 * 
	 * @param dtm
	 * @param dsm
	 * @param buildings
	 * @return the epsg
	 */
	private String getEpsg(GridCoverage2D dtm, GridCoverage2D dsm, SimpleFeatureCollection buildings) {
		
		String epsgDtm = CRS.toSRS(dtm.getCoordinateReferenceSystem(), true);
		LOGGER.info("epsg dtm " + epsgDtm);
		
		String epsgDsm = CRS.toSRS(dsm.getCoordinateReferenceSystem(), true);
		LOGGER.info("epsg dsm " + epsgDtm);
		
		String epsgBuilding = CRS.toSRS(buildings.getSchema().getCoordinateReferenceSystem(), true);
		LOGGER.info("epsg building " + epsgBuilding);
		
		if (epsgDtm != null && epsgDsm != null && epsgBuilding != null) {
			
			//controllo che gli epsg siano sempre gli stessi
			if (epsgDtm.equalsIgnoreCase(epsgDsm) && epsgDsm.equalsIgnoreCase(epsgBuilding)) {
				return epsgDtm;
			}
			else {
				throw new WPSException("Different reference system");
			}
		}
		throw new WPSException("Reference system is null");
	}
	
}
