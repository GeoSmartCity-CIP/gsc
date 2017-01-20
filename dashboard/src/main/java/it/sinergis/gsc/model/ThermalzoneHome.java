package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class Thermalzone.
 * 
 * @see it.sinergis.gsc.model.Thermalzone
 * @author Hibernate Tools
 */
public class ThermalzoneHome {
	
	private static final Logger log = Logger.getLogger(ThermalzoneHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public Thermalzone findById(String id) {
		log.debug("getting Thermalzone instance with id: " + id);
		try {
			Thermalzone instance = entityManager.find(Thermalzone.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Thermalzone merge(Thermalzone detachedInstance) {
		log.debug("merging Thermalzone instance");
		try {
			Thermalzone result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(Thermalzone transientInstance) {
		log.debug("persisting Thermalzone instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(Thermalzone persistentInstance) {
		log.debug("removing Thermalzone instance");
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
