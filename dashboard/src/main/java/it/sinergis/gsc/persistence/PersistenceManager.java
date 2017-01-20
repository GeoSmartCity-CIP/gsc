package it.sinergis.gsc.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

/**
 * Classe che gestisce l'Entity Manager Factory per il collegamento con la base dati.
 * 
 * @author Manuel De Meo
 */
public class PersistenceManager {
	/** Puntatore all'entity manager factory. */
	private static EntityManagerFactory emf = null;
	
	/** Il logger di log4j. */
	private static Logger logger = null;
	
	/** Puntatore all'oggetto stesso (singletone). */
	private static PersistenceManager pm = null;
	
	/**
	 * Restituisce un'istanza singletone della classe.
	 * 
	 * @return PersistenceManager Restituisce un'istanza singletone della classe.
	 */
	public static synchronized PersistenceManager getInstance() {
		if (pm == null) {
			pm = new PersistenceManager();
			logger.debug("Creata nuova istanza del PersistenceManager.");
		}
		
		logger.debug("Restituito il PersistenceManager.");
		
		return pm;
	}
	
	/**
	 * Crea una nuova istanza della classe.
	 */
	private PersistenceManager() {
		logger = Logger.getLogger(PersistenceManager.class);
		logger.debug("Inizializzo l'EntityManagerFactory.");
		emf = Persistence.createEntityManagerFactory("navigatore_fotocartografico_gsc_plugin");
	}
	
	/**
	 * Chiude il collegamento con la base dati.
	 */
	public synchronized void closeEntityManagerFactory() {
		if ((emf != null) && (emf.isOpen() == true)) {
			emf.close();
			logger.debug("Chiuso l'EntityManagerFactory.");
		}
	}
	
	/**
	 * Restituisce un entity manager.
	 * 
	 * @return entity manager factory
	 */
	public EntityManager getEntityManager() {
		EntityManagerFactory entManFactory = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager entMan = null;
		
		entMan = entManFactory.createEntityManager();
		
		return entMan;
	}
	
	/**
	 * Restituisce un puntatore all'entity manager factory.
	 * 
	 * @return entity manager factory
	 */
	public EntityManagerFactory getEntityManagerFactory() {
		logger.debug("Restituito l'EntityManagerFactory.");
		
		return emf;
	}
}
