package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DTypeValue.
 * 
 * @see it.sinergis.gsc.model.DTypeValue
 * @author Hibernate Tools
 */
public class DTypeValueHome {
	
	private static final Logger log = Logger.getLogger(DTypeValueHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DTypeValue findById(String id) {
		log.debug("getting DTypeValue instance with id: " + id);
		try {
			DTypeValue instance = entityManager.find(DTypeValue.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DTypeValue merge(DTypeValue detachedInstance) {
		log.debug("merging DTypeValue instance");
		try {
			DTypeValue result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DTypeValue transientInstance) {
		log.debug("persisting DTypeValue instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DTypeValue persistentInstance) {
		log.debug("removing DTypeValue instance");
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
