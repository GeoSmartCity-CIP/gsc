package eu.geosmartcity.hub.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

import eu.geosmartcity.hub.delegate.PostgisDelegate;
import eu.geosmartcity.hub.delegate.TransactionDelegate;
import eu.geosmartcity.hub.utils.Functions;
import eu.geosmartcity.hub.utils.HibernateUtil;
import eu.geosmartcity.hub.utils.ReadFromConfig;
import eu.geosmartcity.hub.velocity.SetupInsertSensorTemplate;

public class ShapeParser {
	final static Logger LOGGER = Logger.getLogger(ShapeParser.class);
	
	public void getDetailFromShape(String uri, String heightAttribute, String epsg, String timestamp)
			throws IOException, UnsupportedOperationException, FactoryException {
		readShape(uri, heightAttribute, epsg, timestamp);
	}
	
	private void readShape(String uri, String heightAttribute, String epsg, String timestamp) throws IOException,
			UnsupportedOperationException, FactoryException {
		
		boolean geotools = true;
		File file = new File(uri);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", file.toURI().toURL());
		DataStore dataStore = DataStoreFinder.getDataStore(map);
		
		String typeName = dataStore.getTypeNames()[0];
		
		FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
		
		Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")
		PostgisDelegate pg = new PostgisDelegate();
		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = null;
		try {
			source.getInfo();
			collection = source.getFeatures(filter);
		}
		catch (Exception e) {
			LOGGER.warn(e);
			geotools = false;
		}
		
		if (geotools) {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tx = session.beginTransaction();
			SetupInsertSensorTemplate template = new SetupInsertSensorTemplate();
			try {
				FeatureIterator<SimpleFeature> features = collection.features();
				while (features.hasNext()) {
					SimpleFeature feature = features.next();
					Geometry d = (Geometry) feature.getDefaultGeometry();
					d.getSRID();
					MultiPolygon mp = (MultiPolygon) feature.getDefaultGeometry();
					Coordinate[] coordinates = mp.getCoordinates();
					String value = "0.0";
					String multiPz = "";
					if (heightAttribute != null && heightAttribute != "") {
						value = readHeightValue(feature, heightAttribute);
					}
					else {
						multiPz = Functions.addZ(mp, coordinates);
					}
					
					System.out.println(feature.getDefaultGeometryProperty().getValue());
					String sql = "";
					if (multiPz.equals("")) {
						sql = template.getTemplateInsertMultiPolygon(value, feature.getDefaultGeometryProperty()
								.getValue().toString(), epsg, timestamp);
					}
					else {
						sql = template.getTemplateInsertMultiPolygon("0.0", multiPz, epsg, timestamp);
					}
					pg.insertGeometry(sql, tx, session);
				}
				tx.commit();
			}
			catch (Exception e) {
				LOGGER.error(e);
			}
		}
		else {//shape 3d fatto con tecnlogia esri
			List<String> geoms = getGeomFromGdal(uri);
			if (!geoms.isEmpty()) {
				PostgisDelegate pgis = new PostgisDelegate();
				Session session = HibernateUtil.getSessionFactory().getCurrentSession();
				Transaction tx = session.beginTransaction();
				SetupInsertSensorTemplate template = new SetupInsertSensorTemplate();
				for (String elem : geoms) {
					pgis.insertGeometry(template.getTemplateInsertMultiPolygon("0.0", elem, epsg, timestamp), tx,
							session);
				}
				tx.commit();
			}
			else {
				LOGGER.info("You have not been found geometries");
			}
			
		}
	}
	
	private String readHeightValue(SimpleFeature feature, String heightAttribute) {
		try {
			for (Property attribute : feature.getProperties()) {
				if (heightAttribute.equals(attribute.getName().getLocalPart())) {
					return String.valueOf(attribute.getValue());
				}
			}
		}
		catch (Exception e) {
			LOGGER.warn("Error in reading height value", e);
		}
		return "0.0";
	}
	
	private SimpleFeature getGeoSmartFeature(SimpleFeature feature, PostgisDelegate pg, TransactionDelegate tr,
			DataStore pgDatastore, MultiPolygon mpolygon, int epsg) {
		return pg.createFeature("building", "name", pgDatastore, mpolygon, epsg);
		
	}
	
	private List<String> getGeomFromGdal(String pathSource) {
		List<String> systemoutList = new ArrayList<String>();
		try {
			String[] cmdArray = new String[5];
			// first argument is the program we want to open
			cmdArray[0] = ReadFromConfig.loadByName("ogrinfopath");
			cmdArray[1] = "-where";//-where ELEMCLASS='roof'
			cmdArray[2] = ReadFromConfig.loadByName("attributeWhereOgrinfo") + "=\'"
					+ ReadFromConfig.loadByName("valueWhereOgrinfo") + "\'";
			// second argument is a txt file we want to open with notepad
			cmdArray[3] = ReadFromConfig.loadByName("ogrinfoargument");
			cmdArray[4] = pathSource;
			String systemout = Functions.execCmd(cmdArray);
			boolean done = false;
			int p1, p2 = -1;
			int index = 0;
			
			while (!done) {
				p1 = systemout.indexOf("MULTIPOLYGON", index);
				if (p1 >= 0) {
					p2 = systemout.indexOf(")))", index + 3);
				}
				if (p1 >= 0 && p2 >= 0) {
					index = p2;
					System.out.println("Index : " + index);
					systemoutList.add(systemout.substring(p1, p2 + 3));
				}
				else {
					done = true;
				}
			}
		}
		catch (IOException e) {
			LOGGER.info(e);
		}
		return systemoutList;
	}
}