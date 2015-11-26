package it.sinergis.routingpreferences.jpadao;

import it.sinergis.routingpreferences.dao.DAOFactory;
import it.sinergis.routingpreferences.dao.ItinerariesPreferencesDAO;
import it.sinergis.routingpreferences.dao.RoutingPreferencesDAO;

/**
 * A factory for creating JpaDAO objects.
 *
 * {@inheritDoc}
 * @author Andrea Di Nora
 */
public class JpaDAOFactory extends DAOFactory {
	
    /**
     * Costruttore di default.
     */
    public JpaDAOFactory() {
    }
   
    @Override
    public RoutingPreferencesDAO getRoutingPreferencesDAO() {
        return new JpaRoutingPreferencesDAO();
    }

	@Override
	public ItinerariesPreferencesDAO getItinerariesPreferencesDAO() {
		return new JpaItinerariesPreferencesDAO();
	}
}
