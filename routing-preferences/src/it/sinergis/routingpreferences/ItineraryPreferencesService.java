package it.sinergis.routingpreferences;

import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.sinergis.routingpreferences.dao.ItinerariesPreferencesDAO;
import it.sinergis.routingpreferences.exception.RPException;
import it.sinergis.routingpreferences.model.ItinerariesPreferences;

/***
 * Class to handle ItineraryPreference requests.
 * 
 * @author Lorenzo Longhitano
 * 
 */
public class ItineraryPreferencesService extends ServiceCommons {

	/** Logger. */
	private static Logger logger;
	
	
	/** ItinerariesPreferences DAO. */
	ItinerariesPreferencesDAO itinerariesPreferencesDAO;
	
	
	
	public final String ITINERARY_TABLE_NAME = "rp_t_itineraries";
	public final String ITINERARY_COLUMN_NAME = "data";
	
	public ItineraryPreferencesService() {
		logger = Logger.getLogger(this.getClass());	
		itinerariesPreferencesDAO = daoFactory.getItinerariesPreferencesDAO();
	}
	
	/**
	 * Saves an itinerary.
	 * 
	 * @param jsonItineraryPreferenceText this parameter has to be a json response returned by the opentripplanner services.
	 * @param userId the userId that is logged and who is saving the itinerary.
	 * @param itineraryId represents the index of the selected itinerary that has to be saved among all the itineraries avaliable in the opentripplanner response json.
	 * @param notes additional notes to save within the json.
	 * 
	 * @return a json string containing the id of the newly created itinerary if the operation succeeded or an error otherwise.
	 */
	public String saveItinerary(String jsonItineraryPreferenceText,String userId,String itineraryId,String notes) {		
		try {
			checkJsonWellFormed(jsonItineraryPreferenceText);
			
			String modifiedJSON = adjustJSON(jsonItineraryPreferenceText,userId,itineraryId,notes);
			Long persistedItineraryId = daoFactory.getItinerariesPreferencesDAO().saveItinerary(modifiedJSON);
			return jsonifyResult("itineraryId",persistedItineraryId.toString());
			
		} catch(RPException rpe) {
			return rpe.returnErrorString();
		} catch(Exception e) {
			logger.error("Save itineraries service error",e);
			RPException rpe = new RPException("ER01");
			logger.error("saveItinerary service: unhandled error "+rpe.returnErrorString());
			return rpe.returnErrorString();
		}
	}
	
	/**
	 * Returns all the saved itineraries matching with the given query.
	 * 
	 * @param jsonItineraryPreferenceText The research query.
	 * 				Must follow the following syntax:
	 * 				-	from the json root node type all the names of all the json nodes of the json tree until you reach the desired element.
	 * 				-   each element must be quoted (')
	 * 				-   each element must be divided from the following elements by a '/'
	 * 				-   all operators allowed in the PSQL database are still legal.
	 * 
	 * 				E.g.: 'itineraries'/'duration' = '9999' 
	 * 						implies that one of the json root direct descendants is called "itinerary" and
	 * 						that "itinerary" has a direct discendant called "duration". 
	 * 				
	 * @return a json string containing the list of all the itineraries matching the given query. Each of those is inserted in a wider json that 
	 * 				has "json" as the root element, and contains a list of the found itineraries.
	 */
	public String getItineraries(String jsonItineraryPreferenceText) {
		try {
			String query = createQuery(jsonItineraryPreferenceText,ITINERARY_TABLE_NAME,ITINERARY_COLUMN_NAME,"select");
			StringBuilder sb = new StringBuilder();
			if(query == null) {
				throw new RPException("ER03");
			}
			List<ItinerariesPreferences> ipList = daoFactory.getItinerariesPreferencesDAO().readItinerary(query);
			if(ipList == null || ipList.isEmpty()) {
				logger.error("getItineraries: no records found.");
				throw new RPException("ER05");
			} else {	
				logger.info(ipList.size()+" itineraries found.");	
				sb.append("{\"json\":[");
				for(int i = 0; i< ipList.size(); i++) {
					if(i != 0) {
						sb.append(",");
					}
					logger.info(ipList.get(i).getData());
					sb.append(cleanData(ipList.get(i).getData()));
				}
				sb.append("]}");
			}
			return sb.toString();
		} catch(RPException rpe) {
			return rpe.returnErrorString();
		} catch(Exception e) {
			logger.error("Get Itineraries service error",e);
			RPException rpe = new RPException("ER01");
			logger.error("getItineraries service: unhandled error "+rpe.returnErrorString());
			return rpe.returnErrorString();
		}
	}
	
