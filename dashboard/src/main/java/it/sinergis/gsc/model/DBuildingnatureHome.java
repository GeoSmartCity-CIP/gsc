package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DBuildingnature.
 * 
 * @see it.sinergis.gsc.model.DBuildingnature
 * @author Hibernate Tools
 */
public class DBuildingnatureHome {
	
	private static final Logger log = Logger.getLogger(DBuildingnatureHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DBuildingnature findById(String id) {
		log.debug("getting DBuildingnature instance with id: " + id);
		try {
			DBuildingnature instance = entityManager.find(DBuildingnature.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DBuildingnature merge(DBuildingnature detachedInstance) {
		log.debug("merging DBuildingnature instance");
		try {
			DBuildingnature result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DBuildingnature transientInstance) {
		log.debug("persisting DBuildingnature instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DBuildingnature persistentInstance) {
		log.debug("removing DBuildingnature instance");
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
