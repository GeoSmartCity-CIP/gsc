package it.sinergis.rdf.controller;

import it.sinergis.utils.ProjectUtils;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RDFGeneratorRestController {
	private static final Logger logger = Logger.getLogger(RDFGeneratorRestController.class);
	
	@RequestMapping(value = "/rdf/{elements}", method = RequestMethod.GET, produces={"application/xml"})
	public ResponseEntity<String> getRDFWithLimit(@PathVariable("elements") Integer elements){
		logger.info("ricevuto filtro count: " + elements);
		if (elements == 0)
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		ClassLoader classLoader = this.getClass().getClassLoader();
		String wfs = ProjectUtils.getProperty(classLoader, "wfs") + "&maxFeatures=" + elements;
				
		String rdfString = ProjectUtils.generateRDF(classLoader, wfs); 
		if (rdfString != null){
			return new ResponseEntity<String>(rdfString, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/rdf/{elements}/bbox/{bbox}", method = RequestMethod.GET, produces={"application/xml"})
	public ResponseEntity<String> getRDFWithLimitAndBBox(@PathVariable("elements") Integer elements, @PathVariable("bbox") String bbox){
		logger.info("ricevuto filtro count: " + elements);
		if (elements == 0)
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		ClassLoader classLoader = this.getClass().getClassLoader();
		String wfs = ProjectUtils.getProperty(classLoader, "wfs") + "&maxFeatures=" + elements +	"&bbox=" + bbox;
		
		String rdfString = ProjectUtils.generateRDF(classLoader, wfs); 
		if (rdfString != null){
			return new ResponseEntity<String>(rdfString, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/rdf", method = RequestMethod.GET, produces={"application/xml"})
	public ResponseEntity<String> getRDF(){
		ClassLoader classLoader = this.getClass().getClassLoader();
		String wfs = ProjectUtils.getProperty(classLoader, "wfs");
				
		String rdfString = ProjectUtils.generateRDF(classLoader, wfs); 
		if (rdfString != null){
			return new ResponseEntity<String>(rdfString, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/rdf/bbox/{bbox}", method = RequestMethod.GET, produces={"application/xml"})
	public ResponseEntity<String> getRDFByBBox(@PathVariable("bbox") String bbox){
		ClassLoader classLoader = this.getClass().getClassLoader();
		String wfs = ProjectUtils.getProperty(classLoader, "wfs") + "&bbox=" + bbox;
				
		String rdfString = ProjectUtils.generateRDF(classLoader, wfs); 
		if (rdfString != null){
			return new ResponseEntity<String>(rdfString, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	
}
