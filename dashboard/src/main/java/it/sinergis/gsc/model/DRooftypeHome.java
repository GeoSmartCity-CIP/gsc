package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DRooftype.
 * 
 * @see it.sinergis.gsc.model.DRooftype
 * @author Hibernate Tools
 */
public class DRooftypeHome {
	
	private static final Logger log = Logger.getLogger(DRooftypeHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DRooftype findById(String id) {
		log.debug("getting DRooftype instance with id: " + id);
		try {
			DRooftype instance = entityManager.find(DRooftype.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DRooftype merge(DRooftype detachedInstance) {
		log.debug("merging DRooftype instance");
		try {
			DRooftype result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DRooftype transientInstance) {
		log.debug("persisting DRooftype instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DRooftype persistentInstance) {
		log.debug("removing DRooftype instance");
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
