package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class ThermalzoneEnergyamountT.
 * 
 * @see it.sinergis.gsc.model.ThermalzoneEnergyamountT
 * @author Hibernate Tools
 */
public class ThermalzoneEnergyamountTHome {
	
	private static final Logger log = Logger.getLogger(ThermalzoneEnergyamountTHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public ThermalzoneEnergyamountT findById(ThermalzoneEnergyamountTId id) {
		log.debug("getting ThermalzoneEnergyamountT instance with id: " + id);
		try {
			ThermalzoneEnergyamountT instance = entityManager.find(ThermalzoneEnergyamountT.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public ThermalzoneEnergyamountT merge(ThermalzoneEnergyamountT detachedInstance) {
		log.debug("merging ThermalzoneEnergyamountT instance");
		try {
			ThermalzoneEnergyamountT result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(ThermalzoneEnergyamountT transientInstance) {
		log.debug("persisting ThermalzoneEnergyamountT instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(ThermalzoneEnergyamountT persistentInstance) {
		log.debug("removing ThermalzoneEnergyamountT instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		}
		catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}
}
