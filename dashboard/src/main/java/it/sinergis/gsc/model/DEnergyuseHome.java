package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DEnergyuse.
 * 
 * @see it.sinergis.gsc.model.DEnergyuse
 * @author Hibernate Tools
 */
public class DEnergyuseHome {
	
	private static final Logger log = Logger.getLogger(DEnergyuseHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DEnergyuse findById(String id) {
		log.debug("getting DEnergyuse instance with id: " + id);
		try {
			DEnergyuse instance = entityManager.find(DEnergyuse.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DEnergyuse merge(DEnergyuse detachedInstance) {
		log.debug("merging DEnergyuse instance");
		try {
			DEnergyuse result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DEnergyuse transientInstance) {
		log.debug("persisting DEnergyuse instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DEnergyuse persistentInstance) {
		log.debug("removing DEnergyuse instance");
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
