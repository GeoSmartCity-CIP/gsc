package it.sinergis.routingpreferences;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.sinergis.routingpreferences.dao.DAOFactory;
import it.sinergis.routingpreferences.exception.RPException;
import it.sinergis.routingpreferences.jpadao.JpaDAOFactory;

public class ServiceCommons {

	/** Logger. */
	private static Logger logger;
	
	/** DAO factory. */
	DAOFactory daoFactory;
	
	/** Jackson object mapper. */
	ObjectMapper om;
	
	public ServiceCommons() {
		logger = Logger.getLogger(this.getClass());
		daoFactory = new JpaDAOFactory();
		om = new ObjectMapper();
	}
	
	
	/**
	 * Checks if the given text is a well formed json string.
	 * 
	 * @param jsonText
	 * @throws RPException
	 */
	protected void checkJsonWellFormed(String jsonText) throws RPException {
		try {
			om.readTree(jsonText);
		} catch (JsonProcessingException e) {
			logger.error("Error: Json string is not well formed",e);
			throw new RPException("ER07");
		} catch (Exception e) {
			logger.error("Error checking if json is valid",e);
			RPException rpe = new RPException("ER01");
			logger.error("getPreferences service: unhandled error "+rpe.returnErrorString());
			throw rpe;
		}
	}
	
	/**
	 * Creates the actual research query from the semplified input string given by the user.
	 * 
	 * @param text
	 * @return
	 */
	protected String createQuery(String text,String tableName,String columnName,String mode) {
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
						pieces[i] = " "+pieces[i].substring(0,firstBracketIndex+1).trim()+" "+columnName+"->"+pieces[i].substring(firstBracketIndex+1).trim()+" ";
					} else {
						pieces[i] = " "+columnName+"->"+pieces[i].trim()+" ";
					}
				} else {
					if(firstBracketIndex != -1) {
						pieces[i] = " "+pieces[i].substring(0,firstBracketIndex+1).trim()+" "+columnName+"->>"+pieces[i].substring(firstBracketIndex+1).trim()+" ";
					} else {
						pieces[i] = " "+columnName+"->>"+pieces[i].trim()+" ";
					}
				}
				pieces[i] = pieces[i].replace("/", "->");
				text = StringUtils.replace(text,oldPiece,pieces[i]);
			}
			query += text;
			logger.info("transformed query:"+ query);
			return query; 
		} catch(Exception e) {
			logger.error("Error",e);
			logger.error("Error in the research query: research queries must follow the following format: 'jsonNode'/'jsonChildNode'/.../'jsonRequestedNode' = 'requestedValue'");
			return null;
		}
	}
	
	/**
	 * 
	 * Given a map builds a json string where all the entries stay on the same json level.
	 * 
	 * @param result
	 * @return
	 */
	protected String jsonifyResults(HashMap<String,String> result) {
		StringBuilder sb = new StringBuilder();
		int count = 1;
		sb.append("{");
		for (Map.Entry<String, String> entry : result.entrySet())
		{
			sb.append("\""+entry.getKey()+"\":\""+entry.getValue()+"\"");
			if(result.size() != count) {
				sb.append(",");
			}
			count ++;
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Given a name and a value builds a json string where all the entries stay on the same json level.
	 * 
	 * @param jsonResultName
	 * @param jsonResultValue
	 * @return
	 */
	protected String jsonifyResult(String jsonResultName,String jsonResultValue) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\""+jsonResultName+"\":\""+jsonResultValue+"\"");
		sb.append("}");
		return sb.toString();
	}
}
