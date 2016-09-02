package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DMaterial.
 * 
 * @see it.sinergis.gsc.model.DMaterial
 * @author Hibernate Tools
 */
public class DMaterialHome {
	
	private static final Logger log = Logger.getLogger(DMaterialHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DMaterial findById(String id) {
		log.debug("getting DMaterial instance with id: " + id);
		try {
			DMaterial instance = entityManager.find(DMaterial.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DMaterial merge(DMaterial detachedInstance) {
		log.debug("merging DMaterial instance");
		try {
			DMaterial result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DMaterial transientInstance) {
		log.debug("persisting DMaterial instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DMaterial persistentInstance) {
		log.debug("removing DMaterial instance");
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
