package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DClge.
 * 
 * @see it.sinergis.gsc.model.DClge
 * @author Hibernate Tools
 */
public class DClgeHome {
	
	private static final Logger log = Logger.getLogger(DClgeHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DClge findById(String id) {
		log.debug("getting DClge instance with id: " + id);
		try {
			DClge instance = entityManager.find(DClge.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DClge merge(DClge detachedInstance) {
		log.debug("merging DClge instance");
		try {
			DClge result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DClge transientInstance) {
		log.debug("persisting DClge instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DClge persistentInstance) {
		log.debug("removing DClge instance");
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
