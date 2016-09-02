package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class HLocation.
 * 
 * @see it.sinergis.gsc.model.HLocation
 * @author Hibernate Tools
 */
public class HLocationHome {
	
	private static final Logger log = Logger.getLogger(HLocationHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public HLocation findById(String id) {
		log.debug("getting HLocation instance with id: " + id);
		try {
			HLocation instance = entityManager.find(HLocation.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public HLocation merge(HLocation detachedInstance) {
		log.debug("merging HLocation instance");
		try {
			HLocation result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(HLocation transientInstance) {
		log.debug("persisting HLocation instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(HLocation persistentInstance) {
		log.debug("removing HLocation instance");
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
