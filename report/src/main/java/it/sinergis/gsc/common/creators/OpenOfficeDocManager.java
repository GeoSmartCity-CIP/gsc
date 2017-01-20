package it.sinergis.gsc.common.creators;

import it.sinergis.gsc.common.Constants;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.connection.ConnectionSetupException;
import com.sun.star.connection.NoConnectException;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNamed;
import com.sun.star.drawing.XShape;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lib.uno.adapter.XOutputStreamToByteArrayAdapter;
import com.sun.star.text.ControlCharacter;
import com.sun.star.text.HoriOrientation;
import com.sun.star.text.TextContentAnchorType;
import com.sun.star.text.VertOrientation;
import com.sun.star.text.WrapTextMode;
import com.sun.star.text.XBookmarksSupplier;
import com.sun.star.text.XParagraphCursor;
import com.sun.star.text.XSimpleText;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextGraphicObjectsSupplier;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;

/**
 * Classe che gestisce le interazioni con l'istanza di OpenOffice utilizzata.
 * 
 * @author Giuseppe Giuffrida
 */
public class OpenOfficeDocManager {
	
	/**
	 * Logger.
	 */
	private static Logger _logger = null;
	
	/** Indirizzo dell'istanza di OpenOffice. */
	private String _host;
	
	/** Porta di ascolto dell'istanza di OpenOffice. */
	private String _port;
	
	/** Template. */
	private XComponent _template;
	
	/** XComponentContext. */
	private XComponentContext _xcomponentcontext;
	
	/**
	 * Costruttore.
	 * 
	 * @param host
	 *            indirizzo dell'istanza di OpenOffice
	 * @param port
	 *            porta di ascolto dell'istanza di OpenOffice
	 */
	public OpenOfficeDocManager(String host, String port) {
		_logger = Logger.getLogger(this.getClass());
		_host = host;
		_port = port;
	}
	
	/**
	 * Metodo per la creazione del report GSC.
	 * 
	 * @param filePath
	 *            path dell'immagine
	 * @param jsonClass
	 *            JSON contenente informazioni come il numero e la percentuale tra gli edifici selezionati fanno parte
	 *            delle varie classi energetiche
	 * @return
	 * @throws Exception
	 */
	public XComponent createGSCReport(String filePath, ArrayNode jsonClass) throws Exception {
		
		//Recuperiamo il bookmark in cui inserire l'allegato
		XTextContent bookmark = getBookmarkByName(Constants.GSC_BOOKMARK_NAME, _template);
		if (bookmark == null) {
			return null;
		}
		
		// Recuperiamo il XTextRange associato al bookmark     	
		XTextRange bookmarkTxtRange = bookmark.getAnchor();
		XText bookmarkText = bookmarkTxtRange.getText();
		// Creiamo il cursore del testo a partira dalla posizione del bookmark 
		XTextCursor bookmarkCursor = bookmarkText.createTextCursor();
		bookmarkCursor.gotoRange(bookmarkTxtRange, false);
		
		//Creiamo un cursore per scorrere i paragrafi
		XParagraphCursor xParaCursor = UnoRuntime.queryInterface(XParagraphCursor.class, bookmarkCursor);
		
		insertParagraph(bookmarkCursor, bookmarkText, xParaCursor, false);
		
		XMultiServiceFactory xMSF = UnoRuntime.queryInterface(XMultiServiceFactory.class, _template);
		
		XTextTable risultatiTable = createTable(2, 3, bookmarkCursor, xMSF);
		
		List<String> colonne = new ArrayList<String>();
		colonne.add("Class");
		colonne.add("Number of buildings");
		colonne.add("%");
		
		//Popoliamo la riga di intestazione della tabella
		createTableHeader(risultatiTable, colonne);
		
		//popolaTabella(risultatiTable, jsonClass);
		
		// Avanziamo il cursore 
		insertParagraph(bookmarkCursor, bookmarkText, xParaCursor, false);
		
		insertImage(bookmarkCursor, convertToUrl(filePath).toString());
		
		return _template;
	}
	
