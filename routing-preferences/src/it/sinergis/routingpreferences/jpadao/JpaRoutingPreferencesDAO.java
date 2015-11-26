package it.sinergis.routingpreferences.jpadao;

import it.sinergis.routingpreferences.common.PropertyReader;
import it.sinergis.routingpreferences.dao.RoutingPreferencesDAO;
import it.sinergis.routingpreferences.model.RoutingPreferences;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

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

	@Override
	public void insertRoutingPreferences(String jsonText)
			throws Exception {
		EntityManager em = null;
		try {			
			
			em = getEntityManager();
			
			RoutingPreferences itinerariesPreferences= null;			
			
			em.getTransaction().begin();
			
			//Nuova preferenza
			itinerariesPreferences = new RoutingPreferences();
			itinerariesPreferences.setData(jsonText);
			
			em.persist(itinerariesPreferences);					
			em.flush();
			
			em.getTransaction().commit();				
		}
		catch(Exception ex) {
			if(em != null)
				em.getTransaction().rollback();
			
			logger.error("Save preferences error", ex);
			if(ex instanceof PersistenceException) {
				PropertyReader pr = new PropertyReader("error_messages.properties");			
				throw new Exception(pr.getValue("ER03"));
			} else {
				PropertyReader pr = new PropertyReader("error_messages.properties");			
				throw new Exception(pr.getValue("ER02"));
			}
		}
		finally {
			if (em != null)
				em.close();
		}
		
	}

	@Override
	public List<RoutingPreferences> searchRoutingPreferences(String queryText)
			throws Exception {
		EntityManager em = null;
		try {			
			
			em = getEntityManager();

			em.getTransaction().begin();
			
			//Nuova preferenza
			Query query = em.createNativeQuery(queryText,RoutingPreferences.class);					
			List<RoutingPreferences> resultList = (List<RoutingPreferences>) query.getResultList();

			return resultList;
			
		}
		catch(Exception ex) {
			if(em != null)
				em.getTransaction().rollback();
			
			logger.error("Search routing preference error", ex);
			if(ex instanceof PersistenceException) {
				PropertyReader pr = new PropertyReader("error_messages.properties");			
				throw new Exception(pr.getValue("ER03"));
			} else {
				PropertyReader pr = new PropertyReader("error_messages.properties");			
				throw new Exception(pr.getValue("ER02"));
			}
		}
		finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public void deleteRoutingPreference(String queryText)
			throws Exception {
		EntityManager em = null;
		try {			
			
			em = getEntityManager();

			em.getTransaction().begin();
			
			//Nuova preferenza
			Query query = em.createNativeQuery(queryText,RoutingPreferences.class);	
			int result = query.executeUpdate();
			logger.info(result+" records deleted.");
			em.getTransaction().commit();
		}
		catch(Exception ex) {
			if(em != null)
				em.getTransaction().rollback();
			
			logger.error("Delete routing preference error", ex);
			if(ex instanceof PersistenceException) {
				PropertyReader pr = new PropertyReader("error_messages.properties");			
				throw new Exception(pr.getValue("ER03"));
			} else {
				PropertyReader pr = new PropertyReader("error_messages.properties");			
				throw new Exception(pr.getValue("ER02"));
			}
		}
		finally {
			if (em != null)
				em.close();
		}
		
	}
		
//	/**
//	 * Metodo che esegue il salvataggio delle informazioni di una preferenza di routing
//	 */
//	public void insertRoutingPreferences(RoutingPreferencesInsertRequest routingPreferencesInsertRequest) throws  Exception {
//
//		EntityManager em = null;
//		try {			
//			
//			em = getEntityManager();
//			
//			RoutingPreferences routingPreferences = null;			
//						
//			em.getTransaction().begin();
//			
//			//Nuova preferenza
//			routingPreferences = new RoutingPreferences();
//			routingPreferences.setUserId(routingPreferencesInsertRequest.getRoutingPreference().getUserID());
//			routingPreferences.setBikingSpeed(routingPreferencesInsertRequest.getRoutingPreference().getBikingSpeed());
//			routingPreferences.setMaxBikeDistance(routingPreferencesInsertRequest.getRoutingPreference().getMaxBikeDistance());
//			routingPreferences.setMaxWalkDistance(routingPreferencesInsertRequest.getRoutingPreference().getMaxWalkDistance());
//			routingPreferences.setWalkingSpeed(routingPreferencesInsertRequest.getRoutingPreference().getWalkingSpeed());
//					
//			em.persist(routingPreferences);					
//			em.flush();
//			
//			em.getTransaction().commit();					
//		}
//		catch(Exception ex) {
//			if(em != null)
//				em.getTransaction().rollback();
//			
//			logger.error("Save routing preference error", ex);
//						
//			throw ex;
//		}
//		finally {
//			if (em != null)
//				em.close();
//		}
//	}
//
//	@Override
//	public List<RoutingPreferences> searchRoutingPreferences(String userId)
//			throws Exception {
//		
//		List<RoutingPreferences> result;
//		EntityManager em = null;
//		try {			
//			
//			em = getEntityManager();
//				
//			Query query = em.createNamedQuery("RoutingPreferences.findByUserId");
//			
//			query.setParameter("userId", userId);
//			
//			result = (List<RoutingPreferences>) query.getResultList();	
//			
//			return result;
//		}
//		catch(Exception ex) {			
//			
//			logger.error("Search routing preference service error : ", ex);
//						
//			throw ex;
//		}
//		finally {
//			if (em != null)
//				em.close();
//		}
//	}
//
//	@Override
//	public void deleteRoutingPreference(
//			RoutingPreferencesDeleteRequest routingPreferencesRequest)
//			throws Exception {
//
//		List<RoutingPreferences> result;
//		EntityManager em = null;
//		try {			
//			
//			em = getEntityManager();
//			em.getTransaction().begin();
//			
//			Query query = em.createNamedQuery("RoutingPreferences.findByUserId");
//			
//			query.setParameter("userId", routingPreferencesRequest.getUserID());
//			
//			result = (List<RoutingPreferences>) query.getResultList();	
//			if(result.isEmpty()) {
//				
//				throw new NoResultException();
//			}
//			
//			for(RoutingPreferences routingPreferences : result) {
//				em.remove(routingPreferences);
//				em.flush();	
//			}
//			
//			em.getTransaction().commit();	
//						
//		}		
//		catch(Exception ex) {			
//			if(em != null)
//				em.getTransaction().rollback();
//			
//			logger.error("Delete routing preference service error", ex);
//						
//			throw ex;
//		}
//		finally {
//			if (em != null)
//				em.close();
//		}
//	}
}
