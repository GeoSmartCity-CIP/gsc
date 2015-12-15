package it.sinergis.routingpreferences.dao;

/**
 * Interfaccia dei servizi.
 * 
 * @author Andrea Di Nora
 */
import it.sinergis.routingpreferences.model.RoutingPreferences;

import java.util.List;

public interface RoutingPreferencesDAO {
	
	List<RoutingPreferences> getRoutingPreferences(String userId) throws Exception;

	void insertRoutingPreferences(String jsonText) throws Exception;

	void deleteRoutingPreferences(String queryText) throws Exception;

	void updateRoutingPreferences(String jsonText, String userId) throws Exception;
}
