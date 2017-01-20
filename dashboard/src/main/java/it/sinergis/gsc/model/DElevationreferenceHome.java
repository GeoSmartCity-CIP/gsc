package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DElevationreference.
 * 
 * @see it.sinergis.gsc.model.DElevationreference
 * @author Hibernate Tools
 */
public class DElevationreferenceHome {
	
	private static final Logger log = Logger.getLogger(DElevationreferenceHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DElevationreference findById(String id) {
		log.debug("getting DElevationreference instance with id: " + id);
		try {
			DElevationreference instance = entityManager.find(DElevationreference.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DElevationreference merge(DElevationreference detachedInstance) {
		log.debug("merging DElevationreference instance");
		try {
			DElevationreference result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DElevationreference transientInstance) {
		log.debug("persisting DElevationreference instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DElevationreference persistentInstance) {
		log.debug("removing DElevationreference instance");
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
