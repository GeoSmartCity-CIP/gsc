package eu.geosmartcity.hub.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;
import de.micromata.opengis.kml.v_2_2_0.Region;
import eu.geosmartcity.hub.delegate.PostgisDelegate;
import eu.geosmartcity.hub.utils.HibernateUtil;
import eu.geosmartcity.hub.velocity.SetupInsertSensorTemplate;

public class KmlParser {
	final static Logger LOGGER = Logger.getLogger(KmlParser.class);
	
	public static void main(String[] args) {
		//		System.out.println("This is KML test");
		//		final Kml kml = Kml.unmarshal(new File("C:\\Users\\A2OB0150\\Desktop\\nuova\\melzo_ridotto.kml"));
		//		KmlParser kmlll = new KmlParser();
		//		kmlll.parseKml(kml, "4326");
		
	}
	
	public void parseKml(Kml kml, String epsg, String timestamp) {
		
		List<String> polygons = new ArrayList<String>();
		Feature feature = kml.getFeature();
		parseFeature(feature, polygons);
		if (polygons.isEmpty()) {
			return;
		}
		try {
			PostgisDelegate pgis = new PostgisDelegate();
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tx = session.beginTransaction();
			SetupInsertSensorTemplate template = new SetupInsertSensorTemplate();
			for (String p : polygons) {
				LOGGER.info(p);
				String sql = template.getTemplateInsertMultiPolygonFromPolygon("POLYGON((" + p + "))", epsg, timestamp);
				pgis.insertGeometry(sql, tx, session);
			}
			tx.commit();
		}
		catch (Exception e) {
			LOGGER.error("Error during the kml writing");
		}
		
	}
	
	private void parseFeature(Feature feature, List<String> polygons) {
		if (feature != null) {
			if (feature instanceof Document) {
				Document document = (Document) feature;
				List<Feature> featureList = document.getFeature();
				for (Feature documentFeature : featureList) {
					parseFeature(documentFeature, polygons);
				}
			}
			if (feature instanceof Folder) {
				Folder folder = (Folder) feature;
				List<Feature> featList = folder.getFeature();
				for (Feature feat : featList) {
					parseFeature(feat, polygons);
				}
			}
			if (feature instanceof Placemark) {
				Placemark placemark = (Placemark) feature;
				Geometry geometry = placemark.getGeometry();
				parseGeometry(geometry, polygons);
				//per la versione 2.1
				parseRegion(placemark.getRegion());
			}
			return;
			
		}
	}
	
	private void parseGeometry(Geometry geometry, List<String> polygons) {
		if (geometry != null) {
			if (geometry instanceof MultiGeometry) {
				List<Geometry> listg = ((MultiGeometry) geometry).getGeometry();
				for (Geometry geom : listg) {
					parseGeometry(geom, polygons);
				}
				
			}
			if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				Boundary outerBoundaryIs = polygon.getOuterBoundaryIs();
				LOGGER.info("Polygon");
				if (outerBoundaryIs != null) {
					LinearRing linearRing = outerBoundaryIs.getLinearRing();
					if (linearRing != null) {
						parseGeometry(linearRing, polygons);
					}
				}
				
			}
			if (geometry instanceof LineString) {
				LineString lines = (LineString) geometry;
				List<Coordinate> coordinates = lines.getCoordinates();
				String coord = "";
				if (coordinates != null) {
					for (Coordinate coordinate : coordinates) {
						if (coord.length() > 0) {
							coord = coord + ",";
						}
						coord = coord + parseCoordinate(coordinate);
					}
					polygons.add(coord);
				}
			}
			if (geometry instanceof LinearRing) {
				List<Coordinate> coordinates = ((LinearRing) geometry).getCoordinates();
				String coord = "";
				if (coordinates != null) {
					for (Coordinate coordinate : coordinates) {
						if (coord.length() > 0) {
							coord = coord + ",";
						}
						coord = coord + parseCoordinate(coordinate);
					}
					polygons.add(coord);
				}
			}
		}
	}
	
	private String parseCoordinate(Coordinate coordinate) {
		if (coordinate != null) {
			System.out.println("Longitude: " + coordinate.getLongitude());
			System.out.println("Latitude : " + coordinate.getLatitude());
			System.out.println("Altitude : " + coordinate.getAltitude());
			System.out.println("");
			return coordinate.getLongitude() + " " + coordinate.getLatitude() + " " + coordinate.getAltitude();
		}
		return "";
	}
	
	private void parseRegion(Region region) {
		if (region != null) {
			LOGGER.info(region.getLatLonAltBox().getNorth());
			LOGGER.info(region.getLatLonAltBox().getSouth());
			LOGGER.info(region.getLatLonAltBox().getWest());
			LOGGER.info(region.getLatLonAltBox().getEast());
			LOGGER.info(region.getLatLonAltBox().getMaxAltitude());
			LOGGER.info(region.getLatLonAltBox().getMinAltitude());
		}
	}
	
}
