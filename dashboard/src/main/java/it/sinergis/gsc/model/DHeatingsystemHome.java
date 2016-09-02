package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DHeatingsystem.
 * 
 * @see it.sinergis.gsc.model.DHeatingsystem
 * @author Hibernate Tools
 */
public class DHeatingsystemHome {
	
	private static final Logger log = Logger.getLogger(DHeatingsystemHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DHeatingsystem findById(String id) {
		log.debug("getting DHeatingsystem instance with id: " + id);
		try {
			DHeatingsystem instance = entityManager.find(DHeatingsystem.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DHeatingsystem merge(DHeatingsystem detachedInstance) {
		log.debug("merging DHeatingsystem instance");
		try {
			DHeatingsystem result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DHeatingsystem transientInstance) {
		log.debug("persisting DHeatingsystem instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DHeatingsystem persistentInstance) {
		log.debug("removing DHeatingsystem instance");
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
