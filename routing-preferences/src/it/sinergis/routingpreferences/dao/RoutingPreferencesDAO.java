package it.sinergis.routingpreferences.dao;

/**
 * Interfaccia dei servizi.
 * 
 * @author Andrea Di Nora
 */
import it.sinergis.routingpreferences.model.RoutingPreferences;

import java.util.List;

public interface RoutingPreferencesDAO {

	//void insertRoutingPreferences(RoutingPreferencesInsertRequest routingPreferencesInsertRequest) throws Exception;
	
	List<RoutingPreferences> searchRoutingPreferences(String userId) throws Exception;
	
	//void deleteRoutingPreference(RoutingPreferencesDeleteRequest routingPreferencesRequest) throws  Exception;

	void insertRoutingPreferences(String jsonText) throws Exception;

	void deleteRoutingPreference(String queryText) throws Exception;
}
