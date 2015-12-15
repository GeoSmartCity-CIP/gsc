package it.sinergis.routingpreferences;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.sinergis.routingpreferences.dao.DAOFactory;
import it.sinergis.routingpreferences.dao.RoutingPreferencesDAO;
import it.sinergis.routingpreferences.exception.RPException;
import it.sinergis.routingpreferences.jpadao.JpaDAOFactory;
import it.sinergis.routingpreferences.model.RoutingPreferences;

/***
 * Class to handle RoutingPreference requests.
 * 
 * @author Lorenzo Longhitano
 * 
 */
public class RoutingPreferencesService {

	/** Logger. */
	private static Logger logger;
	
	/** DAO factory. */
	DAOFactory daoFactory;
	
	/** RoutingPreferences DAO. */
	RoutingPreferencesDAO routingPreferencesDAO;
	
	/** Jackson object mapper. */
	ObjectMapper om;

	
	public RoutingPreferencesService() {
		logger = Logger.getLogger(this.getClass());
		daoFactory = new JpaDAOFactory();
		routingPreferencesDAO = daoFactory.getRoutingPreferencesDAO();
		om = new ObjectMapper();
	}
	
	public String saveOrUpdatePreferences(String jsonRoutingPreferenceText) {

		try {
			//check if there's another preference already saved for the same userId
			RoutingPreferences routingPreferences = getPreferencesObject(jsonRoutingPreferenceText);
				
			//if no results found -> add new record
			if(routingPreferences == null) {
				routingPreferencesDAO.insertRoutingPreferences(jsonRoutingPreferenceText);
				return getUserIdFromJsonText(jsonRoutingPreferenceText);
			//otherwise update current record
			} else {
				String queryText = "'userId' = '"+getUserIdFromJsonText(jsonRoutingPreferenceText)+"'";
				routingPreferencesDAO.updateRoutingPreferences(jsonRoutingPreferenceText,createQuery(queryText, "rp_t_preferences2", "data","delete"));
				
				//return id
				return getUserIdFromJsonText(jsonRoutingPreferenceText);
			}
		} catch(RPException rpe) {
			return rpe.returnErrorString();
		} catch(Exception e) {
			RPException rpe = new RPException("ER01");
			logger.error("saveOrUpdatePreferences service: unhandled error "+rpe.returnErrorString());
			return rpe.returnErrorString();
		}
	}
	
	public String getPreferences(String jsonRoutingPreferenceText) {
		try {
			RoutingPreferences routingPreferences = getPreferencesObject(jsonRoutingPreferenceText);
			
			if(routingPreferences == null) {
				logger.error("getPreferences: no records found.");
				throw new RPException("ER05");
			}
			return routingPreferences.getData();
		} catch(RPException rpe) {
			return rpe.returnErrorString();
		} catch(Exception e) {
			RPException rpe = new RPException("ER01");
			logger.error("getPreferences service: unhandled error "+rpe.returnErrorString());
			return rpe.returnErrorString();
		}
	}
	
	public String deletePreferences(String jsonRoutingPreferenceText) {
		try {
			String queryText = "'userId' = '"+getUserIdFromJsonText(jsonRoutingPreferenceText)+"'";
			String query = createQuery(queryText, "rp_t_preferences2", "data","delete");
			routingPreferencesDAO.deleteRoutingPreferences(query);
			return getUserIdFromJsonText(jsonRoutingPreferenceText);
		} catch(RPException rpe) {
			return rpe.returnErrorString();
		} catch(Exception e) {
			RPException rpe = new RPException("ER01");
			logger.error("deletePreferences service: unhandled error "+rpe.returnErrorString());
			return rpe.returnErrorString();
		}
	}
	
	private RoutingPreferences getPreferencesObject(String jsonRoutingPreferenceText) throws RPException {
		
		try {
			String queryText = "'userId' = '"+getUserIdFromJsonText(jsonRoutingPreferenceText)+"'";
			String query = createQuery(queryText, "rp_t_preferences2", "data","select");
			List<RoutingPreferences> routingPreferences = routingPreferencesDAO.getRoutingPreferences(query);
			
			if(routingPreferences.isEmpty()) {
				return null;
			}
			//research query can only find 1 record at most
			return routingPreferences.get(0);
		} catch(RPException rpe) {
			throw rpe;
		} catch(Exception e) {
			logger.error("saveOrUpdatePreferences service: unhandled error: ");
			throw new RPException("ER01");
		}
	}
	
	private String getUserIdFromJsonText(String jsonRoutingPreferenceText) throws JsonParseException, JsonMappingException, IOException {
		//PreferenceObject preferenceObject = om.readValue(jsonRoutingPreferenceText, PreferenceObject.class);
		//return preferenceObject.getUserId();
		JsonNode rootNode = om.readTree(jsonRoutingPreferenceText);
		return rootNode.findValue("userId").toString();
	}
	
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
}