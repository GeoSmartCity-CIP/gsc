package it.sinergis.gsc.services;

import it.sinergis.gsc.common.PropertyReader;
import it.sinergis.gsc.common.creators.OpenOfficeDocManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.UUID;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sun.star.lang.XComponent;

public class CreateReportService {
	
	/** Logger. */
	private static final Logger log = Logger.getLogger(CreateReportService.class);
	
	/** Indirizzo dell'istanza di OpenOffice da usare per la creazione del report GSC. */
	private String _host;
	
	/** Porta dell'istanza di OpenOffice da usare per la creazione del report GSC. */
	private String _port;
	
	/** Cartella in cui vengono inseriti i file .png contenenti i grafici del plugin GSC. */
	private String _tempFileDir;
	
	/** Object mapper. */
	private ObjectMapper om;
	
	/** Property reader. */
	private PropertyReader pr;
	
	/**
	 * Costruttore.
	 * 
	 * @param host
	 *            indirizzo dell'istanza di OpenOffice da usare per la creazione del report GSC
	 * @param port
	 *            porta dell'istanza di OpenOffice da usare per la creazione del report GSC
	 * @param tempFileDir
	 *            cartella in cui vengono inseriti i file .png contenenti i grafici del plugin GSC
	 */
	public CreateReportService(String host, String port, String tempFileDir) {
		_host = host;
		_port = port;
		_tempFileDir = tempFileDir;
		om = new ObjectMapper();
		pr = new PropertyReader("error_messages.properties");
	}
	
	/**
	 * Converte l'svg passato come parametro in un file png che viene salvato nella cartella temporanea.
	 * 
	 * @param svgAsXML
	 *            svg contenente un grafico del plugin GSC
	 * @return path del file png salvato temporaneamente
	 * @throws Exception
	 */
	public String convertiSvgInPng(String svgAsXML) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append(_tempFileDir);
		sb.append("chart");
		sb.append(UUID.randomUUID().toString());
		sb.append(".png");
		
		OutputStream pdf_ostream = null;
		
		try {
			TranscoderInput input_svg_image = new TranscoderInput(new StringReader(svgAsXML));
			pdf_ostream = new FileOutputStream(sb.toString());
			
			TranscoderOutput output_pdf_file = new TranscoderOutput(pdf_ostream);
			Transcoder transcoder = new PNGTranscoder();
			transcoder.transcode(input_svg_image, output_pdf_file);
			
			pdf_ostream.flush();
			pdf_ostream.close();
			
			return sb.toString();
		}
		catch (FileNotFoundException fnfe) {
			log.error("Directory not found: " + _tempFileDir, fnfe);
			throw new Exception(fnfe);
		}
		catch (IOException ioe) {
			log.error("Error in the method convertiSvgInPng", ioe);
			throw new Exception(ioe);
		}
		catch (Exception e) {
			log.error("Error in the method convertiSvgInPng", e);
			
			pdf_ostream.flush();
			pdf_ostream.close();
			
			OpenOfficeDocManager ooMgr = new OpenOfficeDocManager(_host, _port);
			//elimino il file creato in locale
			ooMgr.deleteFile(sb.toString());
			
			throw e;
		}
	}
	
	/**
	 * Tale metodo crea e ritorna il report GSC.
	 * 
	 * @param filePath
	 *            path del file png salvato temporaneamente
	 * @param jsonClass
	 *            JSON formattato come stringa contenente informazioni come il numero e la percentuale tra gli edifici
	 *            selezionati fanno parte delle varie classi energetiche
	 * @return array di byte rappresentante il pdf
	 * @throws Exception
	 */
	public byte[] createReport(String filePath, String jsonClass) throws Exception {
		
		OpenOfficeDocManager ooMgr = new OpenOfficeDocManager(_host, _port);
		
		try {
			String msg = ooMgr.loadTemplate();
			if (msg != null) {
				throw new Exception(msg);
			}
			
			ArrayNode arrayNodeClass = ((ArrayNode) om.readTree(jsonClass));
			
			XComponent doc = ooMgr.createGSCReport(filePath, arrayNodeClass);
			if (doc == null) {
				throw new Exception("Error creating the GSC report.");
			}
			
			byte[] pdf = ooMgr.storeAsPDF(doc);
			if (pdf == null) {
				throw new Exception("Error storing the GSC report.");
			}
			
			//elimino il file creato in locale
			ooMgr.deleteFile(filePath);
			
			return pdf;
		}
		catch (Exception e) {
			ooMgr.deleteFile(filePath);
			throw e;
		}
	}
	
	/**
	 * Elimina il file png salvato temporaneamente.
	 * 
	 * @param filePath
	 *            path del file png salvato temporaneamente
	 */
	public void deleteFilePng(String filePath) {
		OpenOfficeDocManager ooMgr = new OpenOfficeDocManager(_host, _port);
		ooMgr.deleteFile(filePath);
	}
	
	public PropertyReader getPr() {
		return pr;
	}
	
	public void setPr(PropertyReader pr) {
		this.pr = pr;
	}
}
