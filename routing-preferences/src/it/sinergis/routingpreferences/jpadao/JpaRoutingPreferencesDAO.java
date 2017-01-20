package it.sinergis.routingpreferences.jpadao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import it.sinergis.routingpreferences.dao.RoutingPreferencesDAO;
import it.sinergis.routingpreferences.exception.RPException;
import it.sinergis.routingpreferences.model.RoutingPreferences;

/**
 * Classe per la gestione della JpaDAO delle preferenze di routing
 * Implementazione delle dichiarazioni presenti nel DAO corrispondente
 * 
 * @author Andrea Di Nora
 */
public class JpaRoutingPreferencesDAO extends AbstractJpaDAO implements RoutingPreferencesDAO {
	
	private static Logger logger = null;
	
	/**
	 * Costruttore Crea un nuovo JpaRoutingPreferencesDAO.
	 */
	public JpaRoutingPreferencesDAO() {
		
		logger = Logger.getLogger(this.getClass());
		
	}

	/* (non-Javadoc)
	 * @see it.sinergis.routingpreferences.dao.RoutingPreferencesDAO#updateRoutingPreferences(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateRoutingPreferences(String jsonText,String queryText)
			throws RPException {
		EntityManager em = null;
		try {			
			em = getEntityManager();
			em.getTransaction().begin();
			
			Query query = em.createNativeQuery(queryText,RoutingPreferences.class);	
			int result = query.executeUpdate();
			//no checks since there can only be one record deleted here
			logger.info(result+" records deleted.");
			RoutingPreferences routingPreferences = new RoutingPreferences();
			routingPreferences.setData(jsonText);
			
			em.persist(routingPreferences);					
			em.flush();
			
			em.getTransaction().commit();				
		}
		catch(Exception ex) {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			logger.error("update routingpreferences error", ex);
			throw new RPException("ER01");
		}
		finally {
			if (em != null)
				em.close();
		}
		
	}
	/* (non-Javadoc)
	 * @see it.sinergis.routingpreferences.dao.RoutingPreferencesDAO#insertRoutingPreferences(java.lang.String)
	 */
	@Override
	public void insertRoutingPreferences(String jsonText)
			throws RPException {
		EntityManager em = null;
		try {			
			
			em = getEntityManager();
			em.getTransaction().begin();
			
			RoutingPreferences routingPreferences = new RoutingPreferences();
			routingPreferences.setData(jsonText);
			
			em.persist(routingPreferences);					
			em.flush();
			
			em.getTransaction().commit();				
		}
		catch(Exception ex) {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			logger.error("Save routing preferences error", ex);
			throw new RPException("ER01");
		}
		finally {
			if (em != null)
				em.close();
		}
		
	}

	/* (non-Javadoc)
	 * @see it.sinergis.routingpreferences.dao.RoutingPreferencesDAO#getRoutingPreferences(java.lang.String)
	 */
	@Override
	public List<RoutingPreferences> getRoutingPreferences(String queryText)
			throws RPException {
		EntityManager em = null;
		try {						
			em = getEntityManager();
			em.getTransaction().begin();
			Query query = em.createNativeQuery(queryText,RoutingPreferences.class);					
			List<RoutingPreferences> resultList = (List<RoutingPreferences>) query.getResultList();

			return resultList;			
		}
		catch(Exception ex) {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			logger.error("Get routingpreference error", ex);
			throw new RPException("ER01");
		}
		finally {
			if (em != null)
				em.close();
		}
	}

	/* (non-Javadoc)
	 * @see it.sinergis.routingpreferences.dao.RoutingPreferencesDAO#deleteRoutingPreferences(java.lang.String)
	 */
	@Override
	public void deleteRoutingPreferences(String queryText)
			throws RPException {
		EntityManager em = null;
		try {		
				
			em = getEntityManager();

			em.getTransaction().begin();
			
			Query query = em.createNativeQuery(queryText,RoutingPreferences.class);	
			int result = query.executeUpdate();
			logger.info(result+" records deleted.");
			if(result == 0) {
				logger.error("no routing preferences found for deletion.");
				throw new RPException("ER06");
			}
			
			em.getTransaction().commit();
			
		} catch(Exception ex) {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			if(ex instanceof RPException) {
				throw ex;
			} else {
				logger.error("Delete routing preference error", ex);
				throw new RPException("ER01");
			}
		}
		finally {
			if (em != null)
				em.close();
		}
		
	}
}