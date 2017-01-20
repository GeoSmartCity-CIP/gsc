package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DBuildingtype.
 * 
 * @see it.sinergis.gsc.model.DBuildingtype
 * @author Hibernate Tools
 */
public class DBuildingtypeHome {
	
	private static final Logger log = Logger.getLogger(DBuildingtypeHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DBuildingtype findById(String id) {
		log.debug("getting DBuildingtype instance with id: " + id);
		try {
			DBuildingtype instance = entityManager.find(DBuildingtype.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DBuildingtype merge(DBuildingtype detachedInstance) {
		log.debug("merging DBuildingtype instance");
		try {
			DBuildingtype result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DBuildingtype transientInstance) {
		log.debug("persisting DBuildingtype instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DBuildingtype persistentInstance) {
		log.debug("removing DBuildingtype instance");
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
