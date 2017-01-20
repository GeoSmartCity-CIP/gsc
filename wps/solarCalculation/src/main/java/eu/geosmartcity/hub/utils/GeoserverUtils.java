package eu.geosmartcity.hub.utils;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.wps.WPSException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;

public class GeoserverUtils {
	
	private static final Logger LOGGER = Logger.getLogger(GeoserverUtils.class);
	
	/*
	 * pubblica il layer del potenziale solare su geoserver
	 */
	public static String publisherSolarRasterOnGeoserver(String raster, String width, String heigth, Catalog catalog,
			String layerName, String ws, String epsg, ReferencedEnvelope envelope) throws FileNotFoundException {
		
		String getMapRequest = null;
		
		try {
			
			File outputRasterFile = new File(raster);
			
			GeoServerRESTPublisher publisher = getRestPublisher();
			
			boolean isWsCreated = checkWs(publisher, ws, catalog);
			
			String fileName = null;
			if (outputRasterFile.getName() != null) {
				fileName = outputRasterFile.getName().substring(0, outputRasterFile.getName().indexOf("."));
			}
			
			if (isWsCreated) {
				
				boolean publishResult = publisher.publishGeoTIFF(ws, fileName, fileName, outputRasterFile, epsg,
						GSResourceEncoder.ProjectionPolicy.FORCE_DECLARED,
						ProjectPropertiesSolar.loadByName(Constants.SOLAR_STYLE), null);
				
				if (publishResult) {
					CoverageInfo coverage = catalog.getCoverageByName(fileName);
					
					if (coverage != null) {
						//coverage.getSupportedFormats().add(Constants.TIF_EXTENSION);
						coverage.getSupportedFormats().add("TIFF");
						coverage.setNativeFormat(Constants.TIF_EXTENSION);
						coverage.setNativeBoundingBox(envelope);
						coverage.setSRS(epsg);
						catalog.save(coverage);
					}
					
					getMapRequest = ProjectPropertiesSolar.loadByName(Constants.GEOSERVER_REST_URL)
							+ "/wms?SERVICE=WMS&VERSION=" + ProjectPropertiesSolar.loadByName(Constants.WMS_VERSION)
							+ "&request=GetMap&LAYERS=" + fileName + "&bbox=" + coverage.boundingBox().getMinX() + ","
							+ coverage.boundingBox().getMinY() + "," + coverage.boundingBox().getMaxX() + ","
							+ coverage.boundingBox().getMaxY() + "&srs=" + epsg + "&WIDTH=" + width + "&HEIGHT="
							+ heigth + "&FORMAT=application/openlayers";
					
					getMapRequest += "&styles=" + ProjectPropertiesSolar.loadByName(Constants.SOLAR_STYLE);
				}
			}
			
		}
		catch (Exception e) {
			throw new WPSException("error publishing layer on geoserver");
		}
		finally {
			if (raster != null) {
				//ProcessUtils.deleteTmpFile(raster);
			}
		}
		
		return getMapRequest;
	}
	
	/*
	 * pubblica il layer dello zero balance su geoserver
	 */
	public static String publisherZeroBalanceLayerOnGeoserver(String layer, String width, String heigth,
			Catalog catalog, String layerName, String ws, String epsg, ReferencedEnvelope envelope)
			throws FileNotFoundException {
		
		String getMapRequest = null;
		
		try {
			
			File outputLayerFile = new File(layer);
			
			GeoServerRESTPublisher publisher = getRestPublisher();
			
			boolean isWsCreated = checkWs(publisher, ws, catalog);
			
			String fileName = null;
			if (outputLayerFile.getName() != null) {
				fileName = outputLayerFile.getName().substring(0, outputLayerFile.getName().indexOf("."));
			}
			
			if (isWsCreated) {
				
				boolean publishResult = publisher.publishShp(ws, ProjectPropertiesSolar.loadByName(Constants.ZERO_BALANCE_STORENAME), fileName, outputLayerFile, epsg,
							ProjectPropertiesSolar.loadByName(Constants.ZERO_BALANCE_STYLE));
				
				if (publishResult) {
					FeatureTypeInfo featureType = catalog.getFeatureTypeByName(fileName);
					
					getMapRequest = ProjectPropertiesSolar.loadByName(Constants.GEOSERVER_REST_URL)
							+ "/wms?SERVICE=WMS&VERSION=" + ProjectPropertiesSolar.loadByName(Constants.WMS_VERSION)
							+ "&request=GetMap&LAYERS=" + fileName + "&bbox=" + featureType.boundingBox().getMinX() + ","
							+ featureType.boundingBox().getMinY() + "," + featureType.boundingBox().getMaxX() + ","
							+ featureType.boundingBox().getMaxY() + "&srs=" + epsg + "&WIDTH=" + width + "&HEIGHT="
							+ heigth + "&FORMAT=application/openlayers";
					
					getMapRequest += "&styles=" + ProjectPropertiesSolar.loadByName(Constants.ZERO_BALANCE_STYLE);
				}
			}
			
		}
		catch (Exception e) {
			throw new WPSException("error publishing layer on geoserver");
		}
		finally {
			if (layer != null) {
				ProcessUtils.deleteTmpFile(layer);
			}
		}
		
		return getMapRequest;
	}
	
