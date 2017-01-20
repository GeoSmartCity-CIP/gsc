package eu.geosmartcity.hub.velocity;

import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * Esempio di creazione di un rdf usando un template velocity e caricamento tramite le api Jena all'interno del modello
 * relativo nota: ritorna due volte il modello
 */
public class VelocityTemplate {
	private static final Logger LOGGER = Logger.getLogger(VelocityTemplate.class);
	
	// xmlName: nome del file di xml nelle resources associato all'oggetto che si vuolem richiedere
	// params: array di parametri nella forma nomeParametro#valoreDaSostituire
	public final String getRequestXmlVelocity(String xmlName, Hashtable<String, String> params) {
		/* first, get and initialize an engine */
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, this); //configurazione necessaria per l'init
		try {
			ve.init(props);
		}
		catch (Exception e) {
			LOGGER.error("Errore nell'inizializzazione del template");
			LOGGER.error(e);
		}
		/* next, get the Template */
		Template t = null;
		try {
			t = ve.getTemplate(xmlName);
		}
		catch (ResourceNotFoundException e) {
			LOGGER.error("Risorsa non trovata");
			LOGGER.error(e);
		}
		catch (ParseErrorException e) {
			LOGGER.error(e);
		}
		catch (Exception e) {
			LOGGER.error("Errore nella generazione del template");
			LOGGER.error(e);
		}
		
		VelocityContext context = new VelocityContext();
		
		/* parametri */
		Set<String> keys = params.keySet();
		for (String key : keys) {
			context.put(key, params.get(key));
		}
		
		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		try {
			t.merge(context, writer);
		}
		catch (ResourceNotFoundException e) {
			LOGGER.error(e);
		}
		catch (ParseErrorException e) {
			LOGGER.error(e);
		}
		catch (MethodInvocationException e) {
			LOGGER.error(e);
		}
		
		return writer.toString();
	}
	
	public final String getRequestXmlVelocity(String xmlName, Hashtable<String, String> params, String encoding) {
		/* first, get and initialize an engine */
		Properties properties = new Properties();
		properties.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
		properties.setProperty("classpath." + VelocityEngine.RESOURCE_LOADER + ".class",
				ClasspathResourceLoader.class.getName());
		
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, this); //configurazione necessaria per l'init
		try {
			ve.init(properties);
		}
		catch (Exception e) {
			LOGGER.error("Errore nell'inizializzazione del template");
			LOGGER.error(e);
		}
		/* next, get the Template */
		Template t = null;
		try {
			t = ve.getTemplate(xmlName, encoding);
		}
		catch (ResourceNotFoundException e) {
			LOGGER.error("Risorsa non trovata");
			LOGGER.error(e);
		}
		catch (ParseErrorException e) {
			LOGGER.error(e);
		}
		catch (Exception e) {
			LOGGER.error("Errore nella generazione del template");
			LOGGER.error(e);
		}
		
		VelocityContext context = new VelocityContext();
		
		/* parametri */
		Set<String> keys = params.keySet();
		for (String key : keys) {
			context.put(key, params.get(key));
		}
		
		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		try {
			t.merge(context, writer);
		}
		catch (ResourceNotFoundException e) {
			LOGGER.error(e);
		}
		catch (ParseErrorException e) {
			LOGGER.error(e);
		}
		catch (MethodInvocationException e) {
			LOGGER.error(e);
		}
		
		return writer.toString();
	}
	
}
