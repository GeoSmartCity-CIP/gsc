package it.sinergis.routingpreferences.jpadao;

import it.sinergis.routingpreferences.persistence.PersistenceManager;

import javax.persistence.EntityManager;

/**
 * Classe astratta che definisce dei metodi di base per le classi DAO.
 * 
 * @author Andrea Di Nora
 */
public abstract class AbstractJpaDAO {
    /**
     * ritorna l'entity manager.
     * 
     * @return EntityManager da una istanza del PersistenceManager
     */
    public EntityManager getEntityManager() {
        return PersistenceManager.getInstance().getEntityManager();
    }
}
