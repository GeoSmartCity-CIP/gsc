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
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.ProgressListener;

import eu.geosmartcity.hub.utils.Constants;
import eu.geosmartcity.hub.utils.GeoserverUtils;
import eu.geosmartcity.hub.utils.ProcessUtils;
import eu.geosmartcity.hub.utils.ProjectPropertiesSolar;

@DescribeProcess(title = "zeroBalanceCalculation", description = "Service that compute the zero balance layer")
public class ZeroBalanceCalculation extends SpringBeanProcessFactory {
	
	private static final Logger LOGGER = Logger.getLogger(SolarPotentialCalculation.class);
	
	private Catalog catalog;
	
	public ZeroBalanceCalculation(Catalog catalog) {
		super(Constants.WPS_TITLE, Constants.WPS_NAMESPACE, ZeroBalanceCalculation.class);
		this.catalog = catalog;
	}
	
	@DescribeResult(name = "result", description = "output result")
	public String execute(
			@DescribeParameter(name = "solarPotential", description = "The raster for solar potential", min = 0) GridCoverage2D solarPotentialRaster,
			@DescribeParameter(name = "buildings", description = "The vector layer for buildings with energy consumption") SimpleFeatureCollection buildingsWithEnergyConsumption,
			@DescribeParameter(name = "paramElectricityConsumption", description = "The name of parameter for electricity energy consumption", defaultValue = "ele_2014") String paramElectricityConsumption,
			@DescribeParameter(name = "paramSolarPotential", description = "The name of parameter for solar potential, if the layer are unique and containing both attributes ", defaultValue = "sp", min = 0) String paramSolarPotential,
			ProgressListener progressListener) throws Exception {
		
		ReferencedEnvelope bboxBuildings = buildingsWithEnergyConsumption.getBounds();
		
		String rasterSolar = null;
		String rasterSolarClipped = "null";
		String buildingFile = null;
		String getMap = null;
		String epsg = CRS.toSRS(buildingsWithEnergyConsumption.getSchema().getCoordinateReferenceSystem(), true);
		
		try {
			
			if (solarPotentialRaster != null) {
				//cerco il raster del potenziale solare sul catalog di geoserver, se non c'è lo creo
				rasterSolar = GeoserverUtils.getObjPath(catalog, solarPotentialRaster);
				LOGGER.info("potential raster in " + rasterSolar);
				
				//clippo il raster
				rasterSolarClipped = ProcessUtils.clip(rasterSolar, bboxBuildings);
				LOGGER.info("potential raster clipped in " + rasterSolarClipped);
				
				progressListener.setTask(new SimpleInternationalString("Clipping Raster Solar Potential"));
				progressListener.progress(10);
				
				epsg = getEpsg(solarPotentialRaster, buildingsWithEnergyConsumption);
			}
			
			//cerco il layer degli edifici su geoserver, se non c'è lo creo
			buildingFile = GeoserverUtils.getObjPath(catalog, buildingsWithEnergyConsumption);
			LOGGER.info("buildingFile in " + buildingFile);
			
			progressListener.setTask(new SimpleInternationalString("Loading buildings layer"));
			progressListener.progress(30);
			
			
			
			progressListener.setTask(new SimpleInternationalString("Execute grass's script"));
			progressListener.progress(50);
			
			String command = ProjectPropertiesSolar.loadByName(Constants.PATH_SCRIPT_ZERO_BALANCE) + " ";
			command += ProjectPropertiesSolar.loadByName(Constants.MAPSET) + " ";
			command += ProjectPropertiesSolar.loadByName(Constants.LOCATION) + " ";
			command += rasterSolarClipped + " ";
			command += buildingFile + " ";
			command += epsg + " ";
			command += paramElectricityConsumption + " ";
			
			if (rasterSolarClipped == null && paramSolarPotential == null) {
				//layer unico ma non è indicato il parametro del potenziale solare
				throw new WPSException("there aren't a raster layer, the layer is unique but you don't specify the solar potential parameter");
			} else if (rasterSolarClipped.equals("null") && paramSolarPotential != null) {
				command += paramSolarPotential + " ";
			}
			
			LOGGER.info("comando eseguito " + command);
			
			String output = ProcessUtils.exec(command);
			LOGGER.info("comando eseguito " + command);
			
			progressListener.progress(90);
			
			File f = new File(output);
			if (!f.exists()) {
				LOGGER.error("file " + output + " non trovato");
				throw new WPSException("error while creating file " + output);
			}
			
			progressListener.setTask(new SimpleInternationalString("Starting Geoserver publishing"));
			
			getMap = GeoserverUtils.publisherZeroBalanceLayerOnGeoserver(output,
					ProjectPropertiesSolar.loadByName(Constants.RASTER_WIDTH),
					ProjectPropertiesSolar.loadByName(Constants.RASTER_HEIGHT), catalog, output,
					ProjectPropertiesSolar.loadByName(Constants.GEOSERVER_WS_TEMP), Constants.EPSG + epsg,
					bboxBuildings);
			
			progressListener.complete();
		}
		catch (Exception e) {
			LOGGER.error("errore nel calcolo della procedura", e);
			throw new WPSException("error while calculating procedure ", e);
		}
		finally {
			if (rasterSolar != null && rasterSolar.startsWith(ProjectPropertiesSolar.loadByName(Constants.TMP_PATH))) {
				ProcessUtils.deleteTmpFile(rasterSolar);
			}
			if (rasterSolarClipped != null
					&& rasterSolarClipped.startsWith(ProjectPropertiesSolar.loadByName(Constants.TMP_PATH))) {
				ProcessUtils.deleteTmpFile(rasterSolarClipped);
			}
			if (buildingFile != null && buildingFile.startsWith(ProjectPropertiesSolar.loadByName(Constants.TMP_PATH))) {
				ProcessUtils.deleteTmpFile(buildingFile);
			}
		}
		
		return getMap;
	}
	
	/**
	 * controllo che gli epsg dei dati in input siano uguali e restituisce l'epsg
	 * 
	 * @param dtm
	 * @param dsm
	 * @param buildings
	 * @return the epsg
	 */
	private String getEpsg(GridCoverage2D raster, SimpleFeatureCollection buildings) {
		
		String epsgRaster = CRS.toSRS(raster.getCoordinateReferenceSystem(), true);
		LOGGER.info("epsg raster " + epsgRaster);
		
		String epsgBuilding = CRS.toSRS(buildings.getSchema().getCoordinateReferenceSystem(), true);
		LOGGER.info("epsg building " + epsgBuilding);
		
		if (epsgRaster != null && epsgBuilding != null) {
			
			//controllo che gli epsg siano sempre gli stessi
			if (epsgRaster.equalsIgnoreCase(epsgBuilding)) {
				return epsgRaster;
			}
			else {
				throw new WPSException("Different reference system");
			}
		}
		throw new WPSException("Reference system is null");
	}
}
