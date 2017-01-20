package eu.geosmartcity.hub.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.media.jai.PlanarImage;

import org.apache.log4j.Logger;
import org.geoserver.wfs.Transaction;
import org.geoserver.wps.WPSException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml.producer.FeatureTransformer;
import org.geotools.referencing.CRS;
import org.geotools.resources.image.ImageUtilities;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class ProcessUtils {
	
	private static Logger LOGGER = Logger.getLogger(ProcessUtils.class);
	
	public static String exec(String command) throws Exception {
		
		Runtime run = Runtime.getRuntime();
		Process pp = run.exec(command);
		
		StringBuffer response = new StringBuffer();
		
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(pp.getInputStream()));
		for (String line; (line = streamReader.readLine()) != null;) {
			LOGGER.debug("log di grass " + line);
			response.append(line);
		}
		
		streamReader = new BufferedReader(new InputStreamReader(pp.getErrorStream()));
		
		for (String line; (line = streamReader.readLine()) != null;) {
			LOGGER.debug("log di grass " + line);
			response.append(line);
		}
		
		return response.toString();
	}
	
	public static String execArrayString(String[] command, boolean getOutput) throws Exception {
		
		Runtime run = Runtime.getRuntime();
		Process pp = run.exec(command);
		
		StringBuffer response = new StringBuffer();
		BufferedReader streamReader = null;
		if (getOutput) {
			//se il processo restituisce in output delle informazioni e non solo degli errori (vedi zonal_stat.py)
			streamReader = new BufferedReader(new InputStreamReader(pp.getInputStream()));
		}
		else {
			streamReader = new BufferedReader(new InputStreamReader(pp.getErrorStream()));
		}
		
		for (String line; (line = streamReader.readLine()) != null;) {
			LOGGER.debug("log di gdal " + line);
			if (!getOutput && (line.toLowerCase().contains("warning") || !line.toLowerCase().contains("error"))) {
				continue;
			}
			response.append(line);
		}
		
		if (!getOutput) {
			if (!response.toString().equals("")) {
				LOGGER.error("errore di gdal " + response.toString());
				throw new Exception(response.toString());
			}
		}
		
		return response.toString();
	}
	
	/*
	 * scrive il raster su un file in modo da poter lanciare il comando gdal
	 */
	public static boolean writeRasterSuFile(String pathFile, GridCoverage2D raster) {
		File fileDem = new File(pathFile);
		
		GeoTiffWriter writer = null;
		try {
			if (fileDem != null) {
				LOGGER.debug("inizio a scrivere il raster " + fileDem.getName() + " in " + pathFile);
				writer = new GeoTiffWriter(fileDem);
				writer.write(raster, null);
				LOGGER.debug("fine scrittura raster " + fileDem.getName() + " in " + pathFile);
			}
		}
		catch (Exception e) {
			LOGGER.error("errore nella scrittura del raster " + raster.getName(), e);
			throw new WPSException("errore during writing raster " + raster.getName(), e);
		}
		finally {
			try {
				writer.dispose();
				raster.dispose(false);
				
				//per togliere il lock sul file
				PlanarImage planarImage = (PlanarImage) raster.getRenderedImage();
				ImageUtilities.disposePlanarImageChain(planarImage);
			}
			catch (Throwable e) {
				LOGGER.error("errore nella chiusura del writer ", e);
				throw new WPSException("errore during closing raster " + raster.getName(), e);
			}
		}
		return true;
	}
	
	/*
	 * scrive il layer vettoriale in un file gml in modo da poter lanciare il comando grass
	 */
	public static boolean writeVectorLayerSuGML(String pathFile, SimpleFeatureCollection sfs) {
		
		FileOutputStream xml = null;
		
		try {
			xml = new FileOutputStream(new File(pathFile));
			
			SimpleFeatureType ft = sfs.getSchema();
			FeatureTransformer tx = new FeatureTransformer();
			
			// set the SRS for the entire featureCollection.
			String srsName = CRS.toSRS(ft.getCoordinateReferenceSystem(), true);
			tx.setSrsName(Constants.OPENGIS_URL_EPSG + srsName);
			
			// set the namespace and the prefix
			String namespaceURI = ft.getName().getNamespaceURI();
			tx.getFeatureTypeNamespaces().declareNamespace(ft, Constants.PREFIX_GML, namespaceURI);
			
			tx.transform(sfs, xml);
			
		}
		catch (Exception e) {
			LOGGER.error("errore nella scrittura della simpleFeatureCollection " + sfs.getSchema(), e);
			throw new WPSException("errore during writing simpleFeatureCollection " + sfs.getSchema(), e);
		}
		finally {
			try {
				if (xml != null) {
					xml.close();
				}
			}
			catch (Throwable e) {
				LOGGER.error("errore nella chiusura del writer ", e);
				throw new WPSException("errore during closing sfs " + sfs.getSchema(), e);
			}
		}
		return true;
	}
	
	/*
	 * scrive il raster su un file in modo da poter lanciare il comando gdal
	 */
	public static boolean writeShpSuFile(String pathFile, SimpleFeatureCollection sfs) {
		File fileShp = new File(pathFile);
		
		ShapefileDataStore newDataStore = null;
		try {
			
			ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
			
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("url", fileShp.toURI().toURL());
			params.put("create spatial index", Boolean.TRUE);
			
			newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
			newDataStore.createSchema(sfs.getSchema());
			
			 /*
	         * Write the features to the shapefile
	         */
	        DefaultTransaction transaction = new DefaultTransaction("create");

	        String typeName = newDataStore.getTypeNames()[0];
	        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

	        if (featureSource instanceof SimpleFeatureStore) {
	            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
	            featureStore.setTransaction(transaction);
	            try {
	                featureStore.addFeatures(sfs);
	                transaction.commit();
	            } catch (Exception problem) {
	                problem.printStackTrace();
	                transaction.rollback();
	            } finally {
	                transaction.close();
	            }
	        } else {
	        	throw new WPSException(typeName + " does not support read/write access");
	        }
			
			newDataStore.forceSchemaCRS(sfs.getSchema().getCoordinateReferenceSystem());
		}
		catch (Exception e) {
			LOGGER.error("errore nella scrittura della simpleFeatureCollection " + sfs.getSchema(), e);
			throw new WPSException("errore during writing simpleFeatureCollection " + sfs.getSchema(), e);
		}
		finally {
			try {
				if (newDataStore != null) {
					newDataStore.dispose();
				}
			}
			catch (Throwable e) {
				LOGGER.error("errore nella chiusura del writer ", e);
				throw new WPSException("errore during closing sfs " + sfs.getSchema(), e);
			}
		}
		return true;
	}
	
	/*
	 * cancella i file temporanei
	 */
	public static boolean deleteTmpFile(String filePath) {
		File tmpFile = new File(filePath);
		if (tmpFile != null && tmpFile.exists()) {
			LOGGER.debug("cancello il file temporaneo " + tmpFile.getName());
			return tmpFile.delete();
		}
		return false;
	}
	
	/*
	 * restituisce il raster in formato GridCoverage2D da un file su fs
	 */
	public static GridCoverage2D getGridCoverageFromFile(File file, CoordinateReferenceSystem coords) {
		GridCoverage2D gridCoverage = null;
		try {
			gridCoverage = GridFormatFinder.findFormat(file)
					.getReader(file, new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, coords)).read(null);
		}
		catch (Exception e) {
			LOGGER.error("errore nella lettura del raster ", e);
			e.printStackTrace();
			throw new WPSException("errore during read raster " + gridCoverage.getName(), e);
		}
		return gridCoverage;
	}
	
	/*
	 * clip del raster -> dava errore di dimensione diversa tra raster
	 */
	public static String clip(String inputFile, ReferencedEnvelope boundingBox) {
		//es: gdal_translate -of GTiff -projwin 12.2017225465 46.8696467203 14.1100550674 45.3826431966 C:/Users/a2sb0132/Desktop/envplus/dem_wgs84.tif "[temporary file]"
		//controllare il boundingbox
		String inputFileReplace = new File(inputFile).getName().replace(".", "");
		String projwin = boundingBox.getMinX() + " " + boundingBox.getMaxY() + " " + boundingBox.getMaxX() + " "
				+ boundingBox.getMinY();
		
		String clipRasterFile = ProjectPropertiesSolar.loadByName(Constants.TMP_PATH) + inputFileReplace + "_" + System.currentTimeMillis() + Constants.TIF_EXTENSION;
		if (inputFile != null) {
			LOGGER.debug("inizio clip raster " + inputFile);
			clipRasterFile = GdalOperation.translateForClip(inputFile, clipRasterFile, projwin);
			LOGGER.debug("fine clip " + clipRasterFile);
		}
		return clipRasterFile;
	}
	
}
