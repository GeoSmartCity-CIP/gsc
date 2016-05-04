package eu.geosmartcity.hub.utils;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import eu.geosmartcity.hub.delegate.PostgisDelegate;
import eu.geosmartcity.hub.velocity.SetupInsertSensorTemplate;

public class Uploadservices {
	final static Logger LOGGER = Logger.getLogger(Uploadservices.class);
	
	public List checkTable(String schema, String table, Transaction tx, Session session) {
		SetupInsertSensorTemplate template = new SetupInsertSensorTemplate();
		String sql = template.getCheckTableTemplate(schema, table);
		PostgisDelegate pg = new PostgisDelegate();
		List result = pg.getResultList(sql, tx, session);
		
		return result;
		
	}
	
	public boolean createTable(String epsg, Transaction tx, Session session) {
		boolean state = false;
		SetupInsertSensorTemplate template = new SetupInsertSensorTemplate();
		String sql = template.createInheritsTable(epsg);
		PostgisDelegate pg = new PostgisDelegate();
		boolean result = pg.insertGeometry(sql, tx, session);
		if (result) {
			state = this.replaceTriggerFunction(epsg, tx, session);
		}
		return state;
		//prendere il template per creare una tabella ereditata
		//poi chiamare il replacetrigger function
	}
	
	public String readTriggerFunction(Transaction tx, Session session) {
		//leggere da config il nome della funzione
		SetupInsertSensorTemplate template = new SetupInsertSensorTemplate();
		String sql = template.readTriggerFunction(ReadFromConfig.loadByName("triggerName"));
		PostgisDelegate pg = new PostgisDelegate();
		List<String> triggerResult = pg.getResultList(sql, tx, session);
		if (!triggerResult.isEmpty()) {
			return triggerResult.get(0);
		}
		else
			return null;
	}
	
	public boolean replaceTriggerFunction(String epsg, Transaction tx, Session session) {
		boolean result = false;
		String trigger = this.readTriggerFunction(tx, session);
		if (trigger != null) {
			String[] splitTrigger = trigger.split("ELSE");
			SetupInsertSensorTemplate template = new SetupInsertSensorTemplate();
			String portion = template.getPortionTrigger(epsg);
			String sqlReplaceNewTrigger = splitTrigger[0] + " " + portion + " ELSE " + splitTrigger[1];
			PostgisDelegate pg = new PostgisDelegate();
			boolean stateReplace = pg.insertGeometry(sqlReplaceNewTrigger, tx, session);
			result = true && stateReplace;
		}
		return result;
		//splittare il text ottenuto dalla funzione sopra per ELSE
		//prendere il primo pezzo.. aggiungere 
		//		ELSIF ( ST_SRID(NEW.geometry) = 4326 ) THEN
		//        INSERT INTO wps_solar.buildings_4326 VALUES (NEW.*);
		//		ELSE e text[2]
	}
	
}
