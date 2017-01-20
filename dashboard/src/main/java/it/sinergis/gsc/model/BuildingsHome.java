package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class Buildings.
 * 
 * @see it.sinergis.gsc.model.Buildings
 * @author Hibernate Tools
 */
public class BuildingsHome {
	
	private static final Logger log = Logger.getLogger(BuildingsHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public Buildings findById(String id) {
		log.debug("getting Buildings instance with id: " + id);
		try {
			Buildings instance = entityManager.find(Buildings.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Buildings merge(Buildings detachedInstance) {
		log.debug("merging Buildings instance");
		try {
			Buildings result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(Buildings transientInstance) {
		log.debug("persisting Buildings instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(Buildings persistentInstance) {
		log.debug("removing Buildings instance");
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
