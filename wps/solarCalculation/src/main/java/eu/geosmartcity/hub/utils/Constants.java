package eu.geosmartcity.hub.utils;

public class Constants {
	public final static String WPS_TITLE = "wps GEOSMARTCITY";
	public final static String WPS_NAMESPACE = "gsc";
	
	public final static String TMP_PATH = "tmp_path";
	
	//estensioni
	public final static String TIF_EXTENSION = ".tif";
	public static final String GML_EXTENSION = ".gml";
	
	//url per epsg nel gml
	public static final String OPENGIS_URL_EPSG = "http://www.opengis.net/gml/srs/epsg.xml#";
	//prefisso del gml
	public static final String PREFIX_GML = "gsc";
	public static final String EPSG = "EPSG:";
	
	//parametri per lo script che esegue le funzioni grass
	public static final String PATH_SCRIPT = ProjectProperties.loadByName("path_script");
	public static final String MAPSET = ProjectProperties.loadByName("mapset");
	public static final String LOCATION = ProjectProperties.loadByName("location");
	
	public static final String GEOSERVER_REST_URL = ProjectProperties.loadByName("geoserver_rest_url");
	public static final String GEOSERVER_REST_USER = ProjectProperties.loadByName("geoserver_rest_user");
	public static final String GEOSERVER_REST_PW = ProjectProperties.loadByName("geoserver_rest_pw");
	public static final String GEOSERVER_WS_TEMP = ProjectProperties.loadByName("geoserver_ws_temp");
	public static final String GEOSERVER_STORE = ProjectProperties.loadByName("geoserver_store");
	public static final String WMS_VERSION = ProjectProperties.loadByName("wms_version");
	public static final String RASTER_WIDTH = ProjectProperties.loadByName("width_raster");
	public static final String RASTER_HEIGHT = ProjectProperties.loadByName("height_raster");
	
	public static final String DATE_FORMAT = ProjectProperties.loadByName("dateFormat");
	public static final String SOLAR_STYLE = ProjectProperties.loadByName("solar_style");
	
	
}
