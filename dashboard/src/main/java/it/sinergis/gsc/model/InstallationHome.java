package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class Installation.
 * 
 * @see it.sinergis.gsc.model.Installation
 * @author Hibernate Tools
 */
public class InstallationHome {
	
	private static final Logger log = Logger.getLogger(InstallationHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public Installation findById(String id) {
		log.debug("getting Installation instance with id: " + id);
		try {
			Installation instance = entityManager.find(Installation.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Installation merge(Installation detachedInstance) {
		log.debug("merging Installation instance");
		try {
			Installation result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(Installation transientInstance) {
		log.debug("persisting Installation instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(Installation persistentInstance) {
		log.debug("removing Installation instance");
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
