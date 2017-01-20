package eu.geosmartcity.hub.utils;

import org.apache.log4j.Logger;
import org.geoserver.wps.WPSException;

public class GdalOperation {
	
	private static String GDAL_TRANSLATE = "gdal_translate";
	private static String GTIFF = "GTiff";
	
	private static final Logger LOGGER = Logger.getLogger(GdalOperation.class);
	
	/*
	 * esegue gdal_translate per clip
	 */
	public static String translateForClip(String inputRaster, String outputRaster, String boundingBox) {
		try {
			String command = ProjectPropertiesSolar.loadByName("gdal_path") + GDAL_TRANSLATE + " -projwin " + boundingBox
					+ " -of GTiff ";
			
			command += inputRaster + " " + outputRaster;
			LOGGER.debug("comando eseguito " + command);
			ProcessUtils.exec(command);
		}
		catch (Exception e) {
			LOGGER.error("errore nella traslazione per il clip del " + inputRaster, e);
			e.printStackTrace();
			throw new WPSException("errore during translate for file " + inputRaster, e);
		}
		return outputRaster;
	}
	
	/*
	 * esegue gdal_translate per normalizzare il raster
	 * @param inputDem path raster in input
	 * @return outputDem path raster in output
	 */
	public static String translateToNormalize(String inputRaster, String outputRaster, String srs) {
		try {
			// gdal_translate -of GTiff -a_srs EPSG:4326 file1.tif file2.tif
			String command = ProjectPropertiesSolar.loadByName("gdal_path") + GDAL_TRANSLATE + " --config GDAL_DATA \""
					+ ProjectPropertiesSolar.loadByName("gdal_data") + "\" -of " + GTIFF + " -a_srs " + srs + " "
					+ inputRaster + " " + outputRaster;
			
			LOGGER.debug("comando eseguito " + command);
			ProcessUtils.exec(command);
		}
		catch (Exception e) {
			LOGGER.error("errore nella traslazione del " + inputRaster, e);
			e.printStackTrace();
			throw new WPSException("errore during translate for file " + inputRaster, e);
		}
		return outputRaster;
	}
}
