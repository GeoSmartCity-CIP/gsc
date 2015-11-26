package it.sinergis.routingpreferences.servlet;

import it.sinergis.routingpreferences.common.PreferenceObject;
import it.sinergis.routingpreferences.common.PropertyReader;
import it.sinergis.routingpreferences.dao.DAOFactory;
import it.sinergis.routingpreferences.jpadao.JpaDAOFactory;
import it.sinergis.routingpreferences.model.ItinerariesPreferences;
import it.sinergis.routingpreferences.model.RoutingPreferences;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SaveJSONServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger;
	
	public SaveJSONServlet() {}
	
	public void init(ServletConfig config) throws ServletException {
		// Inizializzazione
		logger = Logger.getLogger(this.getClass());
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);

	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String servizio = request.getParameter("servizio");
		String testo = request.getParameter("text").replaceAll("\\p{Cntrl}", "");
		String userId = request.getParameter("userId");
		String itineraryId = request.getParameter("itineraryId");
		String notes = request.getParameter("notes");
		String maxWalkingDistance = request.getParameter("maxWalkingDistance");
		String walkingSpeed = request.getParameter("walkingSpeed");
		String maxBikingDistance = request.getParameter("maxBikingDistance");
		String bikingSpeed = request.getParameter("bikingSpeed");
		
		if(servizio.equalsIgnoreCase("save")) {
			logger.info(testo);
			//Salvataggio su db			
			DAOFactory daoFactory = new JpaDAOFactory();
			try {
				String modifiedJSON = adjustJSON(testo,userId,itineraryId,notes);
				if(modifiedJSON != null) {
					daoFactory.getItinerariesPreferencesDAO().saveItinerary(modifiedJSON);
					request.setAttribute("risposta",modifiedJSON);
				} else {
					logger.error("It was not possible to save JSON data. Check if the following parameters have been specified:'userId', 'note', 'itineraryId')");
					PropertyReader pr = new PropertyReader("error_messages.properties");			
					throw new Exception(pr.getValue("ER04"));
				}
				
			} catch (Exception e) {
				logger.error(e.getMessage());
				request.setAttribute("risposta", e.getMessage());
			}
		} else if(servizio.equalsIgnoreCase("read")) {
			logger.info(testo);
			DAOFactory daoFactory = new JpaDAOFactory();
			try {
				String query = createQuery(testo,"rp_t_itineraries","data");
				if(query == null) {
					logger.error("Error in the research query: research queries must follow the following format: 'jsonNode'/'jsonChildNode'/.../'jsonRequestedNode' = 'requestedValue'");
					PropertyReader pr = new PropertyReader("error_messages.properties");			
					throw new Exception(pr.getValue("ER03"));
				}
				List<ItinerariesPreferences> ipList = daoFactory.getItinerariesPreferencesDAO().readItinerary(query);

				StringBuilder sb = new StringBuilder();
				sb.append("json:[");
				
				for(int i = 0; i< ipList.size(); i++) {
					if(i != 0) {
						sb.append(",");
					}
					//sb.append("{");
					sb.append(cleanData(ipList.get(i).getData()));
					//sb.append("}");
				}
				
				sb.append("]");
					
				request.setAttribute("risposta", sb.toString());
			} catch (Exception e) {
				logger.error(e.getMessage());
				request.setAttribute("risposta", e.getMessage());
			}
		} else if(servizio.equalsIgnoreCase("savePreferences")) {
			logger.info(testo);
			//Salvataggio su db			
			DAOFactory daoFactory = new JpaDAOFactory();
			try {
				ObjectMapper om = new ObjectMapper();
				PreferenceObject preferenceObject = new PreferenceObject();
				if(!StringUtils.isEmpty(userId)) {
					preferenceObject.setUserId(userId);
				} else {
					PropertyReader pr = new PropertyReader("error_messages.properties");			
					throw new Exception(pr.getValue("ER04"));
				}
				if(!StringUtils.isEmpty(userId)) {
					preferenceObject.setMaxWalkingDistance(maxWalkingDistance);
				}
				if(!StringUtils.isEmpty(userId)) {
					preferenceObject.setWalkingSpeed(walkingSpeed);
				}
				if(!StringUtils.isEmpty(userId)) {
					preferenceObject.setMaxBikingDistance(maxBikingDistance);
				}
				if(!StringUtils.isEmpty(userId)) {
					preferenceObject.setBikingSpeed(bikingSpeed);
				}
				String jsonText = om.writeValueAsString(preferenceObject);
				logger.info("JSON preference saved:"+jsonText);
				
				daoFactory.getRoutingPreferencesDAO().insertRoutingPreferences(jsonText);
				
				request.setAttribute("risposta", "Preference succesfully saved .");
			} catch (Exception e) {
				logger.error(e.getMessage());
				request.setAttribute("risposta", e.getMessage());
			}
		//search by userId
		} else if(servizio.equalsIgnoreCase("readPreferences")) {
			logger.info(testo);
			DAOFactory daoFactory = new JpaDAOFactory();
			try {
				String query = "select * from rp_t_preferences2 where data->>'userId' = '"+userId+"'";
				List<RoutingPreferences> ipList = daoFactory.getRoutingPreferencesDAO().searchRoutingPreferences(query);
				
				StringBuilder sb = new StringBuilder();
				sb.append("json:[");
				
				for(int i = 0; i< ipList.size(); i++) {
					if(i != 0) {
						sb.append(",");
					}
					sb.append("{");
					sb.append(ipList.get(i).getData());
					sb.append("}");
				}
				
				sb.append("]");
				
				request.setAttribute("risposta", sb.toString());
			} catch (Exception e) {
				logger.error(e.getMessage());
				request.setAttribute("risposta", e.getMessage());
			}
		//delete by userId
		} else if(servizio.equalsIgnoreCase("deletePreferences")) {
			logger.info(testo);
			DAOFactory daoFactory = new JpaDAOFactory();
			try {
				String query = "delete from rp_t_preferences2 where data->>'userId' = '"+userId+"'";
				daoFactory.getRoutingPreferencesDAO().deleteRoutingPreference(query);
				
				request.setAttribute("risposta", "Record succesfully deleted.");
			} catch (Exception e) {
				logger.error(e.getMessage());
				request.setAttribute("risposta",e.getMessage());
			}
		}
		
		request.setAttribute("servizio", servizio);
		request.setAttribute("text", testo);
		request.getRequestDispatcher("testService.jsp")
				.forward(request, response);
	}

	/**
	 * Creates the actual research query from the semplified input string given by the user.
	 * 
	 * @param testo
	 * @return
	 */
	private String createQuery(String testo,String tableName,String columnName) {
		String query = "select * from "+tableName+" where ";
		
		try {
			String[] pieces = testo.split("AND|OR");
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
				testo = StringUtils.replace(testo,oldPiece,pieces[i]);
			}
			query += testo;
			logger.info("transofrmed query:"+ query);
			return query; 
		} catch(Exception e) {
			logger.error("Error in the research query: research queries must follow the following format: 'jsonNode'/'jsonChildNode'/.../'jsonRequestedNode' = 'requestedValue'");
			return null;
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
			logger.error(e.getMessage());
			return null;
		}	
	}

	private String adjustJSON(String testo, String userId, String itineraryId,
			String notes) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode jsonTreeRootObject = null;
		try {
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
			
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		}	
	}
}
