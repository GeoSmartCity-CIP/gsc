package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class HEnergysource.
 * 
 * @see it.sinergis.gsc.model.HEnergysource
 * @author Hibernate Tools
 */
public class HEnergysourceHome {
	
	private static final Logger log = Logger.getLogger(HEnergysourceHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public HEnergysource findById(String id) {
		log.debug("getting HEnergysource instance with id: " + id);
		try {
			HEnergysource instance = entityManager.find(HEnergysource.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public HEnergysource merge(HEnergysource detachedInstance) {
		log.debug("merging HEnergysource instance");
		try {
			HEnergysource result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(HEnergysource transientInstance) {
		log.debug("persisting HEnergysource instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(HEnergysource persistentInstance) {
		log.debug("removing HEnergysource instance");
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
