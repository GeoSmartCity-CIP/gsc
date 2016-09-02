package it.sinergis.gsc.servlet;

import it.sinergis.gsc.common.Constants;
import it.sinergis.gsc.services.BuildingConsumptionService;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet per il plugin GSC.
 * 
 * @author Giuseppe Giuffrida
 */
public class GSCServlet extends HttpServlet {
	
	/**
	 * Logger.
	 */
	private static Logger logger = null;
	
	/** Serial version. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore di default.
	 */
	public GSCServlet() {
		
		logger = Logger.getLogger(this.getClass());
	}
	
	/**
	 * Ogni richiesta ricevuta con una get viene rigirata al metodo doPost di questa stessa classe.
	 * 
	 * @param req
	 *            la richiesta ricevuta con la get
	 * @param res
	 *            l'oggetto risposta da popolare
	 * @throws ServletException
	 *             per tutte le eccezioni che non si e' in grado di gestire (l'eccezione originale viene wrappata)
	 * @throws IOException
	 *             in caso di errore di I/O
	 * @see #doPost()
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		doPost(req, res);
	}
	
	/**
	 * Gestore di tutte le richieste pervenute alla servlet (sia in POST che in GET).
	 * 
	 * @param request
	 *            la richiesta ricevuta con la post
	 * @param response
	 *            l'oggetto risposta da popolare
	 * @throws ServletException
	 *             per tutte le eccezioni che non si e' in grado di gestire (l'eccezione originale viene wrappata)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		try {
			request.setCharacterEncoding("UTF-8");
			String action = request.getParameter("actionName");
			
			if (action != null && !action.equals("")) {
				
				getDati(request, response);
			}
		}
		catch (java.lang.Exception e) {
			throw new ServletException(e);
		}
	}
	
	/**
	 * Inizializzazioni varie.
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
	}
	
	/**
	 * Costruisce il json contenente il messaggio di errore passato come parametro.
	 * 
	 * @param msg
	 *            messaggio di errore
	 * @return json contenente il messaggio di errore
	 */
	private String buildErrorMessage(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"");
		sb.append(Constants.JSON_SUCCESS_FIELD);
		sb.append("\": false, \"");
		sb.append(Constants.JSON_MSG_FIELD);
		sb.append("\": \"");
		sb.append(msg);
		sb.append("\"}");
		return sb.toString();
	}
	
	/**
	 * Se actionName = getConsumptions: richiama il servizio per il recupero dei consumi energetici e di altre
	 * informazioni, come classe energetica e volume, degli edifici i cui id vengono passati all'interno della richiesta
	 * inviata alla servlet. Se actionName = getEnergyCertificates: richiama il servizio per il recupero dei certificati
	 * energetici di un edificio il cui id viene passato all'interno della richiesta inviata alla servlet. Se actionName
	 * = getConsumptionsNormalizedSingleBuilding: richiama il servizio per il recupero dei consumi energetici
	 * dell'edificio il cui id viene passato all'interno della richiesta inviata alla servlet. Se actionName =
	 * getConsumptionsNormalizedMultiBuildings: richiama il servizio per il recupero dei consumi energetici degli
	 * edifici i cui id vengono passati all'interno della richiesta inviata alla servlet. Se actionName =
	 * getEnergyClassMultiBuildings: richiama il servizio per il recupero delle classi energetiche degli edifici i cui
	 * id vengono passati all'interno della richiesta inviata alla servlet. Se actionName =
	 * getConsumptionsNormalizedSingleInstallation: richiama il servizio per il recupero dei consumi energetici
	 * dell'impianto il cui id viene passato all'interno della richiesta inviata alla servlet. Se actionName =
	 * getConsumptionsNormalizedMultiInstallations: richiama il servizio per il recupero dei consumi energetici degli
	 * impianti i cui id vengono passati all'interno della richiesta inviata alla servlet.
	 * 
	 * @param request
	 *            la richiesta ricevuta con la post
	 * @param response
	 *            l'oggetto risposta da popolare
	 * @throws Exception
	 */
	private void getDati(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String action = request.getParameter("actionName");
		
		BuildingConsumptionService bcs = new BuildingConsumptionService();
		
		try {
			StringBuilder stringBuilder = new StringBuilder();
			
			BufferedReader reader = request.getReader();
			char[] charBuffer = new char[128];
			int bytesRead = -1;
			while ((bytesRead = reader.read(charBuffer)) > 0) {
				stringBuilder.append(charBuffer, 0, bytesRead);
			}
			
			String jsonParameter = stringBuilder.toString();
			logger.debug(jsonParameter);
			
			String json = "";
			
			if (action.equalsIgnoreCase("getConsumptions")) {
				json = bcs.recuperaJSONConsumiPerTipoDiConsumo(jsonParameter);
			}
			else if (action.equalsIgnoreCase("getEnergyCertificates")) {
				json = bcs.recuperaJSONCertificatiEdificio(jsonParameter);
			}
			else if (action.equalsIgnoreCase("getConsumptionsNormalizedSingleBuilding")) {
				json = bcs.recuperaJSONConsumiBuildingsPerDashboard(jsonParameter, true);
			}
			else if (action.equalsIgnoreCase("getConsumptionsNormalizedMultiBuildings")) {
				json = bcs.recuperaJSONConsumiBuildingsPerDashboard(jsonParameter, false);
			}
			else if (action.equalsIgnoreCase("getEnergyClassMultiBuildings")) {
				json = bcs.recuperaJSONClassiEdifici(jsonParameter);
			}
			else if (action.equalsIgnoreCase("getConsumptionsNormalizedSingleInstallation")) {
				json = bcs.recuperaJSONConsumiInstallationPerDashboard(jsonParameter, true);
			}
			else if (action.equalsIgnoreCase("getConsumptionsNormalizedMultiInstallations")) {
				json = bcs.recuperaJSONConsumiInstallationPerDashboard(jsonParameter, false);
			}
			
			logger.debug(json);
			response.setContentType("text/html");
			response.getWriter().write(json);
		}
		catch (Exception e) {
			logger.error("Error sending JSON", e);
			response.setContentType("text/html");
			
			if (action.equalsIgnoreCase("getEnergyCertificates")) {
				response.getWriter().write(
				        buildErrorMessage(bcs.getPr().getValue(Constants.ERRORE_RECUPERO_CERTIFICATI)));
			}
			else if (action.equalsIgnoreCase("getEnergyClassMultiBuildings")) {
				response.getWriter().write(buildErrorMessage(bcs.getPr().getValue(Constants.ERRORE_RECUPERO_CLASSI)));
			}
			else {
				response.getWriter().write(buildErrorMessage(bcs.getPr().getValue(Constants.ERRORE_RECUPERO_CONSUMI)));
			}
		}
	}
}