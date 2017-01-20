package it.sinergis.routingpreferences.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyReader {

	InputStream inputStream;
	Properties prop;
	
	/** Logger. */
	private static Logger logger;
	
	public PropertyReader(String propFileName) {
		logger = Logger.getLogger(this.getClass());
		try {
			prop = new Properties();
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch(IOException e) {
				logger.error("error while reading error messages property file");
			}
		}
	}
	
	public String getValue(String key) {
		return prop.getProperty(key);
	}
}
