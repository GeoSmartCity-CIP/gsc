package it.sinergis.routingpreferences.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import it.sinergis.routingpreferences.ItineraryPreferencesService;
import it.sinergis.routingpreferences.RoutingPreferencesService;
import it.sinergis.routingpreferences.exception.RPException;

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
		
		if(servizio.equalsIgnoreCase("saveItinerary")) {
			logger.info(testo);
			try {
				ItineraryPreferencesService itineraryPreferencesService = new ItineraryPreferencesService();
				String responseJSON = itineraryPreferencesService.saveItinerary(testo,userId,itineraryId,notes);
				
				request.setAttribute("responseJSON", responseJSON);
			} catch(Exception e) {
				logger.error("Generic error while retrieving routing preferences",e);
				RPException rpe = new RPException("ER01");
				request.setAttribute("responseJSON", rpe.returnErrorString());
			}
			
		} else if(servizio.equalsIgnoreCase("getItinerary")) {
			logger.info(testo);
			try {
				ItineraryPreferencesService itineraryPreferencesService = new ItineraryPreferencesService();
				String responseJSON = itineraryPreferencesService.getItineraries(testo);
				
				request.setAttribute("responseJSON", responseJSON);
			} catch(Exception e) {
				logger.error("Generic error while retrieving routing preferences",e);
				RPException rpe = new RPException("ER01");
				request.setAttribute("responseJSON", rpe.returnErrorString());
			}
		} else if(servizio.equalsIgnoreCase("deleteItinerary")) {
			logger.info(testo);
			try {
				ItineraryPreferencesService itineraryPreferencesService = new ItineraryPreferencesService();
				String responseJSON = itineraryPreferencesService.deleteItinerary(Long.valueOf(itineraryId));
				
				request.setAttribute("responseJSON", responseJSON);
			} catch(Exception e) {
				logger.error("Generic error while retrieving routing preferences",e);
				RPException rpe = new RPException("ER01");
				request.setAttribute("responseJSON", rpe.returnErrorString());
			}
		}else if(servizio.equalsIgnoreCase("saveOrUpdatePreferences")) {
			logger.info(testo);
			try {
				RoutingPreferencesService routingPreferencesService = new RoutingPreferencesService();
				String responseJSON = routingPreferencesService.saveOrUpdatePreferences(testo);
				
				request.setAttribute("responseJSON", responseJSON);
			} catch(Exception e) {
				logger.error("Generic error while retrieving routing preferences",e);
				RPException rpe = new RPException("ER01");
				request.setAttribute("responseJSON", rpe.returnErrorString());
			}
		//search by userId
		} else if(servizio.equalsIgnoreCase("getPreferences")) {
			logger.info(testo);
			try {
				
				RoutingPreferencesService routingPreferencesService = new RoutingPreferencesService();
				String responseJSON = routingPreferencesService.getPreferences(testo);

				request.setAttribute("responseJSON", responseJSON);
			} catch(Exception e) {
				logger.error("Generic error while retrieving routing preferences",e);
				RPException rpe = new RPException("ER01");
				request.setAttribute("responseJSON", rpe.returnErrorString());
			}
		//delete by userId
		} else if(servizio.equalsIgnoreCase("deletePreferences")) {
			logger.info(testo);
			try {
				RoutingPreferencesService routingPreferencesService = new RoutingPreferencesService();
				String responseJSON = routingPreferencesService.deletePreferences(testo);
				
				request.setAttribute("responseJSON", responseJSON);
			} catch(Exception e) {
				logger.error("Generic error while retrieving routing preferences",e);
				RPException rpe = new RPException("ER01");
				request.setAttribute("responseJSON", rpe.returnErrorString());
			}
		}
		
		request.setAttribute("servizio", servizio);
		request.setAttribute("text", testo);
		request.getRequestDispatcher("testService.jsp")
				.forward(request, response);
	}
}
