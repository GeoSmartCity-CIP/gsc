package eu.geosmartcity.hub.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ProjectProperties {
	
	private static Logger LOGGER = Logger.getLogger(ProjectProperties.class);
	private static Properties propertiesBuildingCo2Emissions;
	private static Properties messages;
	
	private static Properties readFromConfig() {
		InputStream inputStream = ProjectProperties.class.getClassLoader().getResourceAsStream("configBuildingCo2Emissions.properties");
		propertiesBuildingCo2Emissions = new Properties();
		try {
			propertiesBuildingCo2Emissions.load(inputStream);
		}
		catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("errore nel caricamento del file di configurazione", e);
		}
		return propertiesBuildingCo2Emissions;
	}
	
	public static String loadByName(String property) {
		if (propertiesBuildingCo2Emissions==null) {
			propertiesBuildingCo2Emissions = readFromConfig();
		} 
		return (String) propertiesBuildingCo2Emissions.get(property);
	}
	
	private static Properties readFromMessages() {
		InputStream inputStream = ProjectProperties.class.getClassLoader().getResourceAsStream("messages.properties");
		messages = new Properties();
		try {
			messages.load(inputStream);
		}
		catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("errore nel caricamento del file di messages", e);
		}
		return messages;
	}
	
	public static String loadByNameFromMessages(String property) {
		if (messages==null) {
			messages = readFromMessages();
		} 
		return (String) messages.get(property);
	}
}
