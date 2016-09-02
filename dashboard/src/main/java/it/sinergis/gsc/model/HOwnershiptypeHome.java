package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class HOwnershiptype.
 * 
 * @see it.sinergis.gsc.model.HOwnershiptype
 * @author Hibernate Tools
 */
public class HOwnershiptypeHome {
	
	private static final Logger log = Logger.getLogger(HOwnershiptypeHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public HOwnershiptype findById(String id) {
		log.debug("getting HOwnershiptype instance with id: " + id);
		try {
			HOwnershiptype instance = entityManager.find(HOwnershiptype.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public HOwnershiptype merge(HOwnershiptype detachedInstance) {
		log.debug("merging HOwnershiptype instance");
		try {
			HOwnershiptype result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(HOwnershiptype transientInstance) {
		log.debug("persisting HOwnershiptype instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(HOwnershiptype persistentInstance) {
		log.debug("removing HOwnershiptype instance");
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
