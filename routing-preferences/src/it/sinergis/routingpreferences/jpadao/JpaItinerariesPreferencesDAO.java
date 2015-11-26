package it.sinergis.routingpreferences.jpadao;

import it.sinergis.routingpreferences.common.PropertyReader;
import it.sinergis.routingpreferences.dao.ItinerariesPreferencesDAO;
import it.sinergis.routingpreferences.model.ItinerariesPreferences;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

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
	public void saveItinerary(String jsonText) throws Exception {
		
		EntityManager em = null;
		try {			
			
			em = getEntityManager();
			
			ItinerariesPreferences itinerariesPreferences= null;			
			
			em.getTransaction().begin();
			
			//Nuova preferenza
			itinerariesPreferences = new ItinerariesPreferences();
			itinerariesPreferences.setData(jsonText);
			
			em.persist(itinerariesPreferences);					
			em.flush();
			
			em.getTransaction().commit();				
		}
		catch(Exception ex) {
			if(em != null)
				em.getTransaction().rollback();
			
			logger.error("Save itinerary preference error", ex);
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
	public List<ItinerariesPreferences> readItinerary(String queryText) throws Exception {
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
}
