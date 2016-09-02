package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DPerfClass.
 * 
 * @see it.sinergis.gsc.model.DPerfClass
 * @author Hibernate Tools
 */
public class DPerfClassHome {
	
	private static final Logger log = Logger.getLogger(DPerfClassHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DPerfClass findById(String id) {
		log.debug("getting DPerfClass instance with id: " + id);
		try {
			DPerfClass instance = entityManager.find(DPerfClass.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DPerfClass merge(DPerfClass detachedInstance) {
		log.debug("merging DPerfClass instance");
		try {
			DPerfClass result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DPerfClass transientInstance) {
		log.debug("persisting DPerfClass instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DPerfClass persistentInstance) {
		log.debug("removing DPerfClass instance");
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