	/**
	 * Metodo per la cancellazione di un'immagine in formato png temporanea.
	 * 
	 * @param filePath
	 *            path dell'immagine
	 */
	public void deleteFile(String filePath) {
		File file = new File(filePath);
		file.delete();
	}
	
	/**
	 * Metodo che si occupa di caricare il template del report GSC su OpenOffice.
	 * 
	 * @return stringa null se il caricamento del template e' andato a buon fine, altrimenti l'eventuale messaggio di
	 *         errore.
	 */
	public String loadTemplate() {
		
		//Stabiliamo la connessione con l'istanza di OpenOffice
		Object conn = getRemoteOOConnection();
		
		if (conn == null) {
			return "No connection to OpenOffice.";
		}
		
		// Create a service manager from the initial object
		XMultiComponentFactory xMCF = UnoRuntime.queryInterface(XMultiComponentFactory.class, conn);
		
		if (xMCF != null) {
			XComponentLoader loader = null;
			
			// avvia (start-up) un'istanza di Open Office
			Object desktop;
			try {
				desktop = xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", _xcomponentcontext);
				// recupera l'oggetto service XDesktop interface			    
				XDesktop xDesktop = UnoRuntime.queryInterface(XDesktop.class, desktop);
				
				loader = UnoRuntime.queryInterface(XComponentLoader.class, xDesktop);
				
				//Url del template per il comune corrente
				URL resource = this.getClass().getClassLoader().getResource("template/reportGSC.odt");
				
				//Apriamo il template su OpenOffice in maniera silente        	
				_template = getTemplate(loader, "file://" + resource.getPath());
			}
			catch (Exception e) {
				_logger.error("Error getting the GSC template.", e);
				return "Error getting the GSC template.";
			}
		}
		else {
			_logger.error("ServiceManager is null.");
			return "Error storing the GSC template.";
		}
		
		return null;
	}
	
	/**
	 * Metodo che crea il file PDF a partire dal template modificato
	 * 
	 * @param xDoc
	 *            documento da salvare come PDF
	 * @return array di byte rappresentante il file PDF
	 */
	public byte[] storeAsPDF(XComponent xDoc) {
		
		XOutputStreamToByteArrayAdapter docOdt = new XOutputStreamToByteArrayAdapter();
		
		XStorable xStorable = UnoRuntime.queryInterface(com.sun.star.frame.XStorable.class, xDoc);
		
		PropertyValue[] propertyValue = new com.sun.star.beans.PropertyValue[2];
		propertyValue[0] = new PropertyValue();
		propertyValue[0].Name = "OutputStream";
		propertyValue[0].Value = docOdt;
		propertyValue[1] = new PropertyValue();
		propertyValue[1].Name = "FilterName";
		propertyValue[1].Value = "writer_pdf_Export";
		try {
			_logger.debug("Saving document.");
			xStorable.storeToURL("private:stream", propertyValue);
			_logger.debug("Document is saved correctly.");
		}
		catch (IOException e) {
			_logger.error("Error saving the document.", e);
		}
		
		XCloseable xCloseable = UnoRuntime.queryInterface(XCloseable.class, xDoc);
		
		if (xCloseable != null) {
			try {
				_logger.debug("Closing document.");
				xCloseable.close(false);
				_logger.debug("Document is closed correctly.");
			}
			catch (CloseVetoException e) {
				_logger.error("Error closing the document.", e);
				return null;
			}
		}
		else {
			_logger.debug("Interfaccia XCloseable == null, get XComponent.");
			XComponent xComponent = UnoRuntime.queryInterface(XComponent.class, xDoc);
			_logger.debug("Perform dispose.");
			xComponent.dispose();
			_logger.debug("Dispose is performed correctly.");
		}
		return docOdt.getBuffer();
	}
	
