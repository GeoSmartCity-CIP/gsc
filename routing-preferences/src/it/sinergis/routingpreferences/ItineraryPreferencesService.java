package it.sinergis.routingpreferences;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.sinergis.routingpreferences.dao.DAOFactory;
import it.sinergis.routingpreferences.dao.ItinerariesPreferencesDAO;
import it.sinergis.routingpreferences.exception.RPException;
import it.sinergis.routingpreferences.jpadao.JpaDAOFactory;
import it.sinergis.routingpreferences.model.ItinerariesPreferences;

/***
 * Class to handle ItineraryPreference requests.
 * 
 * @author Lorenzo Longhitano
 * 
 */
public class ItineraryPreferencesService {

	/** Logger. */
	private static Logger logger;
	
	/** DAO factory. */
	DAOFactory daoFactory;
	
	/** ItinerariesPreferences DAO. */
	ItinerariesPreferencesDAO itinerariesPreferencesDAO;
	
	/** Jackson object mapper. */
	ObjectMapper om;

	
	public ItineraryPreferencesService() {
		logger = Logger.getLogger(this.getClass());
		daoFactory = new JpaDAOFactory();
		itinerariesPreferencesDAO = daoFactory.getItinerariesPreferencesDAO();
		om = new ObjectMapper();
	}
	
	public String saveItinerary(String jsonItineraryPreferenceText,String userId,String itineraryId,String notes) {		
		try {
			String modifiedJSON = adjustJSON(jsonItineraryPreferenceText,userId,itineraryId,notes);
			daoFactory.getItinerariesPreferencesDAO().saveItinerary(modifiedJSON);
			return modifiedJSON;
			
		} catch(RPException rpe) {
			return rpe.returnErrorString();
		} catch(Exception e) {
			RPException rpe = new RPException("ER01");
			logger.error("saveItinerary service: unhandled error "+rpe.returnErrorString());
			return rpe.returnErrorString();
		}
	}
	
	public String getItineraries(String jsonItineraryPreferenceText) {
		try {
			String query = createQuery(jsonItineraryPreferenceText,"rp_t_itineraries","data","select");
			StringBuilder sb = new StringBuilder();
			if(query == null) {
				throw new RPException("ER03");
			}
			List<ItinerariesPreferences> ipList = daoFactory.getItinerariesPreferencesDAO().readItinerary(query);
			if(ipList == null || ipList.isEmpty()) {
				logger.error("getItineraries: no records found.");
				throw new RPException("ER05");
			} else {	
				sb.append("{json:[");
				for(int i = 0; i< ipList.size(); i++) {
					if(i != 0) {
						sb.append(",");
					}
					sb.append(cleanData(ipList.get(i).getData()));
				}
				sb.append("]}");
			}
				
			return sb.toString();
		} catch(RPException rpe) {
			return rpe.returnErrorString();
		} catch(Exception e) {
			RPException rpe = new RPException("ER01");
			logger.error("getItineraries service: unhandled error "+rpe.returnErrorString());
			return rpe.returnErrorString();
		}
	}
	
//	public String deleteItineraries(String jsonItinerariesPreferenceText) {
//		try {
//			String queryText = "'userId' = '"+getUserIdFromJsonText(jsonItinerariesPreferenceText)+"'";
//			String query = createQuery(queryText, "rp_t_itineraries", "data","delete");
//			itinerariesPreferencesDAO.deleteItineraries(query);
//			return getUserIdFromJsonText(jsonItinerariesPreferenceText);
//		} catch(RPException rpe) {
//			return rpe.returnErrorString();
//		} catch(Exception e) {
//			RPException rpe = new RPException("ER01");
//			logger.error("deleteItinerary service: unhandled error "+rpe.returnErrorString());
//			return rpe.returnErrorString();
//		}
//	}
	
	/**
	 * Creates the actual research query from the semplified input string given by the user.
	 * 
	 * @param text
	 * @return
	 */
	private String createQuery(String text,String tableName,String columnName,String mode) {
		String query = "";
		if("delete".equalsIgnoreCase(mode)) {
			query += "delete from ";
		} else if("select".equalsIgnoreCase(mode)) {
			query += "select * from ";
		}
		query += tableName+" where ";
		
		try {
			String[] pieces = text.split("AND|OR");
			for(int i=0; i<pieces.length; i++) {
				int lastPieceElement = pieces[i].lastIndexOf("/");
				int firstBracketIndex = pieces[i].indexOf("(");

				String oldPiece = pieces[i];
				
				if(lastPieceElement != -1) {
					pieces[i] = pieces[i].substring(0,lastPieceElement).trim()+"->>"+pieces[i].substring(lastPieceElement+1).trim()+" ";
					if(firstBracketIndex != -1) {
						pieces[i] = pieces[i].substring(0,firstBracketIndex).trim()+" "+columnName+"->"+pieces[i].substring(firstBracketIndex).trim()+" ";
					} else {
						pieces[i] = " "+columnName+"->"+pieces[i].trim()+" ";
					}
				} else {
					if(firstBracketIndex != -1) {
						pieces[i] = pieces[i].substring(0,firstBracketIndex).trim()+" "+columnName+"->>"+pieces[i].substring(firstBracketIndex).trim()+" ";
					} else {
						pieces[i] = " "+columnName+"->>"+pieces[i].trim()+" ";
					}
				}
				pieces[i] = pieces[i].replace("/", "->");
				text = StringUtils.replace(text,oldPiece,pieces[i]);
			}
			query += text;
			logger.info("transofrmed query:"+ query);
			return query; 
		} catch(Exception e) {
			logger.error("Error",e);
			logger.error("Error in the research query: research queries must follow the following format: 'jsonNode'/'jsonChildNode'/.../'jsonRequestedNode' = 'requestedValue'");
			return null;
		}
	}
	
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
					ObjectNode itinerariesParent = jsonTreeRootObject.findParent("itineraries");
					itinerariesParent.replace("itineraries",selectedItinerary);
				}
				
			}
			return mapper.writeValueAsString(jsonTreeRootObject);
				
		} catch(RPException rpe) {
			throw rpe;
		} catch(Exception e) {
			logger.error("saveOrUpdatePreferences service: unhandled error: ");
			throw new RPException("ER01");
		}
	}
	
	private String cleanData(String jsonDataRetrieved) {
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
		} catch (IOException e) {
			logger.error("Error",e);
			logger.error(e.getMessage());
			return null;
		}	
	}
}