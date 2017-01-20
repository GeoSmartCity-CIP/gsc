package it.sinergis.routingpreferences.dao;

import java.util.List;

import it.sinergis.routingpreferences.exception.RPException;
import it.sinergis.routingpreferences.model.ItinerariesPreferences;


public interface ItinerariesPreferencesDAO {
	
	Long saveItinerary(String jsonText) throws Exception;
	
	List<ItinerariesPreferences> readItinerary(String query) throws Exception;

	void deleteItineraryById(Long id) throws RPException;

}
