package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DHeightstatus.
 * 
 * @see it.sinergis.gsc.model.DHeightstatus
 * @author Hibernate Tools
 */
public class DHeightstatusHome {
	
	private static final Logger log = Logger.getLogger(DHeightstatusHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DHeightstatus findById(String id) {
		log.debug("getting DHeightstatus instance with id: " + id);
		try {
			DHeightstatus instance = entityManager.find(DHeightstatus.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DHeightstatus merge(DHeightstatus detachedInstance) {
		log.debug("merging DHeightstatus instance");
		try {
			DHeightstatus result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DHeightstatus transientInstance) {
		log.debug("persisting DHeightstatus instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DHeightstatus persistentInstance) {
		log.debug("removing DHeightstatus instance");
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