	/**
	 * Convert string path into URL class path.
	 * 
	 * @param path
	 * @return
	 */
	private URL convertToUrl(String path) {
		if (isEmpty(path)) {
			return null;
		}
		try {
			return new URL("file:" + convertUrlString(path));
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Convert a string path to be used in URL class path entry.
	 * 
	 * @param classpath
	 * @return
	 */
	private String convertUrlString(String classpath) {
		
		if (isEmpty(classpath)) {
			return "";
		}
		
		classpath = classpath.trim();
		if (classpath.length() < 2) {
			return "";
		}
		if (classpath.charAt(0) != '/' && classpath.charAt(1) == ':') {
			// add leading slash for windows platform
			// assuming drive letter path
			classpath = "/" + classpath;
		}
		if (!classpath.endsWith("/")) {
			File file = new File(classpath);
			if (file.exists() && file.isDirectory()) {
				classpath = classpath.concat("/");
			}
		}
		return classpath;
	}
	
	/**
	 * Metodo che crea una tabella in una determinata posizione con un certo numero di righe e colonne
	 * 
	 * @param rowsCount
	 *            numero di elementi da metter in tabella (= numero righe - 1)
	 * @param columnsCount
	 *            numero di campi (colonne)
	 * @param bookmarkCursor
	 *            XTextCursor che rappresenta la posizione in cui andare ad inserire la tabella
	 * @param xMSF
	 *            ServiceManager
	 * @return tabella vuota
	 * @throws Exception
	 */
	private XTextTable createTable(int rowsCount, int columnsCount, XTextCursor bookmarkCursor,
	        XMultiServiceFactory xMSF) throws Exception {
		//creiamo la tabella dei risultati
		XTextTable risultatiTable = null;
		try {
			risultatiTable = UnoRuntime.queryInterface(XTextTable.class,
			        xMSF.createInstance("com.sun.star.text.TextTable"));
			
			//Aggiungiamo la riga di intestazione
			risultatiTable.initialize(rowsCount, columnsCount); // rows, cols		
			bookmarkCursor.getText().insertTextContent(bookmarkCursor, risultatiTable, true);
			
		}
		catch (IllegalArgumentException e) {
			_logger.error("Error in the method createTable", e);
			throw e;
		}
		catch (Exception e) {
			_logger.error("Error in the method createTable", e);
			throw e;
		}
		
		return risultatiTable;
	}
	
	/**
	 * Metodo che crea l'intestazione della tabella dei risultati
	 * 
	 * @param risultatiTable
	 *            tabella dei risultati
	 * @param colonne
	 *            elenco delle colonne
	 */
	private void createTableHeader(XTextTable risultatiTable, List<String> colonne) {
		
		String[] cellNames = risultatiTable.getCellNames();
		//int j = 0;
		int i = 0;
		for (i = 0; i < colonne.size(); i++) {
			String fieldName = colonne.get(i);
			
			XText xCellText = UnoRuntime.queryInterface(XText.class, risultatiTable.getCellByName(cellNames[i]));
			// Set the text in the cell to sText
			XTextCursor cellCur = xCellText.createTextCursor();
			xCellText.insertString(cellCur, fieldName, true);
			//Impostiamo in grassetto al stringa appena inserita
			XPropertySet xCursorProps = UnoRuntime.queryInterface(XPropertySet.class, cellCur);
			try {
				xCursorProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.BOLD));
				xCursorProps.setPropertyValue("CharFontName", "Arial Narrow");
			}
			catch (UnknownPropertyException e) {
				_logger.error("Error in the method createTableHeader", e);
			}
			catch (PropertyVetoException e) {
				_logger.error("Error in the method createTableHeader", e);
			}
			catch (IllegalArgumentException e) {
				_logger.error("Error in the method createTableHeader", e);
			}
			catch (WrappedTargetException e) {
				_logger.error("Error in the method createTableHeader", e);
			}
		}
	}
	
