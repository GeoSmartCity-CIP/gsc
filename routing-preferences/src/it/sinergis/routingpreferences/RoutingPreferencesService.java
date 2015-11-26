//package it.sinergis.routingpreferences;
//
//import it.sinergis.routingpreferences.common.Constants;
//import it.sinergis.routingpreferences.jpadao.JpaRoutingPreferencesDAO;
//import it.sinergis.routingpreferences.model.RoutingPreferences;
//import it.sinergis.routingpreferences.schema.ErrorType;
//import it.sinergis.routingpreferences.schema.ObjectFactory;
//import it.sinergis.routingpreferences.schema.RoutingPreferenceType;
//import it.sinergis.routingpreferences.schema.RoutingPreferencesDeleteRequest;
//import it.sinergis.routingpreferences.schema.RoutingPreferencesDeleteResponseType;
//import it.sinergis.routingpreferences.schema.RoutingPreferencesInsertRequest;
//import it.sinergis.routingpreferences.schema.RoutingPreferencesInsertResponseType;
//import it.sinergis.routingpreferences.schema.RoutingPreferencesSearchRequest;
//import it.sinergis.routingpreferences.schema.RoutingPreferencesSearchResponseType;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.jws.WebMethod;
//import javax.jws.WebParam;
//import javax.jws.WebService;
//import javax.persistence.NoResultException;
//import javax.xml.XMLConstants;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//import javax.xml.transform.stream.StreamSource;
//import javax.xml.validation.Schema;
//import javax.xml.validation.SchemaFactory;
//
//import org.apache.log4j.Logger;
//import org.xml.sax.SAXException;
//import org.xml.sax.helpers.DefaultHandler;
//
///***
// * WebService per la gestione delle preferenze di routing
// * 
// * @author Andrea Di Nora
// * 
// */
//@WebService(name = "routingPreferencesService", targetNamespace = "http://routingPreferencesService.sinergis.it/")
//public class RoutingPreferencesService {
//
//	/** Logger. */
//	private static Logger logger;
//	
//	/** Factory JAXB Routing Preferences Service. */
//	private ObjectFactory routingPreferencesObjFactory;
//	
//	
//	/**
//	 * Costruttore di default
//	 */
//	public RoutingPreferencesService() {
//		logger = Logger.getLogger(this.getClass());
//		this.routingPreferencesObjFactory = new ObjectFactory();			
//	}
//	
//	
//	/**
//	 * Metodo che inserisce una preferenza di routing nella banca dati
//	 * @param request dati sulla preferenza da inserire, in formato xml
//	 * @return xml di risposta: vuoto se l'inserimento e' andato a buon fine oppure contiene l'eventuale messaggio di errore
//	 */
//	@WebMethod(operationName = "routingPreferencesInsert")
//	public RoutingPreferencesInsertResponseType routingPreferencesInsert(@WebParam(name = "request", targetNamespace = "") RoutingPreferencesInsertRequest request) {
//		
//		logger.debug("Service request: " + request);
//				
//		RoutingPreferencesInsertResponseType response = routingPreferencesObjFactory.createRoutingPreferencesInsertResponseType();		
//		if(!validateInsertRequest(request)) {
//			ErrorType err = routingPreferencesObjFactory.createErrorType();
//			err.setCode(Constants.RP002_ERR_CODE);
//			err.setDescription(Constants.RP002_ERR_MSG);
//			response.setError(err);
//			return response;
//		}
//			         
//        try {
//        	JpaRoutingPreferencesDAO jpaRoutingPreferencesDAO = new JpaRoutingPreferencesDAO();        	
//        	jpaRoutingPreferencesDAO.insertRoutingPreferences(request);
//			
//		}
//        catch(IOException e) {
//        	ErrorType err = routingPreferencesObjFactory.createErrorType();
//			err.setCode(Constants.RP006_ERR_CODE);
//			err.setDescription(Constants.RP006_ERR_MSG);
//			response.setError(err);
//			return response;
//        }
//        catch (Exception e) {
//        	ErrorType err = routingPreferencesObjFactory.createErrorType();
//			err.setCode(Constants.RP001_ERR_CODE);
//			err.setDescription(Constants.RP001_ERR_MSG);
//			response.setError(err);
//			return response;
//
//		}
//               
//		return response;
//	}
//	
//	/**
//	 * Metodo che ricerca le preferenze di routing in input
//	 * @param request contenuto dei dati di ricerca, in formato xml
//	 * @return lista delle preferenze di routing (eventualmente vuota) oppure un messaggio di errore se il servizio fallisce
//	 */
//	@WebMethod(operationName = "routingPreferencesSearch")
//	public RoutingPreferencesSearchResponseType routingPreferencesSearch(@WebParam(name = "request", targetNamespace = "") RoutingPreferencesSearchRequest request) {
//		logger.debug("Service request: " + request);
//		
//		RoutingPreferencesSearchResponseType response = routingPreferencesObjFactory.createRoutingPreferencesSearchResponseType();
//		
//		if(!validateSearchRequest(request)) {
//			ErrorType err = routingPreferencesObjFactory.createErrorType();
//			err.setCode(Constants.RP002_ERR_CODE);
//			err.setDescription(Constants.RP002_ERR_MSG);
//			response.setError(err);
//			return response;
//		}
//			 	
//        try {
//        	JpaRoutingPreferencesDAO jpaRoutingPreferencesDAO = new JpaRoutingPreferencesDAO();
//        	
//        	List<RoutingPreferences> results = jpaRoutingPreferencesDAO.searchRoutingPreferences(request.getUserID());        	
//        	
//        	response.getRoutingPreference().addAll(popolaSearchResponse(results));
//        	
//        	return response;
//			
//		} catch (Exception e) {
//			ErrorType err = routingPreferencesObjFactory.createErrorType();
//			err.setCode(Constants.RP003_ERR_CODE);
//			err.setDescription(Constants.RP003_ERR_MSG);
//			response.setError(err);
//			return response;
//		}
//	}
//
//	/**
//	 * Metodo che elimina dalla banca dati una preferenza di routing non piu' necessaria
//	 * @param request dati sulla preferenza di routing da eliminare, in formato xml
//	 * @return risposta vuota se il servizio e' andato a buon fine, oppure un eventuale messaggio di errore
//	 */
//	@WebMethod(operationName = "routingPreferencesDelete")
//	public RoutingPreferencesDeleteResponseType routingPreferencesDelete(@WebParam(name = "request", targetNamespace = "") RoutingPreferencesDeleteRequest request) {
//		
//		logger.debug("Service request: " + request);
//		
//		RoutingPreferencesDeleteResponseType response = routingPreferencesObjFactory.createRoutingPreferencesDeleteResponseType();
//		
//		if(!validateDeleteRequest(request)) {
//			ErrorType err = routingPreferencesObjFactory.createErrorType();
//			err.setCode(Constants.RP002_ERR_CODE);
//			err.setDescription(Constants.RP002_ERR_MSG);
//			response.setError(err);
//			return response;
//		}
//			 
//        JpaRoutingPreferencesDAO jpaRoutingPreferencesDAO = new JpaRoutingPreferencesDAO();
//        try {
//        	jpaRoutingPreferencesDAO.deleteRoutingPreference(request);
//		}
//        catch (NoResultException e) {
//			ErrorType err = routingPreferencesObjFactory.createErrorType();
//			err.setCode(Constants.RP005_ERR_CODE);
//			err.setDescription(Constants.RP005_ERR_MSG);
//			response.setError(err);
//			return response;
//
//        }
//        catch (Exception e) {			
//			ErrorType err = routingPreferencesObjFactory.createErrorType();
//			err.setCode(Constants.RP004_ERR_CODE);
//			err.setDescription(Constants.RP004_ERR_MSG);
//			response.setError(err);
//			return response;
//		}
//               
//		return response;
//	}
//	
//	
//	/**
//	 * Metodo che popola la risposta del metodo di ricerca delle preferenze di routing
//	 * @param routingPreferences elenco delle preferenze di routing
//	 * @return lista delle preferenze di routing
//	 */
//	private List<RoutingPreferenceType> popolaSearchResponse(List<RoutingPreferences> routingPreferences) {
//		
//		List<RoutingPreferenceType> result = new ArrayList<RoutingPreferenceType>();
//		
//		for(RoutingPreferences routingPreference : routingPreferences){
//			RoutingPreferenceType routingPreferenceType = routingPreferencesObjFactory.createRoutingPreferenceType();
//			
//			routingPreferenceType.setUserID(routingPreference.getUserId());
//			routingPreferenceType.setBikingSpeed(routingPreference.getBikingSpeed());
//			routingPreferenceType.setMaxBikeDistance(routingPreference.getMaxBikeDistance());
//			routingPreferenceType.setMaxWalkDistance(routingPreference.getMaxWalkDistance());
//			routingPreferenceType.setWalkingSpeed(routingPreference.getWalkingSpeed());
//			
//			result.add(routingPreferenceType);
//		}
//		
//		return result;
//	}
//	
//	/**
//	 * Metodo che valida la richiesta di inserimeto routing preferences
//	 * @param request richiesta di inserimento routing preferences
//	 * @return true se la richiesta e' valida, false altrimenti
//	 */
//	private boolean validateInsertRequest(RoutingPreferencesInsertRequest request) {
//		
//		//Carichiamo l'xsd		
//		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//		
//		try {
//			Schema schema = schemaFactory.newSchema(new StreamSource(getClass().getResourceAsStream("/routingPreferences.xsd")));
//			JAXBContext jaxbContext = JAXBContext.newInstance("it.sinergis.routingpreferences.schema");
//			
//			Marshaller marshaller = jaxbContext.createMarshaller();
//			marshaller.setSchema(schema);
//			marshaller.marshal(request, new DefaultHandler());
//			return true;
//		} catch (SAXException e) {
//			logger.error("Schema validation service error.",e);
//			
//			return false;
//		} 
//		catch ( JAXBException e) {
//			logger.error("Schema validation service error.",e);
//			
//			return false;
//		}
//			
//	}
//	
//	/**
//	 * Metodo che valida la richiesta di ricerca preferenze routing
//	 * @param request richiesta di ricerca preferenza routing
//	 * @return true se la richiesta e' valida, false altrimenti
//	 */
//	private boolean validateSearchRequest(RoutingPreferencesSearchRequest request) {
//		
//		//Carichiamo l'xsd		
//		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//		
//		try {
//			Schema schema = schemaFactory.newSchema(new StreamSource(getClass().getResourceAsStream("/routingPreferences.xsd")));
//			JAXBContext jaxbContext = JAXBContext.newInstance("it.sinergis.routingpreferences.schema");
//			
//			Marshaller marshaller = jaxbContext.createMarshaller();
//			marshaller.setSchema(schema);
//			marshaller.marshal(request, new DefaultHandler());
//			return true;
//		} catch (SAXException e) {
//			logger.error("Schema validation service error.",e);
//			return false;
//		} 
//		catch ( JAXBException e) {
//			logger.error("Schema validation service error.",e);
//			return false;
//		}
//			
//	}
//	
//	/**
//	 * Metodo che valida la richiesta di cancellazione preferenza routing
//	 * @param request richiesta di cancellazione preferenza routing
//	 * @return true se la richiesta e' valida, false altrimenti
//	 */
//	private boolean validateDeleteRequest(RoutingPreferencesDeleteRequest request) {
//		
//		//Carichiamo l'xsd		
//		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//		
//		try {
//			Schema schema = schemaFactory.newSchema(new StreamSource(getClass().getResourceAsStream("/routingPreferences.xsd")));
//			JAXBContext jaxbContext = JAXBContext.newInstance("it.sinergis.routingpreferences.schema");
//			
//			Marshaller marshaller = jaxbContext.createMarshaller();
//			marshaller.setSchema(schema);
//			marshaller.marshal(request, new DefaultHandler());
//			return true;
//		} catch (SAXException e) {
//			logger.error("Schema validation service error.",e);
//			return false;
//		} 
//		catch ( JAXBException e) {
//			logger.error("Schema validation service error.",e);
//			return false;
//		}
//			
//	}
//}