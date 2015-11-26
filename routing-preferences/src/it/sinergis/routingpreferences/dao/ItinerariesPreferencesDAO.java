package it.sinergis.routingpreferences.dao;

import java.util.List;

import it.sinergis.routingpreferences.model.ItinerariesPreferences;


public interface ItinerariesPreferencesDAO {
	
	void saveItinerary(String jsonText) throws Exception;
	
	List<ItinerariesPreferences> readItinerary(String query) throws Exception;

}
