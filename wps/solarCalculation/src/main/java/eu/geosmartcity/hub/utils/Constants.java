package eu.geosmartcity.hub.utils;

public class Constants {
	public final static String WPS_TITLE = "wps GEOSMARTCITY";
	public final static String WPS_NAMESPACE = "gsc";
	
	public final static String TMP_PATH = "tmp_path";
	
	//estensioni
	public final static String TIF_EXTENSION = ".tiff";
	public static final String GML_EXTENSION = ".gml";
	
	//url per epsg nel gml
	public static final String OPENGIS_URL_EPSG = "http://www.opengis.net/gml/srs/epsg.xml#";
	//prefisso del gml
	public static final String PREFIX_GML = "gsc";
	public static final String EPSG = "EPSG:";
	
	//parametri per lo script che esegue le funzioni grass
	public static final String PATH_SCRIPT_SOLAR = "path_script_solar";
	public static final String PATH_SCRIPT_ZERO_BALANCE = "path_script_zero_balance";
	public static final String MAPSET = "mapset";
	public static final String LOCATION = "location";
	
	public static final String GEOSERVER_REST_URL = "geoserver_rest_url";
	public static final String GEOSERVER_REST_USER ="geoserver_rest_user";
	public static final String GEOSERVER_REST_PW = "geoserver_rest_pw";
	public static final String GEOSERVER_WS_TEMP = "geoserver_ws_temp";
	public static final String WMS_VERSION = "wms_version";
	public static final String RASTER_WIDTH = "width_raster";
	public static final String RASTER_HEIGHT = "height_raster";
	
	public static final String DATE_FORMAT = "dateFormat";
	public static final String SOLAR_STYLE = "solar_style";
	public static final String ZERO_BALANCE_STYLE = "zero_balance_style";
	
	public static final String FIELD_ENERGY_SOLAR = "energy_solar";
	public static final String FIELD_ENERGY_CONSUMPTION = "energy_consumption";
	public static final String FIELD_ZERO_BALANCE = "zero_balance";
	public static final String ZERO_BALANCE_STORENAME = "zero_balance_storename";
}
