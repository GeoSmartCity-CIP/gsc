package it.sinergis.routingpreferences.dao;

/**
 * Factory utilizzata per ottenere gli oggetti DAO.
 * 
 * @author Andrea Di Nora
 */
public abstract class DAOFactory {
    /**
     * getRoutingPreferencesDAO.
     * 
     * @return RoutingPreferencesDAO
     */
    public abstract RoutingPreferencesDAO getRoutingPreferencesDAO();
    
    /**
     * getItinerariesPreferencesDAO.
     * 
     * @return ItinerariesPreferencesDAO
     */
    public abstract ItinerariesPreferencesDAO getItinerariesPreferencesDAO();
}