	/**
	 * Metodo che posiziona il cursore alla posizione individuata da un dato bookmark.
	 * 
	 * @param bookmark
	 *            nome del bookmark da ricercare
	 * @param xWriterComponent
	 *            XComponent su cui ricercare il bookmark
	 * @return XTextComponent alla posizione individuata dal bookmark
	 */
	private XTextContent getBookmarkByName(String bookmark, XComponent xWriterComponent) {
		XTextContent xFoundBookmark = null;
		
		// query XBookmarksSupplier from document model and get bookmarks collection
		XBookmarksSupplier xBookmarksSupplier = UnoRuntime.queryInterface(XBookmarksSupplier.class, xWriterComponent);
		XNameAccess xNamedBookmarks = xBookmarksSupplier.getBookmarks();
		
		// retrieve bookmark by name
		Object foundBookmark;
		
		try {
			foundBookmark = xNamedBookmarks.getByName(bookmark);
			
			xFoundBookmark = UnoRuntime.queryInterface(XTextContent.class, foundBookmark);
			
		}
		catch (NoSuchElementException e) {
			_logger.error("Bookmark " + bookmark + " not found.", e);
			
			xFoundBookmark = null;
		}
		catch (WrappedTargetException e) {
			_logger.error("Error searching the bookmark " + bookmark, e);
			xFoundBookmark = null;
		}
		
		return xFoundBookmark;
	}
	
	/**
	 * Metodo che effettua la connessione ad un'istanza remota di OpenOffice.
	 * 
	 * @return Object oggetto che rappresenta la connessione all'istanza di OpenOffice
	 */
	private Object getRemoteOOConnection() {
		
		XMultiComponentFactory xMCF;
		Object objectUrlResolver = null;
		XUnoUrlResolver xurlresolver = null;
		Object objectInitial = null;
		
		/*
		 * Bootstraps a component context with the jurt base components registered. Component context to be granted to a
		 * component for running. Arbitrary values can be retrieved from the context.
		 */
		try {
			_xcomponentcontext = Bootstrap.createInitialComponentContext(null);
			
			/*
			 * Gets the service manager instance to be used (or null). This method has been added for convenience,
			 * because the service manager is a often used object.
			 */
			xMCF = _xcomponentcontext.getServiceManager();
			_logger.debug("Service Manager: " + xMCF);
			
		}
		catch (java.lang.Exception e) {
			_logger.error("Error creating initial context of OpenOffice.", e);
			return null;
		}
		
		/*
		 * Creates an instance of the component UnoUrlResolver which supports the services specified by the factory.
		 */
		try {
			objectUrlResolver = xMCF
			        .createInstanceWithContext("com.sun.star.bridge.UnoUrlResolver", _xcomponentcontext);
			
			// Create a new url resolver
			xurlresolver = UnoRuntime.queryInterface(XUnoUrlResolver.class, objectUrlResolver);
			
			// Resolves an object that is specified as follow:
			// uno:<connection description>;<protocol description>;<initial object name>
			objectInitial = xurlresolver.resolve("uno:socket,host=" + _host + ",port=" + _port
			        + ";urp;StarOffice.ServiceManager");
			
			_logger.debug("Connection established with OpenOffice at url: " + "uno:socket,host=" + _host + ",port="
			        + _port + ";urp;StarOffice.ServiceManager");
			return objectInitial;
		}
		catch (NoConnectException e) {
			_logger.error("Connection OpenOffice: no instance at " + _host + " " + _port, e);
			
			return null;
		}
		catch (ConnectionSetupException e) {
			_logger.error("Connection OpenOffice: it's not possible to connect to a local resource.", e);
			return null;
		}
		catch (IllegalArgumentException e) {
			_logger.error("Connection OpenOffice: parameter is incorrect.", e);
			return null;
		}
		catch (com.sun.star.uno.Exception e) {
			_logger.error("Error creating UnoUrlResolver component of OpenOffice.", e);
			return null;
		}
	}
	
