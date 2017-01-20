package it.sinergis.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ProjectUtils {
	private static final Logger logger = Logger.getLogger(ProjectUtils.class);

	private static ClassLoader _classLoader = null;
	private static Properties prop = null;

	public static String getProperty(ClassLoader classLoader, String propertyName){
		if (prop == null){
			InputStream input = null;
			try {
				_classLoader = classLoader;
				input = _classLoader.getResourceAsStream("config/project.properties");
				prop = new Properties();
				prop.load(input);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return prop.getProperty(propertyName);
	}

	public static String generateRDF(ClassLoader classLoader, String wfs){
		URL wfsURL;
		String xmlResponse = "";
		try {
			wfsURL = new URL(wfs);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					wfsURL.openStream()));

			String inputLine = "";;
			while ((inputLine = in.readLine()) != null){
				xmlResponse = xmlResponse + inputLine;
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.error("errore nella chiamata al wfs");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("errore nella chiamata al wfs");
			return null;
		}

		if (!xmlResponse.equals("")){
			String rdf = getRDFHeader("config/header");

			if (rdf == null){
				logger.error("file di base vuoto");
				return null;
			}

			DocumentBuilder db = null;
			Document doc = null;
			InputSource is = new InputSource();
			try {
				db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				is.setCharacterStream(new StringReader(xmlResponse));
				doc = db.parse(is);
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
				logger.error("errore nella costruzione dell'xml");
				return null;
			} catch (SAXException e) {
				e.printStackTrace();
				logger.error("errore nella costruzione dell'xml");
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("errore nella costruzione dell'xml");
				return null;
			}

			NodeList nodes = doc.getElementsByTagName("gml:featureMember");
			if (nodes.getLength() > 0){
				logger.info("Elementi tornati dal WFS: " + nodes.getLength());
			}else{
				logger.info("zero elementi ritornati dal WFS");
				return null;
			}

			String specialsNodes = ProjectUtils.getProperty(classLoader, "specialsNodes");
			List<String> specials = new ArrayList<String>();
			if (specialsNodes != null){
				StringTokenizer specialNodesTokenizer = new StringTokenizer(specialsNodes, "||");
				while (specialNodesTokenizer.hasMoreElements()){
					specials.add((String) specialNodesTokenizer.nextElement());
				}
			}			

			HashMap<String, Connection> connectionTable = new HashMap<String, Connection>();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				
				NodeList nodi = element.getElementsByTagName(ProjectUtils.getProperty(classLoader, "wfsFeatureMemberNode"));
				for (int j = 0; j < nodi.getLength(); j++) {
					Node nodo = nodi.item(j);
					if (nodo.getNodeType() == Node.ELEMENT_NODE){
						Element elem = (Element) nodo;
						String wfsFields = ProjectUtils.getProperty(classLoader, "wfs_fields");
						String rdfDescrition = "";

						if (wfsFields != null){
							StringTokenizer wfsFieldsToken = new StringTokenizer(wfsFields, "||");
							while (wfsFieldsToken.hasMoreElements()){
								String propertyNameWFS = (String) wfsFieldsToken.nextElement();
								String wfsPropertyValue = getNodeValue(elem, propertyNameWFS);
								if (wfsPropertyValue != null){
									String propertyRDFTemplate = ProjectUtils.getPropertyTemplate("config/nodes", propertyNameWFS.replace(":", "_-_"));
									if (propertyRDFTemplate != null){
										propertyRDFTemplate = propertyRDFTemplate.replace("@VALUE@", wfsPropertyValue);
										rdfDescrition += propertyRDFTemplate;
									}
								}
							}
						}

						if (specials.size() > 0){
							for (String s: specials){
								Properties config = ProjectUtils.getPropertyFile("config/specialNodes/" + s + "/node.properties");
								if (config != null){
									if (config.getProperty("type") != null && config.getProperty("type").toUpperCase().equals("WFS")){
										String rdfElementHeader = config.getProperty("descriptionHeader");

										List<String> fieldsNameList = getListFromTokenString(config.getProperty("fieldsName"), "||");
										List<String> fieldsMappingList = getListFromTokenString(config.getProperty("fieldsMapping"), "||");

										boolean addProperty = false;
										for (int index=0; index<fieldsNameList.size(); index++){
											String value = getNodeValue(elem, fieldsNameList.get(index));
											if (value != null){
												String tpl = fieldsMappingList.get(index);
												rdfElementHeader += tpl.replace("@VALUE@", value);
												addProperty = true;
											}
										}

										rdfElementHeader += config.getProperty("descriptionFooter");
										if (addProperty)
											rdfDescrition += rdfElementHeader;
									}else if (config.getProperty("type") != null && config.getProperty("type").toUpperCase().equals("DB")){
										Connection connection = connectionTable.get(config.getProperty("postgresHost")+config.getProperty("postgresUser"));
										Statement st = null;
										try {

											if (connection == null){
												Class.forName("org.postgresql.Driver");
												connection = DriverManager.getConnection(config.getProperty("postgresHost"), 
														config.getProperty("postgresUser"), 
														config.getProperty("postgresPassword"));
												connectionTable.put(config.getProperty("postgresHost")+config.getProperty("postgresUser"), connection);
											}

											st = connection.createStatement();
											ResultSet rs = st.executeQuery("SELECT * FROM " + config.getProperty("table") + " WHERE " + config.getProperty("filterField") + " = '" 
													+ getNodeValue(elem, config.getProperty("filterWFSField")) + "'");
											List<String> fieldsNameList = getListFromTokenString(config.getProperty("fieldsName"), "||");
											List<String> fieldsMappingList = getListFromTokenString(config.getProperty("fieldsMapping"), "||");
											List<String> fieldsTypeList = getListFromTokenString(config.getProperty("fieldsType"), "||");
											while (rs.next()) {
												String rdfElementHeader = config.getProperty("descriptionHeader");
												boolean addProperty = false;
												for (int index=0; index<fieldsNameList.size(); index++){
													String value = null;
													if (fieldsTypeList.get(index).toLowerCase().equals("string")){
														value = rs.getString(fieldsNameList.get(index));
													}else if (fieldsTypeList.get(index).toLowerCase().equals("double")){
														value = ""+rs.getDouble(fieldsNameList.get(index));
													}else if (fieldsTypeList.get(index).toLowerCase().equals("date")){
														value = rs.getDate(fieldsNameList.get(index)).toLocaleString();
													}

													if (value != null){
														String tpl = fieldsMappingList.get(index);
														rdfElementHeader += tpl.replace("@VALUE@", value);
														addProperty = true;
													}
												}

												rdfElementHeader += config.getProperty("descriptionFooter");
												if (addProperty)
													rdfDescrition += rdfElementHeader;
											} 
											rs.close();
											st.close();
										} catch (SQLException e) {
											e.printStackTrace();
											logger.error("errore nella connessione al db, non saranno presenti le informazioni presenti su db");
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
											logger.error("errore nella connessione al db, non saranno presenti le informazioni presenti su db");
										}
									}
								}
							}
						}

						rdf += rdfDescrition + "</rdf:Description>";
					}
				}
			}
			
			if (!connectionTable.isEmpty()){
				Set<String> keys = connectionTable.keySet();
				Iterator it = keys.iterator();
				while (it.hasNext()){
					Connection c = connectionTable.get(it.next());
					try {
						c.close();
					} catch (SQLException e) {
						e.printStackTrace();
						logger.error("errore nella chiusura della connessione al db");
					}
				}
			}
			
			rdf = rdf + "</rdf:RDF>";
			System.out.println(rdf);
			return rdf;
		}

		return null;
	}

	private static List<String> getListFromTokenString(String tokenString, String token){
		StringTokenizer st = new StringTokenizer(tokenString, token);
		List<String> list = new ArrayList<String>();
		while (st.hasMoreElements()){
			list.add((String) st.nextElement());
		}

		return list;
	}

	private static Properties getPropertyFile(String filePath){
		InputStream input = _classLoader.getResourceAsStream(filePath);
		Properties p = null;

		try {
			p = new Properties();
			p.load(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return p;
	}

	private static String getPropertyTemplate(String path, String propertyName){
		try {
			return readFile(path + "/" + propertyName);
		} catch (IOException e) {
			logger.error("errore durante il recupero del template " + path + "/" + propertyName);
			e.printStackTrace();
		}
		return null;
	}

	private static String getRDFHeader(String fileName){
		try {
			return readFile(fileName);
		} catch (IOException e) {
			logger.error("errore durante il recupero del file " + fileName);
			e.printStackTrace();
		}
		return null;
	}

	private static String readFile(String fileName) throws IOException {
		InputStream input = _classLoader.getResourceAsStream(fileName);
		InputStreamReader r = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(r);

		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	private static String getNodeValue(Element node, String propertyName){
		if (node.getElementsByTagName(propertyName) != null && node.getElementsByTagName(propertyName).getLength() > 0){
			return node.getElementsByTagName(propertyName).item(0).getChildNodes().item(0).getNodeValue();
		}

		return null;
	}
}
