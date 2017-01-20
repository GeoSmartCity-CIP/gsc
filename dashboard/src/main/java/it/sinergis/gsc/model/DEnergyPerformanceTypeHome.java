package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DEnergyPerformanceType.
 * 
 * @see it.sinergis.gsc.model.DEnergyPerformanceType
 * @author Hibernate Tools
 */
public class DEnergyPerformanceTypeHome {
	
	private static final Logger log = Logger.getLogger(DEnergyPerformanceTypeHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DEnergyPerformanceType findById(String id) {
		log.debug("getting DEnergyPerformanceType instance with id: " + id);
		try {
			DEnergyPerformanceType instance = entityManager.find(DEnergyPerformanceType.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DEnergyPerformanceType merge(DEnergyPerformanceType detachedInstance) {
		log.debug("merging DEnergyPerformanceType instance");
		try {
			DEnergyPerformanceType result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DEnergyPerformanceType transientInstance) {
		log.debug("persisting DEnergyPerformanceType instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DEnergyPerformanceType persistentInstance) {
		log.debug("removing DEnergyPerformanceType instance");
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