	/**
	 * Metodo che carica un template di OpenOffice in maniera silente.
	 * 
	 * @param loader
	 *            XComponentLoader che carica il template
	 * @param path
	 *            percorso in stile UNO del template
	 * @return XComponent rappresentante il template
	 */
	private XComponent getTemplate(XComponentLoader loader, String path) {
		
		PropertyValue[] properties = new PropertyValue[3];
		properties[0] = new PropertyValue();
		properties[0].Name = "ReadOnly";
		properties[0].Value = new Boolean(true);
		properties[1] = new PropertyValue();
		properties[1].Name = "Hidden";
		properties[1].Value = new Boolean(true);
		properties[2] = new PropertyValue();
		properties[2].Name = "AsTemplate";
		properties[2].Value = new Boolean(true);
		
		// recupera l'oggetto document da UNO runtime environment
		// mediante il loader precedentemente acquisito
		XComponent document = null;
		try {
			_logger.debug("Url template: " + path);
			
			document = loader.loadComponentFromURL(path, "_blank", 0, properties);
		}
		catch (IOException e) {
			_logger.error("Error opening the template <" + path + ">", e);
		}
		catch (IllegalArgumentException e) {
			_logger.error("Error opening the template <" + path + ">", e);
		}
		
		return document;
	}
	
	/**
	 * Metodo que inserta la imagen recibida por URL (accesible desde el servidor OO) en el documento plantilla
	 * recibido. Codice recuperato da
	 * https://joinup.ec.europa.eu/svn/opencities/tags/opencities-core/release-3.1.6/src/main
	 * /java/com/andago/opencities/services/office/OpenOfficeServiceImpl.java
	 * 
	 * @param templateFile
	 * @param imageFile
	 * @param resultFile
	 * @param mark
	 * @throws Exception
	 */
	private void insertImage(XTextCursor textCur, String imageFile) throws Exception {
		
		try {
			
			XComponent xComponent = _template;
			
			XTextDocument xTextDocument = UnoRuntime.queryInterface(XTextDocument.class, xComponent);
			
			String name = insertImageAsLinked(imageFile, textCur, xComponent);
			linkedImageToEmbeddedImage(xTextDocument, xComponent, name);
			
		}
		catch (Exception e) {
			_logger.error("Error inserting the image " + imageFile + " in the document.", e);
			throw e;
		}
	}
	
	/**
	 * Codice recuperato da
	 * https://joinup.ec.europa.eu/svn/opencities/tags/opencities-core/release-3.1.6/src/main/java/com
	 * /andago/opencities/services/office/OpenOfficeServiceImpl.java
	 * 
	 * @param imageURL
	 * @param textRange
	 * @return
	 * @throws Exception
	 */
	private String insertImageAsLinked(String imageURL, XTextRange textRange, XComponent xComponent) throws Exception {
		
		String nameImage = null;
		
		try {
			
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssS"); //$NON-NLS-1$
			nameImage = formatter.format(date) + "Image"; //$NON-NLS-1$
			
			XMultiServiceFactory xMultiServiceFactory = UnoRuntime.queryInterface(XMultiServiceFactory.class,
			        xComponent);
			
			Object jloGraphicObject = xMultiServiceFactory.createInstance("com.sun.star.text.GraphicObject"); //$NON-NLS-1$
			XTextContent xGOTextContent = UnoRuntime.queryInterface(XTextContent.class, jloGraphicObject);
			XPropertySet xGOProps = UnoRuntime.queryInterface(XPropertySet.class, jloGraphicObject);
			
			XNamed xGOName = UnoRuntime.queryInterface(XNamed.class, jloGraphicObject);
			
			// p_jlsImageName is the parameter with the name of the image in the
			// document
			xGOName.setName(nameImage);
			xGOProps.setPropertyValue("GraphicURL", imageURL); //$NON-NLS-1$
			// You can also set format options to the image
			
			// Choose among TextContentAnchorType elements
			TextContentAnchorType oTCAT = null;
			xGOProps.setPropertyValue("AnchorType", oTCAT); //$NON-NLS-1$
			
			// Choose among HoriOrientation elements
			short sHO = HoriOrientation.CENTER;
			xGOProps.setPropertyValue("HoriOrient", new Short(sHO)); //$NON-NLS-1$
			
			// Choose among VertOrientation elements
			short sVO = VertOrientation.NONE;
			xGOProps.setPropertyValue("VertOrient", new Short(sVO)); //$NON-NLS-1$
			
			//You can set the width and height of the image 
			Integer jliWidth = 14000, jliHeight = 11000;
			xGOProps.setPropertyValue("Width", jliWidth);
			xGOProps.setPropertyValue("Height", jliHeight);
			// Choose among WrapTextMode elements
			WrapTextMode oWTM = null;
			xGOProps.setPropertyValue("TextWrap", oWTM); //$NON-NLS-1$
			
			// textRange is the XTextRange in which you want to insert the image
			textRange.getText().insertTextContent(textRange, xGOTextContent, false);
			
			return nameImage;
		}
		catch (Exception e) {
			_logger.error("Error inserting the image " + imageURL + " in the document.", e);
			throw e;
		}
	}
	
