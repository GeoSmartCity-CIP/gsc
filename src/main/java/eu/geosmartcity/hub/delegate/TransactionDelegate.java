package eu.geosmartcity.hub.delegate;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

public class TransactionDelegate {
	private static final Logger LOGGER = Logger.getLogger(TransactionDelegate.class);
	private Transaction transaction;
	
	public TransactionDelegate() {
		this.transaction = new DefaultTransaction("handle");
	}
	
	/**
	 * metodo per la creazione della transazione su db
	 * 
	 * @param featStore
	 * @param collection
	 * @return
	 * @throws IOException
	 */
	public final boolean commit() throws IOException {
		boolean response = true;
		
		try {
			transaction.commit();
		}
		catch (Exception eek) {
			LOGGER.warn("ERROR during commit operation");
			LOGGER.debug(eek);
			transaction.rollback();
			response = false;
		}
		return response;
	}
	
	public final boolean addFeatures(FeatureStore<SimpleFeatureType, SimpleFeature> featStore,
			SimpleFeatureCollection collection) {
		boolean response = true;
		
		featStore.setTransaction(this.transaction);
		
		try {
			featStore.addFeatures(collection);
		}
		catch (IOException e) {
			LOGGER.warn("I dati non sono stati aggiunti sul db.");
			LOGGER.error("", e);
			response = false;
		}
		
		return response;
	}
	
	public final boolean modifyFeature(FeatureStore<SimpleFeatureType, SimpleFeature> featStore, Filter filter,
			Name attributeName, Object attributeValue) {
		boolean response = true;
		
		featStore.setTransaction(this.transaction);
		
		try {
			featStore.modifyFeatures(attributeName, attributeValue, filter);
		}
		catch (IOException e) {
			LOGGER.warn("I dati non sono stati aggiunti sul db.");
			LOGGER.error("", e);
			response = false;
		}
		
		return response;
	}
	
	public final boolean modifyFeature(FeatureStore<SimpleFeatureType, SimpleFeature> featStore, Filter filter,
			Name[] attributeNames, Object[] attributeValues) {
		boolean response = true;
		
		featStore.setTransaction(this.transaction);
		
		try {
			featStore.modifyFeatures(attributeNames, attributeValues, filter);
		}
		catch (IOException e) {
			LOGGER.warn("I dati non sono stati aggiunti sul db.");
			LOGGER.error("", e);
			response = false;
		}
		
		return response;
	}
	
	public final boolean removeFeatures(FeatureStore<SimpleFeatureType, SimpleFeature> featStore, Filter filter) {
		boolean response = true;
		
		featStore.setTransaction(this.transaction);
		
		try {
			featStore.removeFeatures(filter);
		}
		catch (IOException e) {
			LOGGER.warn("I dati non sono stati rimossi dal db.");
			LOGGER.debug(e);
			response = false;
		}
		
		return response;
	}
	
	public final FeatureCollection getFeatures(FeatureStore<SimpleFeatureType, SimpleFeature> featStore, Filter filter) {
		FeatureCollection responseFeatures = null;
		featStore.setTransaction(this.transaction);
		
		try {
			responseFeatures = featStore.getFeatures(filter);
		}
		catch (IOException e) {
			LOGGER.warn("Nessuna features trovata.");
			LOGGER.debug(e);
		}
		
		return responseFeatures;
	}
	
	public final boolean close() {
		try {
			this.transaction.close();
		}
		catch (IOException e) {
			LOGGER.error("Chiusara della transazione non avvenuta correttamente.");
			return false;
		}
		return true;
	}
	
	public final boolean rollback() {
		try {
			this.transaction.rollback();
		}
		catch (Exception e) {
			LOGGER.debug("Rollback della transazione fallito");
			return false;
		}
		return true;
	}
}
