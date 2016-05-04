package eu.geosmartcity.hub.velocity;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import eu.geosmartcity.hub.utils.ReadFromConfig;

public class SetupInsertSensorTemplate {
	private static final Logger LOGGER = Logger.getLogger(SetupInsertSensorTemplate.class);
	
	public final String getTemplateInsertMultiPolygon(String z_coord, String multipolygon, String epsg, String timestamp) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the insertSensor request";
		
		hasht.put("z_coord", z_coord);
		hasht.put("multipolygon", multipolygon);
		hasht.put("epsg", epsg);
		hasht.put("timestamp", timestamp);
		
		try {
			page = vt.getRequestXmlVelocity("InsertMultipolygon", hasht);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	public final String getTemplateInsertMultiPolygonFromPolygon(String polygon, String epsg, String timestamp) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the insertSensor request";
		
		hasht.put("polygon", polygon);
		hasht.put("epsg", epsg);
		hasht.put("timestamp", timestamp);
		
		try {
			page = vt.getRequestXmlVelocity("InsertPolygonZtoMultipolygonZ", hasht);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	/**
	 * inserisce un multipolygon dal gml
	 * 
	 * @param polygon
	 * @param epsg
	 * @return
	 */
	public final String getTemplateInsertMultiPolygonFromGML(String multipolygon, String epsg, String height,
			String inspireIdLoc, String inspireIdName, String timestamp) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the InsertMultiPolygonFromGML request";
		
		hasht.put("multipolygon", multipolygon);
		hasht.put("epsg", epsg);
		hasht.put("z_coord", height);
		
		if (inspireIdLoc == null) {
			inspireIdLoc = "null";
		}
		
		if (inspireIdName == null) {
			inspireIdName = "";
		}
		hasht.put("inspireIdLoc", inspireIdLoc);
		hasht.put("inspireIdName", inspireIdName);
		hasht.put("timestamp", timestamp);
		
		try {
			page = vt.getRequestXmlVelocity("InsertMultipolygonFromGML", hasht);
			LOGGER.info(page);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	/**
	 * inserisce un multipolygonZ dal gml
	 * 
	 * @param multipolygonZ
	 * @param epsg
	 * @param height
	 * @return
	 */
	public final String getTemplateInsertMultiPolygonZFromGML(String multipolygonZ, String epsg, String height,
			String inspireIdLoc, String inspireIdName, String timestamp) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the InsertMultiPolygonFromGML request";
		
		hasht.put("multipolygonZ", multipolygonZ);
		hasht.put("epsg", epsg);
		hasht.put("z_coord", height);
		if (inspireIdLoc == null) {
			inspireIdLoc = "null";
		}
		if (inspireIdName == null) {
			inspireIdName = "null";
		} else {
			inspireIdName = "'"+inspireIdName+"'";
		}
		hasht.put("inspireIdLoc", inspireIdLoc);
		hasht.put("inspireIdName", inspireIdName);
		hasht.put("timestamp", timestamp);
		
		try {
			page = vt.getRequestXmlVelocity("InsertMultipolygonZFromGML", hasht);
			LOGGER.info(page);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	public final String getZCoord(String gml, String epsg) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the request for check Z coord";
		
		hasht.put("multipolygonGML", gml);
		hasht.put("epsg", epsg);
		
		try {
			page = vt.getRequestXmlVelocity("GetZCoord", hasht);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	public final String getCheckTableTemplate(String schema, String table) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the request for check Z coord";
		
		hasht.put("schema", schema);
		hasht.put("table", table);
		
		try {
			page = vt.getRequestXmlVelocity("CheckTableExistsTemplate", hasht);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	public final String createInheritsTable(String epsg) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the request for check Z coord";
		
		hasht.put("epsg", epsg);
		
		try {
			page = vt.getRequestXmlVelocity("CreateNewInheritsTable", hasht);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	public final String readTriggerFunction(String functionName) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the request for check Z coord";
		
		hasht.put("functionName", functionName);
		
		try {
			page = vt.getRequestXmlVelocity("ReadTriggerFunction", hasht);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	public final String getPortionTrigger(String epsg) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the request for check Z coord";
		
		hasht.put("epsg", epsg);
		
		try {
			page = vt.getRequestXmlVelocity("TriggerPortion", hasht);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	public final String getWmsRequest(String timestamp, String tableName) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the request wmsrequest";
		
		hasht.put("timestamp", timestamp);
		hasht.put("uriGeoserver", ReadFromConfig.loadByName("uriGeoserver"));
		hasht.put("workspaceGeoserver", ReadFromConfig.loadByName("workspaceGeoserver"));
		hasht.put("tableName", tableName);
		
		try {
			page = vt.getRequestXmlVelocity("WmsRequest", hasht);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	public final String getWfsRequest(String timestamp, String tableName) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the request wfsrequest";
		
		hasht.put("timestamp", timestamp);
		hasht.put("Geo", ReadFromConfig.loadByName("uriGeoserver"));
		hasht.put("workspaceGeoserver", ReadFromConfig.loadByName("workspaceGeoserver"));
		hasht.put("tableName", tableName);
		
		try {
			page = vt.getRequestXmlVelocity("WfsRequest", hasht);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	public final String getJsonRequest(String timestamp, String tableName) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the request jsonrequest";
		
		hasht.put("timestamp", timestamp);
		hasht.put("Geo", ReadFromConfig.loadByName("uriGeoserver"));
		hasht.put("workspaceGeoserver", ReadFromConfig.loadByName("workspaceGeoserver"));
		hasht.put("tableName", tableName);
		
		try {
			page = vt.getRequestXmlVelocity("JsonRequest", hasht);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
	public final String getShapeRequest(String timestamp, String tableName) {
		VelocityTemplate vt = new VelocityTemplate();
		Hashtable<String, String> hasht = new Hashtable<String, String>();
		String page = "Error creating the request shaperequest";
		
		hasht.put("timestamp", timestamp);
		hasht.put("uriGeo", ReadFromConfig.loadByName("uriGeoserver"));
		hasht.put("workspaceGeoserver", ReadFromConfig.loadByName("workspaceGeoserver"));
		hasht.put("tableName", tableName);
		
		try {
			page = vt.getRequestXmlVelocity("ShapeRequest", hasht);
		}
		catch (Exception e) {
			LOGGER.error("Load of template, unsuccessfully");
			LOGGER.debug(e);
		}
		return page;
	}
	
}