	/**
	 * Metodo che permette di aggiungere un nuovo paragrafo al testo e di avanzare il cursore alla fine di tale
	 * paragrafo.
	 * 
	 * @param bookmarkCursor
	 *            cursore del testo
	 * @param bookmarkText
	 *            testo
	 * @param xParaCursor
	 *            cursore dei paragrafi
	 * @param select
	 *            true se va selezionato il testo durante lo spostamento, false altrimenti
	 */
	private boolean insertParagraph(XTextRange bookmarkCursor, XSimpleText bookmarkText, XParagraphCursor xParaCursor,
	        boolean select) {
		try {
			bookmarkText.insertControlCharacter(bookmarkCursor, ControlCharacter.PARAGRAPH_BREAK, false);
			// Posizioniamoci sul paragrafo precedentemente creato
			xParaCursor.gotoEndOfParagraph(select);
			
			return true;
		}
		catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	/**
	 * Controlla se la stringa passata è vuota o meno.
	 * 
	 * @param s
	 *            string
	 * @return
	 */
	private boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}
	
	/**
	 * Codice recuperato da
	 * https://joinup.ec.europa.eu/svn/opencities/tags/opencities-core/release-3.1.6/src/main/java/com
	 * /andago/opencities/services/office/OpenOfficeServiceImpl.java
	 * 
	 * @param xTextDocument
	 * @param xComponent
	 * @param nameImage
	 * @throws OfficeServiceException
	 */
	private void linkedImageToEmbeddedImage(XTextDocument xTextDocument, XComponent xComponent, String nameImage)
	        throws Exception {
		String jlsGOUrl = null;
		try {
			
			XMultiServiceFactory xMultiServiceFactory = UnoRuntime.queryInterface(XMultiServiceFactory.class,
			        xComponent);
			
			XTextGraphicObjectsSupplier xTGOS = UnoRuntime.queryInterface(XTextGraphicObjectsSupplier.class,
			        xMultiServiceFactory);
			XNameAccess xNAGraphicObjects = xTGOS.getGraphicObjects();
			// p_jlsImageName is the parameter with the image name (as above)
			Object jloGraphicObject = xNAGraphicObjects.getByName(nameImage);
			XTextContent xTCGraphicObject = UnoRuntime.queryInterface(XTextContent.class, jloGraphicObject);
			XPropertySet xPSTCGraphicObject = UnoRuntime.queryInterface(XPropertySet.class, xTCGraphicObject);
			// Current internal URL of the image
			jlsGOUrl = xPSTCGraphicObject.getPropertyValue("GraphicURL").toString(); //$NON-NLS-1$
			
			XTextRange xTRTCGraphicObject = xTCGraphicObject.getAnchor();
			// New internal URL
			String jlsInternalUrl = new String(""); //$NON-NLS-1$
			
			// Temporary GraphicObject number 1.
			Object jloShape1 = xMultiServiceFactory.createInstance("com.sun.star.drawing.GraphicObjectShape"); //$NON-NLS-1$
			XShape xGOShape1 = UnoRuntime.queryInterface(XShape.class, jloShape1);
			XTextContent xTCShape1 = UnoRuntime.queryInterface(XTextContent.class, xGOShape1);
			XPropertySet xPSShape1 = UnoRuntime.queryInterface(XPropertySet.class, xGOShape1);
			
			// Temporary GraphicObject number 2.
			Object jloShape2 = xMultiServiceFactory.createInstance("com.sun.star.drawing.GraphicObjectShape"); //$NON-NLS-1$
			XShape xGOShape2 = UnoRuntime.queryInterface(XShape.class, jloShape2);
			XTextContent xTCShape2 = UnoRuntime.queryInterface(XTextContent.class, xGOShape2);
			XPropertySet xPSShape2 = UnoRuntime.queryInterface(XPropertySet.class, xGOShape2);
			
			// The Temporary GraphicObject number 1 points to the same file of
			// the
			// original GraphicObject
			xPSShape1.setPropertyValue("GraphicURL", jlsGOUrl); //$NON-NLS-1$
			// By inserting the Temporary GraphicObject number 1, OpenOffice
			// creates
			// its bitmap.
			xTRTCGraphicObject.getText().insertTextContent(xTRTCGraphicObject, xTCShape1, false);
			// Assign the Bitmap of Temporary GraphicObject number 1 to
			// Temporary
			// GraphicObject number 2.
			xPSShape2.setPropertyValue("GraphicObjectFillBitmap", xPSShape1 //$NON-NLS-1$
			        .getPropertyValue("GraphicObjectFillBitmap")); //$NON-NLS-1$
			// By inserting the Temporary GraphicObject number 2, OpenOffice
			// creates
			// its internal URL (and its bitmap).
			xTRTCGraphicObject.getText().insertTextContent(xTRTCGraphicObject, xTCShape2, false);
			// The Temporary GraphicObject number 1 is no more needed.
			xTRTCGraphicObject.getText().removeTextContent(xTCShape1);
			// Get the internal URL of the Temporary GraphicObject number 2.
			jlsInternalUrl = xPSShape2.getPropertyValue("GraphicURL") //$NON-NLS-1$
			        .toString();
			
			// Assign to original GraphicObject the same internal URL of the
			// Temporary GraphicObject number 2.
			xPSTCGraphicObject.setPropertyValue("GraphicURL", jlsInternalUrl); //$NON-NLS-1$
			// The Temporary GraphicObject number 2 is no more needed.
			xTRTCGraphicObject.getText().removeTextContent(xTCShape2);
			
		}
		catch (Exception e) {
			_logger.error("Error inserting the image " + jlsGOUrl + " in the document.", e);
		}
	}
	
