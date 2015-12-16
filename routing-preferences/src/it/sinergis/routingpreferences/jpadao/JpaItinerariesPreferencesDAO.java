package it.sinergis.routingpreferences.jpadao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import it.sinergis.routingpreferences.dao.ItinerariesPreferencesDAO;
import it.sinergis.routingpreferences.exception.RPException;
import it.sinergis.routingpreferences.model.ItinerariesPreferences;

public class JpaItinerariesPreferencesDAO extends AbstractJpaDAO implements ItinerariesPreferencesDAO{

private static Logger logger = null;
	
	/**
	 * Costruttore Crea un nuovo JpaRoutingPreferencesDAO.
	 */
	public JpaItinerariesPreferencesDAO() {
		
		logger = Logger.getLogger(this.getClass());
		
	}
	

	/* (non-Javadoc)
	 * @see it.sinergis.routingpreferences.dao.ItinerariesPreferencesDAO#saveItinerary(java.lang.String)
	 */
	@Override
	public Long saveItinerary(String jsonText) throws RPException {
		
		EntityManager em = null;
		try {					
			em = getEntityManager();
			em.getTransaction().begin();
			ItinerariesPreferences itinerariesPreferences = new ItinerariesPreferences();	
			itinerariesPreferences.setData(jsonText);
			
			em.persist(itinerariesPreferences);					
			em.flush();
			
			em.getTransaction().commit();	
			return itinerariesPreferences.getId();
		}
		catch(Exception ex) {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			logger.error("Save itinerarye error", ex);
			throw new RPException("ER01");
		}
		finally {
			if (em != null)
				em.close();
		}
	}

	/* (non-Javadoc)
	 * @see it.sinergis.routingpreferences.dao.ItinerariesPreferencesDAO#readItinerary(java.lang.String)
	 */
	@Override
	public List<ItinerariesPreferences> readItinerary(String queryText) throws RPException {
		EntityManager em = null;
		try {			
			em = getEntityManager();
			em.getTransaction().begin();

			Query query = em.createNativeQuery(queryText,ItinerariesPreferences.class);					
			List<ItinerariesPreferences> resultList = (List<ItinerariesPreferences>) query.getResultList();

			return resultList;
			
		}
		catch(Exception ex) {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			logger.error("Read itinerary error", ex);
			throw new RPException("ER01");
		}
		finally {
			if (em != null)
				em.close();
		}
	}
	
	/* (non-Javadoc)
	 * @see it.sinergis.routingpreferences.dao.ItinerariesPreferencesDAO#deleteItineraryById(java.lang.Long)
	 */
	@Override
	public void deleteItineraryById(Long id) throws RPException {
		EntityManager em = null;
		try {			
			
			em = getEntityManager();

			ItinerariesPreferences itinerariesPreferences = em.find(ItinerariesPreferences.class,id);
			if(itinerariesPreferences != null) {
				em.getTransaction().begin();
				em.remove(itinerariesPreferences);
				em.getTransaction().commit();
			} else {
				logger.error("no itineraries found for deletion.");
				throw new RPException("ER06");
			}		
		}
		catch(Exception ex) {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			if(ex instanceof RPException) {
				throw ex;
			} else {
				logger.error("Delete itinerary error",ex);
				throw new RPException("ER01");
			}
		}
		finally {
			if (em != null)
				em.close();
		}
	}
}
