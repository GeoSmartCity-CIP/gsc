package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class BuildingsUseMT.
 * 
 * @see it.sinergis.gsc.model.BuildingsUseMT
 * @author Hibernate Tools
 */
public class BuildingsUseMTHome {
	
	private static final Logger log = Logger.getLogger(BuildingsUseMTHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public BuildingsUseMT findById(BuildingsUseMTId id) {
		log.debug("getting BuildingsUseMT instance with id: " + id);
		try {
			BuildingsUseMT instance = entityManager.find(BuildingsUseMT.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public BuildingsUseMT merge(BuildingsUseMT detachedInstance) {
		log.debug("merging BuildingsUseMT instance");
		try {
			BuildingsUseMT result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(BuildingsUseMT transientInstance) {
		log.debug("persisting BuildingsUseMT instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(BuildingsUseMT persistentInstance) {
		log.debug("removing BuildingsUseMT instance");
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