	private static boolean checkWs(GeoServerRESTPublisher publisher, String ws, Catalog catalog) {
		//se non c'è il ws temp lo creo
		boolean wsCreated = true;
		if (catalog.getWorkspaceByName(ws) == null) {
			wsCreated = publisher.createWorkspace(ws);
		}
		return wsCreated;
	}
	
	/**
	 * restituisce il publisher
	 * 
	 * @return
	 */
	private static GeoServerRESTPublisher getRestPublisher() {
		GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(
				ProjectPropertiesSolar.loadByName(Constants.GEOSERVER_REST_URL),
				ProjectPropertiesSolar.loadByName(Constants.GEOSERVER_REST_USER),
				ProjectPropertiesSolar.loadByName(Constants.GEOSERVER_REST_PW));
		return publisher;
	}
	
	/*
	 * metodo che recupera il rastser o la sfs dalla cartella data di geoserver oppure se non lo trova lo riscrive su fs
	 */
	public static String getObjPath(Catalog catalog, Object obj) throws MalformedURLException {
		String filePath = null;
		
		String objName = null;
		GridCoverage2D raster = null;
		SimpleFeatureCollection sfs = null;
		String objUrl = null;
		
		if (obj instanceof GridCoverage2D) {
			raster = (GridCoverage2D) obj;
			if (raster.getName() != null) {
				objName = raster.getName().toString();
				if (catalog.getCoverageByName(objName) != null) {
					objUrl = catalog.getCoverageByName(objName).getStore().getURL();
				}
			}
		}
		else if (obj instanceof SimpleFeatureCollection) {
			sfs = (SimpleFeatureCollection) obj;
			if (sfs.getSchema() != null) {
				objName = sfs.getSchema().getTypeName();
				if (catalog.getFeatureTypeByName(objName) != null
						&& catalog.getFeatureTypeByName(objName).getStore() != null
						&& catalog.getFeatureTypeByName(objName).getStore().getConnectionParameters() != null) {
					objUrl = (String) catalog.getFeatureTypeByName(objName).getStore().getConnectionParameters()
							.get("url");
				}
			}
		}
		
		//vedo se riesco a recuperarlo dal catalog di geoserver senza doverlo riscrivere sul filesystem
		if (objName != null && objUrl != null) {
			
			URL url = new URL(objUrl);
			
			String geoserverPath = url.getPath();
			
			LOGGER.debug("inizio recupero layer dalla cartella di geoserver " + geoserverPath);
			
			//recupero dove geoserver ha i raster caricati
			File dirGeoserver = null;
			try {
				dirGeoserver = catalog.getResourceLoader().find(geoserverPath);
				LOGGER.debug("path geoserver " + dirGeoserver);
				if (dirGeoserver.exists()) {
					filePath = dirGeoserver.getPath();
					LOGGER.debug("fine recupero da " + filePath);
				}
			}
			catch (IOException e) {
				LOGGER.error("errore nel recupero di " + objName + " dalla directory di geoserver " + geoserverPath, e);
			}
		}
		
		//se non sono riuscita a recuperare il path oppure il file non esiste riscrivo il raster o il layer vettoriale su fs
		if (filePath == null) {
			if (raster != null) {
				filePath = ProjectPropertiesSolar.loadByName(Constants.TMP_PATH) + raster.getName() + "_"
						+ System.currentTimeMillis() + Constants.TIF_EXTENSION;
				LOGGER.debug("inizio scrittura raster " + raster.getName() + " in " + filePath);
				ProcessUtils.writeRasterSuFile(filePath, raster);
			}
			else if (sfs != null) {
				filePath = ProjectPropertiesSolar.loadByName(Constants.TMP_PATH) + sfs.getSchema().getTypeName() + "_"
						+ System.currentTimeMillis() + Constants.GML_EXTENSION;
				LOGGER.debug("inizio scrittura sfs " + sfs.getSchema().getTypeName() + " in " + filePath);
				ProcessUtils.writeVectorLayerSuGML(filePath, sfs);
			}
		}
		return filePath;
	}
	
}
