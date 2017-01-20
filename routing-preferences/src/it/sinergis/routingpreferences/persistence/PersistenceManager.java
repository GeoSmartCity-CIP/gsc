package it.sinergis.routingpreferences.persistence;

import it.sinergis.routingpreferences.common.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;


/**
 * Classe che gestisce l'Entity Manager Factory per il collegamento con la base dati.
 * 
 * @author Andrea Di Nora <br/>
 */
public class PersistenceManager {
    /** Puntatore all'oggetto stesso (singletone). */
    private static PersistenceManager pm = null;

    /** Puntatore all'entity manager factory. */
    private static EntityManagerFactory emf = null;

    /** Il logger di log4j. */
    private static Logger logger = null;

    /**
     * Crea una nuova istanza della classe.
     */
    private PersistenceManager() {
        logger = Logger.getLogger(PersistenceManager.class);
        logger.debug("Starting EntityManagerFactory.");
        emf = Persistence.createEntityManagerFactory(Constants.PERSISTENCE_UNIT);
        
    }

    /**
     * Restituisce un'istanza singletone della classe.
     * 
     * @return PersistenceManager Restituisce un'istanza singletone della classe.
     */
    public static synchronized PersistenceManager getInstance() {
        if (pm == null) {
            pm = new PersistenceManager();
            logger.debug("Create new PersistenceManager instance.");
        }

        logger.debug("PersistenceManager created.");

        return pm;
    }

    /**
     * Chiude il collegamento con la base dati.
     */
    public synchronized void closeEntityManagerFactory() {
        if ((emf != null) && (emf.isOpen() == true)) {
            emf.close();
            logger.debug("EntityManagerFactory closed.");
        }
    }

    /**
     * Restituisce un entity manager.
     * 
     * @return entity manager factory
     */
    public EntityManager getEntityManager() {
        EntityManagerFactory entManFactory = PersistenceManager.getInstance()
                .getEntityManagerFactory();
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
        logger.debug("Got EntityManagerFactory.");

        return emf;
    }
}
