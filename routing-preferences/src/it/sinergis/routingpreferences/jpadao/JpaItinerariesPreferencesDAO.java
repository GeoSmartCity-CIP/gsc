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
	
	/**
	 * Metodo che esegue il salvataggio delle informazioni di una preferenza di routing
	 */
	public void saveItinerary(String jsonText) throws RPException {
		
		EntityManager em = null;
		try {					
			em = getEntityManager();
			em.getTransaction().begin();
			ItinerariesPreferences itinerariesPreferences = new ItinerariesPreferences();	
			itinerariesPreferences.setData(jsonText);
			
			em.persist(itinerariesPreferences);					
			em.flush();
			
			em.getTransaction().commit();				
		}
		catch(Exception ex) {
			if(em != null)
				em.getTransaction().rollback();
			
			logger.error("Save itinerary preference error", ex);
			throw new RPException("ER01");
		}
		finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public List<ItinerariesPreferences> readItinerary(String queryText) throws RPException {
		EntityManager em = null;
		try {			
			
			em = getEntityManager();

			em.getTransaction().begin();
			
			//Nuova preferenza
			Query query = em.createNativeQuery(queryText,ItinerariesPreferences.class);					
			List<ItinerariesPreferences> resultList = (List<ItinerariesPreferences>) query.getResultList();

			return resultList;
			
		}
		catch(Exception ex) {
			if(em != null)
				em.getTransaction().rollback();
			
			logger.error("Read itinerary preference error", ex);
			throw new RPException("ER01");
		}
		finally {
			if (em != null)
				em.close();
		}
	}
	
//	@Override
//	public void deleteItineraries(String queryText) throws RPException {
//		EntityManager em = null;
//		try {			
//			
//			em = getEntityManager();
//
//			em.getTransaction().begin();
//			
//			Query query = em.createNativeQuery(queryText,ItinerariesPreferences.class);	
//			int result = query.executeUpdate();
//			logger.info(result+" records deleted.");
//			if(result == 0) {
//				logger.error("no itineraries preferences found for deletion.");
//				throw new RPException("ER06");
//			}
//			
//			em.getTransaction().commit();
//			
//		}
//		catch(Exception ex) {
//			if(em != null)
//				em.getTransaction().rollback();
//			
//			logger.error("Read itinerary preference error", ex);
//			throw new RPException("ER01");
//		}
//		finally {
//			if (em != null)
//				em.close();
//		}
//	}
}
