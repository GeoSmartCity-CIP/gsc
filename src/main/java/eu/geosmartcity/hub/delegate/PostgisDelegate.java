package eu.geosmartcity.hub.delegate;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.MultiPolygon;

import eu.geosmartcity.hub.utils.HibernateUtil;
import eu.geosmartcity.hub.utils.ReadFromConfig;

public class PostgisDelegate {
	private static final Logger LOGGER = Logger.getLogger(PostgisDelegate.class);
	private final int radix = 10;
	
	public final DataStore getDataStore() {
		DataStore pgDatastore = null;
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("dbtype", "postgis");
		params.put("host", ReadFromConfig.loadByName("host"));
		params.put("port", Integer.parseInt(ReadFromConfig.loadByName("port"), radix));
		params.put("database", ReadFromConfig.loadByName("database"));
		params.put("user", ReadFromConfig.loadByName("user"));
		params.put("passwd", ReadFromConfig.loadByName("passwd"));
		params.put("schema", ReadFromConfig.loadByName("schema"));
		
		try {
			pgDatastore = DataStoreFinder.getDataStore(params);
		}
		catch (Exception e) {
			LOGGER.error("errore nell'inizializzare il database postgis ", e);
		}
		return pgDatastore;
	}
	
	public final List getAttributeStore(FeatureStore<SimpleFeatureType, SimpleFeature> featStore, Filter filter) {
		return featStore.getSchema().getAttributeDescriptors();
	}
	
	public final SimpleFeature createFeature(String typeName, String nameF, DataStore pgDatastore,
			MultiPolygon mpolygon, int epsg) {
		SimpleFeatureBuilder builder = null;
		SimpleFeatureType schema = null;
		try {
			schema = pgDatastore.getSchema(typeName); //TorciaConstants.TOR_TAB_INFORMAZIONI
			builder = new SimpleFeatureBuilder(schema);
		}
		catch (IOException e) {
			LOGGER.error("Creazione feature non andata a buon fine.");
			LOGGER.error(e);
		}
		
		//build the feature with provided ID
		SimpleFeature feature = builder.buildFeature(nameF);
		
		//TODO da concordare se mettere controllo sulle coordinate nulle, in caso non fare la geomtria della segnalazione
		createSetPointGeometry(feature, mpolygon, epsg);
		
		return feature;
	}
	
	private SimpleFeature createSetPointGeometry(SimpleFeature feature, MultiPolygon mpolygon, int epsg) {
		feature.setDefaultGeometry(mpolygon);
		return feature;
	}
	
	public boolean insertGeometry(String sql, Transaction tx, Session session) {
		
		boolean result = false;
		try {
			if (tx == null || session == null) {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				tx = session.beginTransaction();
			}
			Query query = session.createSQLQuery(sql);
			query.executeUpdate();
			result = true;
			//			result = query.list();
		}
		catch (Exception e) {
			LOGGER.error(e);
			tx.rollback();
		}
		return result;
		
	}
	
	public List getResultList(String sql, Transaction tx, Session session) {
		
		List result = null;
		try {
			if (tx == null || session == null) {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				tx = session.beginTransaction();
			}
			Query query = session.createSQLQuery(sql);
			result = query.list();
		}
		catch (Exception e) {
			LOGGER.error(e);
			tx.rollback();
		}
		return result;
		
	}
	
}
