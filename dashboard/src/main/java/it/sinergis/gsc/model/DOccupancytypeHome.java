package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DOccupancytype.
 * 
 * @see it.sinergis.gsc.model.DOccupancytype
 * @author Hibernate Tools
 */
public class DOccupancytypeHome {
	
	private static final Logger log = Logger.getLogger(DOccupancytypeHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DOccupancytype findById(String id) {
		log.debug("getting DOccupancytype instance with id: " + id);
		try {
			DOccupancytype instance = entityManager.find(DOccupancytype.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DOccupancytype merge(DOccupancytype detachedInstance) {
		log.debug("merging DOccupancytype instance");
		try {
			DOccupancytype result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DOccupancytype transientInstance) {
		log.debug("persisting DOccupancytype instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DOccupancytype persistentInstance) {
		log.debug("removing DOccupancytype instance");
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
