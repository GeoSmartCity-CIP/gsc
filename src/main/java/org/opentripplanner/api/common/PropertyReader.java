package org.opentripplanner.api.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

	InputStream inputStream;
	Properties prop;
	
	public PropertyReader(String propFileName) throws IOException {
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
			inputStream.close();
		}
	}
	
	public String getValue(String key) {
		return prop.getProperty(key);
	}
}
