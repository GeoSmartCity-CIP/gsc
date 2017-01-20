package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class HCurrentuse.
 * 
 * @see it.sinergis.gsc.model.HCurrentuse
 * @author Hibernate Tools
 */
public class HCurrentuseHome {
	
	private static final Logger log = Logger.getLogger(HCurrentuseHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public HCurrentuse findById(String id) {
		log.debug("getting HCurrentuse instance with id: " + id);
		try {
			HCurrentuse instance = entityManager.find(HCurrentuse.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public HCurrentuse merge(HCurrentuse detachedInstance) {
		log.debug("merging HCurrentuse instance");
		try {
			HCurrentuse result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(HCurrentuse transientInstance) {
		log.debug("persisting HCurrentuse instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(HCurrentuse persistentInstance) {
		log.debug("removing HCurrentuse instance");
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
