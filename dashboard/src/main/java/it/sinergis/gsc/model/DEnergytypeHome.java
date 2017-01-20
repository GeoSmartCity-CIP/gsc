package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DEnergytype.
 * 
 * @see it.sinergis.gsc.model.DEnergytype
 * @author Hibernate Tools
 */
public class DEnergytypeHome {
	
	private static final Logger log = Logger.getLogger(DEnergytypeHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DEnergytype findById(String id) {
		log.debug("getting DEnergytype instance with id: " + id);
		try {
			DEnergytype instance = entityManager.find(DEnergytype.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DEnergytype merge(DEnergytype detachedInstance) {
		log.debug("merging DEnergytype instance");
		try {
			DEnergytype result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DEnergytype transientInstance) {
		log.debug("persisting DEnergytype instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DEnergytype persistentInstance) {
		log.debug("removing DEnergytype instance");
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
