package it.sinergis.routingpreferences;

import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import it.sinergis.routingpreferences.dao.RoutingPreferencesDAO;
import it.sinergis.routingpreferences.exception.RPException;
import it.sinergis.routingpreferences.model.RoutingPreferences;

/***
 * Class to handle RoutingPreference requests.
 * 
 * @author Lorenzo Longhitano
 * 
 */
public class RoutingPreferencesService extends ServiceCommons {

	/** Logger. */
	private static Logger logger;

	/** RoutingPreferences DAO. */
	RoutingPreferencesDAO routingPreferencesDAO;;

	public final String ROUTING_TABLE_NAME = "rp_t_preferences2";
	public final String ROUTING_COLUMN_NAME = "data";
	
	public RoutingPreferencesService() {
		logger = Logger.getLogger(this.getClass());
		routingPreferencesDAO = daoFactory.getRoutingPreferencesDAO();
	}
	
	/**
	 * Saves a json preference string. Users can insert all the preferences they like, as long as those are presented as json. 
	 * The only mandatory parameter is "userId" that has to be included in the json. 
	 * If the user identified by "userId" has already saved a preference, the old preference will be replaced.
	 * 
	 * @param jsonRoutingPreferenceText the preferences parameters as json text.
	 * @return a json string containing the id of the user that is saving the preferences if the operation succeeded or an error otherwise.
	 */
	public String saveOrUpdatePreferences(String jsonRoutingPreferenceText) {

		try {
			checkJsonWellFormed(jsonRoutingPreferenceText);
			
			//check if there's another preference already saved for the same userId
			RoutingPreferences routingPreferences = getPreferencesObject(jsonRoutingPreferenceText);
				
			//if no results found -> add new record
			if(routingPreferences == null) {
				routingPreferencesDAO.insertRoutingPreferences(jsonRoutingPreferenceText);
				
				logger.info("Preferences succesfully saved");
				logger.info(jsonRoutingPreferenceText);
				
				return jsonifyResult("userId",getUserIdFromJsonText(jsonRoutingPreferenceText));
			//otherwise update current record
			} else {
				String queryText = "'userId' = '"+getUserIdFromJsonText(jsonRoutingPreferenceText)+"'";
				routingPreferencesDAO.updateRoutingPreferences(jsonRoutingPreferenceText,createQuery(queryText,ROUTING_TABLE_NAME,ROUTING_COLUMN_NAME,"delete"));

				logger.info("Preferences succesfully updated");
				logger.info(jsonRoutingPreferenceText);
				return jsonifyResult("userId",getUserIdFromJsonText(jsonRoutingPreferenceText));
			}
		} catch(RPException rpe) {
			return rpe.returnErrorString();
		} catch(Exception e) {
			logger.error("save/update preferences service error",e);
			RPException rpe = new RPException("ER01");
			logger.error("saveOrUpdatePreferences service: unhandled error "+rpe.returnErrorString());
			return rpe.returnErrorString();
		}
	}
	
	/**
	 * Get the preferences saved for the given user.
	 * 
	 * @param jsonRoutingPreferenceText a json text containing the "userId" field.
	 * @return a json string containing the preferences for the specified user if the operation succeeded or an error otherwise.
	 */
	public String getPreferences(String jsonRoutingPreferenceText) {
		try {
			
			checkJsonWellFormed(jsonRoutingPreferenceText);
			
			RoutingPreferences routingPreferences = getPreferencesObject(jsonRoutingPreferenceText);
			
			if(routingPreferences == null) {
				logger.error("getPreferences: no records found.");
				throw new RPException("ER05");
			}
			logger.info("Preferences succesfully retrieved");
			logger.info(routingPreferences.getData());
			
			return routingPreferences.getData();
		} catch(RPException rpe) {
			return rpe.returnErrorString();
		} catch(Exception e) {
			logger.error("Get preferences service error",e);
			RPException rpe = new RPException("ER01");
			logger.error("getPreferences service: unhandled error "+rpe.returnErrorString());
			return rpe.returnErrorString();
		}
	}
	
	/**
	 * Delete the preferences for a given user
	 * 
	 * @param jsonRoutingPreferenceText jsonRoutingPreferenceText a json text containing the "userId" field.
	 * @return a json string containing the id of the user that is deleting the preferences if the operation succeeded or an error otherwise.
	 */
	public String deletePreferences(String jsonRoutingPreferenceText) {
		try {
			
			checkJsonWellFormed(jsonRoutingPreferenceText);
			
			String queryText = "'userId' = '"+getUserIdFromJsonText(jsonRoutingPreferenceText)+"'";
			String query = createQuery(queryText, ROUTING_TABLE_NAME,ROUTING_COLUMN_NAME,"delete");
			routingPreferencesDAO.deleteRoutingPreferences(query);
			
			logger.info("Preferences for userId="+getUserIdFromJsonText(jsonRoutingPreferenceText)+"succesfully deleted");
			
			return jsonifyResult("userId",getUserIdFromJsonText(jsonRoutingPreferenceText));
		} catch(RPException rpe) {
			return rpe.returnErrorString();
		} catch(Exception e) {
			logger.error(" preferences service error",e);
			RPException rpe = new RPException("ER01");
			logger.error("deletePreferences service: unhandled error "+rpe.returnErrorString());
			return rpe.returnErrorString();
		}
	}
	
	/**
	 * Retrieves the preferences given a userId.
	 * 
	 * @param jsonRoutingPreferenceText
	 * @return
	 * @throws RPException
	 */
	private RoutingPreferences getPreferencesObject(String jsonRoutingPreferenceText) throws RPException {
		
		try {
			String queryText = "'userId' = '"+getUserIdFromJsonText(jsonRoutingPreferenceText)+"'";
			String query = createQuery(queryText, ROUTING_TABLE_NAME,ROUTING_COLUMN_NAME,"select");
			List<RoutingPreferences> routingPreferences = routingPreferencesDAO.getRoutingPreferences(query);
			
			if(routingPreferences.isEmpty()) {
				return null;
			}
			//research query can only find 1 record at most
			return routingPreferences.get(0);
		} catch(RPException rpe) {
			throw rpe;
		} catch(Exception e) {
			logger.error("unhandled error: ",e);
			throw new RPException("ER01");
		}
	}
	
	/**
	 * Returns userId within the input json text parameter.
	 * 
	 * @param jsonRoutingPreferenceText
	 * @return
	 * @throws RPException
	 */
	private String getUserIdFromJsonText(String jsonRoutingPreferenceText) throws RPException {
		try {
			JsonNode rootNode = om.readTree(jsonRoutingPreferenceText);
			JsonNode idValue = rootNode.findValue("userId");
			if(idValue == null) {
				logger.error("userId parameter is mandatory within the json string.");
				throw new RPException("ER04");
			}
			return idValue.toString();
		} catch(RPException rpe) {
			throw rpe;
		} catch(Exception e) {
			logger.error("unhandled error: ",e);
			throw new RPException("ER01");
		}
	}
}