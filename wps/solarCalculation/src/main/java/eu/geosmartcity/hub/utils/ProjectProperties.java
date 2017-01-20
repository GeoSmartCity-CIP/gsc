package eu.geosmartcity.hub.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ProjectProperties {
	
	private static Logger LOGGER = Logger.getLogger(ProjectProperties.class);
	private static Properties properties;
	private static Properties messages;
	
	private static Properties readFromConfig() {
		InputStream inputStream = ProjectProperties.class.getClassLoader().getResourceAsStream("config.properties");
		properties = new Properties();
		try {
			properties.load(inputStream);
		}
		catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("errore nel caricamento del file di configurazione", e);
		}
		return properties;
	}
	
	public static String loadByName(String property) {
		if (properties==null) {
			properties = readFromConfig();
		} 
		return (String) properties.get(property);
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
