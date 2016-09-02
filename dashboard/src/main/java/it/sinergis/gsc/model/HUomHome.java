package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class HUom.
 * 
 * @see it.sinergis.gsc.model.HUom
 * @author Hibernate Tools
 */
public class HUomHome {
	
	private static final Logger log = Logger.getLogger(HUomHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public HUom findById(String id) {
		log.debug("getting HUom instance with id: " + id);
		try {
			HUom instance = entityManager.find(HUom.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public HUom merge(HUom detachedInstance) {
		log.debug("merging HUom instance");
		try {
			HUom result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(HUom transientInstance) {
		log.debug("persisting HUom instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(HUom persistentInstance) {
		log.debug("removing HUom instance");
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