	/**
	 * Metodo che popola la tabella
	 * 
	 * @param risultatiTable
	 *            tabella dei risultati
	 * @param json
	 */
	private void popolaTabella(XTextTable risultatiTable, ArrayNode json) {
		
		String[] tableCellNames = risultatiTable.getCellNames();
		
		int numTableColumns = risultatiTable.getColumns().getCount();
		
		//		for (int featIdx = 0; featIdx < json.size(); featIdx++) {
		//			JsonNode node = json.get(featIdx);
		//			
		//			XText xCellText = UnoRuntime.queryInterface(XText.class,
		//			        risultatiTable.getCellByName(tableCellNames[numTableColumns]));
		//			
		//			XTextCursor cellCur = xCellText.createTextCursor();
		//			xCellText.insertString(cellCur, node.path("name").toString(), true);
		//			XPropertySet xCursorProps = UnoRuntime.queryInterface(XPropertySet.class, cellCur);
		//			try {
		//				xCursorProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.NORMAL));
		//				xCursorProps.setPropertyValue("CharFontName", "Arial Narrow");
		//			}
		//			catch (UnknownPropertyException e) {
		//				_logger.error("Errore durante l'impostazione del font della tabella", e);
		//			}
		//			catch (PropertyVetoException e) {
		//				_logger.error("Errore durante l'impostazione del font della tabella", e);
		//			}
		//			catch (IllegalArgumentException e) {
		//				_logger.error("Errore durante l'impostazione del font della tabella", e);
		//			}
		//			catch (WrappedTargetException e) {
		//				_logger.error("Errore durante l'impostazione del font della tabella", e);
		//			}
		//		}
	}
}