	/**
	 * Delete an itinerary given its id
	 * 
	 * @param id The itinerary id in the itineraries table.
	 * @return a json string containing the id of the deleted itinerary if the operation succeeded or an error otherwise.
	 */
	public String deleteItinerary(Long id) {
		try {
			itinerariesPreferencesDAO.deleteItineraryById(id);
			logger.info("Itinerary "+id+" succesfully deleted.");
			return jsonifyResult("itineraryId", id.toString());
		} catch(RPException rpe) {
			return rpe.returnErrorString();
		} catch(Exception e) {
			logger.error("Delete itineraries service error",e);
			RPException rpe = new RPException("ER01");
			logger.error("deleteItinerary service: unhandled error "+rpe.returnErrorString());
			return rpe.returnErrorString();
		}
	}
	
	/**
	 * Adds userId and notes within the jsonText. Deletes all itineraries of the plan that don't match the given index.
	 * 
	 * @param testo
	 * @param userId
	 * @param itineraryId
	 * @param notes
	 * @return
	 * @throws RPException
	 */
	private String adjustJSON(String testo, String userId, String itineraryId,
			String notes) throws RPException {
		try {
			if(userId == null || userId.isEmpty()) {
				logger.error("userId parameter was not specified.");
				throw new RPException("ER04");
			}
			if(itineraryId == null || itineraryId.isEmpty()) {
				logger.error("itineraryId parameter was not specified.");
				throw new RPException("ER04");
			}
			if(notes == null || notes.isEmpty()) {
				logger.error("notes parameter was not specified.");
				throw new RPException("ER04");
			}
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode jsonTreeRootObject = null;
			JsonNode jsonTreeRoot = mapper.readTree(testo);

			JsonNode jsonValue = jsonTreeRoot.findValue("itineraries");
			
			if(jsonTreeRoot instanceof ObjectNode) {
				jsonTreeRootObject = (ObjectNode) jsonTreeRoot;
				jsonTreeRootObject.put("userId", userId);
				jsonTreeRootObject.put("notes", notes);
				if(jsonValue.isArray()) {
					JsonNode selectedItinerary = jsonValue.get(Integer.parseInt(itineraryId) -1);
					if(selectedItinerary == null) {
						logger.error("cannot save without one itinerary within the json string. Check if the chosen itineraryId is valid.");
						throw new RPException("ER08");
					}
					ObjectNode itinerariesParent = jsonTreeRootObject.findParent("itineraries");
					itinerariesParent.replace("itineraries",selectedItinerary);
				}
				
			}
			return mapper.writeValueAsString(jsonTreeRootObject);
				
		} catch(RPException rpe) {
			throw rpe;
		} catch(NumberFormatException nfe) {
			logger.error("cannot save without one itinerary within the json string. Check if the chosen itineraryId is a number.");
			throw new RPException("ER08");
		} catch(Exception e) {
			logger.error("Save itineraries service error",e);
			throw new RPException("ER01");
		}
	}
	
	/**
	 * Removes the notes and the userId field that were artificially added to the json in the saving process. This happens as a final step to the getItineraries method.
	 * 
	 * @param jsonDataRetrieved
	 * @return
	 * @throws RPException
	 */
	private String cleanData(String jsonDataRetrieved) throws RPException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode jsonTreeRootObject = null;
		
		try {
			JsonNode jsonTreeRoot = mapper.readTree(jsonDataRetrieved);
			
			if(jsonTreeRoot instanceof ObjectNode) {
				jsonTreeRootObject = (ObjectNode) jsonTreeRoot;
				jsonTreeRootObject.remove("userId");
				jsonTreeRootObject.remove("notes");
			}
			return mapper.writeValueAsString(jsonTreeRootObject);
		} catch(Exception e) {
			logger.error("Get itineraries service error",e);
			throw new RPException("ER01");
		}
	}
}