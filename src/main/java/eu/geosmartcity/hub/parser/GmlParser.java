package eu.geosmartcity.hub.parser;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import eu.geosmartcity.hub.delegate.PostgisDelegate;
import eu.geosmartcity.hub.utils.Constants;
import eu.geosmartcity.hub.utils.Functions;
import eu.geosmartcity.hub.utils.HibernateUtil;
import eu.geosmartcity.hub.velocity.SetupInsertSensorTemplate;

public class GmlParser {
	
	final static Logger LOGGER = Logger.getLogger(GmlParser.class);
	private Session session;
	
	String epsg;
	SetupInsertSensorTemplate template;
	PostgisDelegate pg;
	String timestamp;
	
	public GmlParser(String epsg, String timestampParam) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		template = new SetupInsertSensorTemplate();
		pg = new PostgisDelegate();
		this.epsg = epsg;
		timestamp = timestampParam;
	}
	
	public void parseFile(InputStream uploadedInputStream, String heightParam, String fieldInspireIdLoc,
			String fieldInspireIdName, String cityGmlFileName) {
		
		LOGGER.info("inizio lettura file gml");
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setIgnoringElementContentWhitespace(true);
		
		Transaction tx = session.beginTransaction();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			InputSource input = new InputSource(uploadedInputStream);
			Document doc = db.parse(input);
			
			doc = removeEmptySpace(doc);
			
			Element root = doc.getDocumentElement();
			NodeList childNode = root.getChildNodes();
			
			if (childNode.getLength() == 0) {
				throw new Exception("there aren't features in gml");
			}
			
			
			if (root.getNodeName().equalsIgnoreCase(Constants.CITYGML_MODEL)
					|| root.getLocalName().equalsIgnoreCase(Constants.CITYGML_MODEL)) {
				//CITY GML
				String pathCityGML = null;
				try {
					LOGGER.info("Parsing of Citygml");
					pathCityGML = Functions.writeFileOnDisk(uploadedInputStream, cityGmlFileName);
					CityGmlParser cityGmlParser = new CityGmlParser();
					cityGmlParser.doParse(pathCityGML, (String) epsg, timestamp);
					LOGGER.info("fine lettura citygml " + pathCityGML);
				}
				catch (Exception e) {
					throw new Exception("error while read the cityGml file ");
				}
				finally {
					if (pathCityGML != null && new File(pathCityGML).exists()) {
						Functions.deleteTmpFile(pathCityGML);
					}
				}
				
			}
			else {
				//GML
				LOGGER.debug("trovate "+childNode.getLength()+ " features in gml file");
				for (int i = 0; i < childNode.getLength(); i++) {
					
					Node node = childNode.item(i);
					
					String height = findHeight(node, heightParam);
					
					String inspireIdLoc = findInspireId(node, fieldInspireIdLoc);
					String inspireIdName = findInspireId(node, fieldInspireIdName);
					
					// POLYGON
					Node findNode = findNode(node, Constants.GML_POLYGON, true, false);
					if (findNode != null) {
						
						String nodeToString = nodeToString(findNode);
						
						insertGeometry(nodeToString, tx, height, inspireIdLoc, inspireIdName, timestamp);
					}
					
					// MULTIPOLYGON
					findNode = findNode(node, Constants.GML_MULTIPOLYGON, true, false);
					if (findNode != null) {
						String nodeToString = nodeToString(findNode);
						
						insertGeometry(nodeToString, tx, height, inspireIdLoc, inspireIdName, timestamp);
					}
				}
				tx.commit();
				LOGGER.info("fine lettura file gml");
			}
			
		}
		catch (Exception e) {
			LOGGER.error("errore nell'istanziazione del parser gml", e);
			e.printStackTrace();
			tx.rollback();
		}
		
	}
	
	/**
	 * esegue il controllo della z e inserisce la geometria sul db
	 * 
	 * @param nodeToString
	 * @param tx
	 * @param height
	 */
	private void insertGeometry(String nodeToString, Transaction tx, String height, String inspireIdLoc,
			String inspireIdName, String timestamp) {
		//controlla se ha la z
		String zCoord = checkIfHasZCoord(nodeToString, tx);
		
		String sql = template.getTemplateInsertMultiPolygonFromGML(nodeToString, epsg, height, inspireIdLoc,
				inspireIdName, timestamp);
		
		if (zCoord != null && !zCoord.equals("null")) {
			//se ha la z cambio la query di insert
			sql = template.getTemplateInsertMultiPolygonZFromGML(nodeToString, epsg, height, inspireIdLoc,
					inspireIdName, timestamp);
		}
		
		pg.insertGeometry(sql, tx, session);
		
	}
	
	/**
	 * metodo che restituisce true se c'è la terza coordinata, false altrimenti
	 * 
	 * @param gml
	 * @param tx
	 * @return
	 */
	private String checkIfHasZCoord(String gml, Transaction tx) {
		String sql = template.getZCoord(gml, String.valueOf(epsg));
		List<Object> resultList = pg.getResultList(sql, tx, session);
		
		return String.valueOf(resultList.get(0));
	}
	
	/**
	 * trova l'altezza
	 * 
	 * @param node
	 * @param heightParam
	 * @return height: l'altezza trovata
	 */
	private String findHeight(Node node, String heightParam) {
		// trovo il campo altezza
		Node findNode = findNode(node, heightParam, true, false);
		
		String height = "0";
		if (findNode != null) {
			height = findNode.getFirstChild().getNodeValue();
			LOGGER.info("trovata altezza di " + height + " nel campo " + heightParam);
		}
		return height;
	}
	
	/**
	 * trova l'inspire id
	 * 
	 * @param node
	 * @param heightParam
	 * @return height: l'altezza trovata
	 */
	private String findInspireId(Node node, String inspireIdParam) {
		// trovo il campo inspire id
		Node findNode = findNode(node, inspireIdParam, true, false);
		
		String inspireId = null;
		if (findNode != null) {
			inspireId = findNode.getFirstChild().getNodeValue();
			LOGGER.debug("trovato inspireId " + inspireId + " nel campo " + inspireIdParam);
		}
		return inspireId;
	}
	
	/**
	 * converte un nodo in stringa
	 * 
	 * @param node
	 * @return
	 */
	private String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		}
		catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		return sw.toString();
	}
	
	/**
	 * rimuove gli spazi vuoti
	 * 
	 * @param doc
	 * @return
	 * @throws XPathExpressionException
	 */
	public Document removeEmptySpace(Document doc) {
		try {
			XPathFactory xpathFactory = XPathFactory.newInstance();
			// XPath to find empty text nodes.
			XPathExpression xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");
			NodeList emptyTextNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);
			
			// Remove each empty text node from document.
			for (int i = 0; i < emptyTextNodes.getLength(); i++) {
				Node emptyTextNode = emptyTextNodes.item(i);
				emptyTextNode.getParentNode().removeChild(emptyTextNode);
			}
			
		}
		catch (XPathExpressionException e) {
			LOGGER.error("errore nella rimozione degli spazi vuoti", e);
		}
		return doc;
	}
	
	/**
	 * trova il nodo nel gml
	 * 
	 * @param root
	 * @param elementName
	 * @param deep
	 * @param elementsOnly
	 * @return
	 */
	public Node findNode(Node root, String elementName, boolean deep, boolean elementsOnly) {
		// Check to see if root has any children if not return null
		if (!(root.hasChildNodes()))
			return null;
		
		// Root has children, so continue searching for them
		Node matchingNode = null;
		String nodeName = null;
		Node child = null;
		
		NodeList childNodes = root.getChildNodes();
		int noChildren = childNodes.getLength();
		for (int i = 0; i < noChildren; i++) {
			if (matchingNode == null) {
				child = childNodes.item(i);
				nodeName = child.getNodeName();
				
				if ((nodeName != null) & (nodeName.equals(elementName)))
					return child;
				
				if (nodeName.indexOf(":") != -1) {
					String nodeNameWithoutWs = nodeName.substring(nodeName.indexOf(":") + 1);
					if (nodeNameWithoutWs.equals(elementName)) {
						return child;
					}
				}
				
				if (deep)
					matchingNode = findNode(child, elementName, deep, elementsOnly);
				
			}
			else
				break;
		}
		
		if (!elementsOnly) {
			NamedNodeMap childAttrs = root.getAttributes();
			noChildren = childAttrs.getLength();
			for (int i = 0; i < noChildren; i++) {
				if (matchingNode == null) {
					child = childAttrs.item(i);
					nodeName = child.getNodeName();
					if ((nodeName != null) & (nodeName.equals(elementName)))
						return child;
				}
				else
					break;
			}
		}
		return matchingNode;
	}
	
}
