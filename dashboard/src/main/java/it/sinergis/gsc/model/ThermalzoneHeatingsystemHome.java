package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class ThermalzoneHeatingsystem.
 * 
 * @see it.sinergis.gsc.model.ThermalzoneHeatingsystem
 * @author Hibernate Tools
 */
public class ThermalzoneHeatingsystemHome {
	
	private static final Logger log = Logger.getLogger(ThermalzoneHeatingsystemHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public ThermalzoneHeatingsystem findById(ThermalzoneHeatingsystemId id) {
		log.debug("getting ThermalzoneHeatingsystem instance with id: " + id);
		try {
			ThermalzoneHeatingsystem instance = entityManager.find(ThermalzoneHeatingsystem.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public ThermalzoneHeatingsystem merge(ThermalzoneHeatingsystem detachedInstance) {
		log.debug("merging ThermalzoneHeatingsystem instance");
		try {
			ThermalzoneHeatingsystem result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(ThermalzoneHeatingsystem transientInstance) {
		log.debug("persisting ThermalzoneHeatingsystem instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(ThermalzoneHeatingsystem persistentInstance) {
		log.debug("removing ThermalzoneHeatingsystem instance");
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
