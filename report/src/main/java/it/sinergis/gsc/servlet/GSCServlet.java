package it.sinergis.gsc.servlet;

import it.sinergis.gsc.common.Constants;
import it.sinergis.gsc.services.BuildingConsumptionService;
import it.sinergis.gsc.services.CreateReportService;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
	
	/** Indirizzo dell'istanza di OpenOffice da usare per la creazione del report GSC. */
	private String _ooHost = null;
	
	/** Porta dell'istanza di OpenOffice da usare per la creazione del report GSC. */
	private String _ooPort = null;
	
	/** Cartella in cui vengono inseriti i file .png contenenti i grafici del plugin GSC. */
	private String _tempFileDir = null;
	
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
		
		_ooHost = getInitParameter("ooHost");
		if (_ooHost == null) {
			logger.error("Parameter <ooHost> doesn't exist. Correct the configuration of the GSCServlet");
			throw new ServletException("Parameter <ooHost> doesn't exist. Correct the configuration of the GSCServlet");
		}
		
		_ooPort = getInitParameter("ooPort");
		if (_ooPort == null) {
			logger.error("Parameter <ooPort> doesn't exist. Correct the configuration of the GSCServlet");
			throw new ServletException("Parameter <ooPort> doesn't exist. Correct the configuration of the GSCServlet");
		}
		
		_tempFileDir = getInitParameter("tempFileDir");
		if (_tempFileDir == null) {
			logger.error("Parameter <tempFileDir> doesn't exist. Correct the configuration of the GSCServlet");
			throw new ServletException(
			        "Parameter <tempFileDir> doesn't exist. Correct the configuration of the GSCServlet");
		}
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
	 * Richiama il servizio per la creazione del report del plugin GSC in formato PDF. In caso di errore viene inviato
	 * un opportuno messaggio.
	 * 
	 * @param request
	 *            la richiesta ricevuta con la post
	 * @param response
	 *            l'oggetto risposta da popolare
	 * @throws Exception
	 */
	private void getReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		CreateReportService createReportService = new CreateReportService(_ooHost, _ooPort, _tempFileDir);
		String filePath = "";
		try {
			String svgAsXML = request.getParameter("xmlSvg");
			logger.debug("SVG: " + svgAsXML);
			
			String jsonClass = request.getParameter("jsonClass");
			logger.debug("JSON energy classes: " + jsonClass);
			
			filePath = createReportService.convertiSvgInPng(svgAsXML);
			
			byte[] pdf = createReportService.createReport(filePath, jsonClass);
			
			response.setContentType("application/x-download");
			response.setHeader("Content-disposition", "attachment; filename=\"ReportBuildings.pdf\"");
			ServletOutputStream serStream = response.getOutputStream();
			serStream.write(pdf);
			serStream.flush();
			serStream.close();
		}
		catch (IOException ioe) {
			logger.error("Catch error in the method getReport", ioe);
			if (!filePath.isEmpty()) {
				createReportService.deleteFilePng(filePath);
			}
		}
		catch (Exception e) {
			logger.error("Catch error in the method getReport", e);
			response.setContentType("text/html");
			response.setHeader("Content-disposition", "");
			response.getWriter().write(
			        buildErrorMessage(createReportService.getPr().getValue(Constants.ERRORE_CREAZIONE_REPORT)));
		}
	}
}