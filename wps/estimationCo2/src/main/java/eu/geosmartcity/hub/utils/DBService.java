package eu.geosmartcity.hub.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.geoserver.wps.WPSException;

import eu.geosmartcity.hub.wps.BuildingCo2EmissionsEstimation;

public class DBService {
	
	private static final Logger LOGGER = Logger.getLogger(BuildingCo2EmissionsEstimation.class);
	
	private String jdbc;
	private String user;
	private String password;
	
	private Connection con;
	
	public DBService() throws SQLException {
		try {
			jdbc = ProjectProperties.loadByName("database");
			user = ProjectProperties.loadByName("user");
			password = ProjectProperties.loadByName("password");
			
			con = DriverManager.getConnection(jdbc, user, password);
		}
		catch (Exception e) {
			LOGGER.error("impossibile connettersi al db " + ProjectProperties.loadByName("database") + " con utente "
					+ ProjectProperties.loadByName("user") + " e pwd " + ProjectProperties.loadByName("password"), e);
		}
		
	}
	
	/**
	 * restituisce il value della tabella conversion data la location, l'anno e il fuel
	 * 
	 * @param location
	 * @param year
	 * @param fuel
	 * @return il value
	 * @throws SQLException
	 */
	public Double getValueFromConversionTable(String location, String year, String fuel, String uom,
			String isConsumption) throws SQLException {
		
		Connection connection = con;
		Statement stmt = connection.createStatement();
		try {
			String query = "SELECT * FROM " + ProjectProperties.loadByName("schema_table_conversion") + "."
					+ ProjectProperties.loadByName("table_conversion") + " WHERE lower(location) ='"
					+ location.toLowerCase() + "' AND year = '" + year + "'" + " AND lower(input)='" + uom.toLowerCase()
					+ "'";
			
			if (isConsumption != null && isConsumption.equalsIgnoreCase("true")) {
				query += " AND lower(fuel) = '" + fuel.toLowerCase() + "'";
			}
			LOGGER.info("query sulla tabella conversion " + query);
			
			ResultSet result = stmt.executeQuery(query);
			if (result != null && result.next()) {
				return result.getDouble("value");
			}
			
		}
		catch (Exception e) {
			LOGGER.error("errore nel recupero del value dalla tabella conversion", e);
			throw new WPSException("error while get value from conversion table");
		}
		finally {
			stmt.close();
		}
		return null;
	}
	
	public void closeConnection() {
		try {
			con.close();
		}
		catch (Exception e) {
			LOGGER.error("errore nella chiusura della connessione al db", e);
		}
	}
	
	public static void main(String args[]) throws SQLException {
		String dbUrl = "jdbc:postgresql://gsm-db:5432/geomap4data";
		String user = "postgres";
		String psw = "postgres";
		System.out.println("prova");
		DBService dbEnergy = new DBService();
		Double value = dbEnergy.getValueFromConversionTable("IT", "01-01-2016", "naturalGas", "M3", "true");
		LOGGER.info("value " + value);
		//dbEnergy.computeAreaPer();
		//dbEnergy.updateFloor();
		//dbEnergy.updateALL("2");
		
	}
	
}
