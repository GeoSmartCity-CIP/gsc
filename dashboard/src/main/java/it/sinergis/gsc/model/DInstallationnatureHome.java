package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DInstallationnature.
 * 
 * @see it.sinergis.gsc.model.DInstallationnature
 * @author Hibernate Tools
 */
public class DInstallationnatureHome {
	
	private static final Logger log = Logger.getLogger(DInstallationnatureHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DInstallationnature findById(String id) {
		log.debug("getting DInstallationnature instance with id: " + id);
		try {
			DInstallationnature instance = entityManager.find(DInstallationnature.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DInstallationnature merge(DInstallationnature detachedInstance) {
		log.debug("merging DInstallationnature instance");
		try {
			DInstallationnature result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DInstallationnature transientInstance) {
		log.debug("persisting DInstallationnature instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DInstallationnature persistentInstance) {
		log.debug("removing DInstallationnature instance");
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
