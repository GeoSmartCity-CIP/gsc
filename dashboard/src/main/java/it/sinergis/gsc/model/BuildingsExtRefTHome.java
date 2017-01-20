package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class BuildingsExtRefT.
 * 
 * @see it.sinergis.gsc.model.BuildingsExtRefT
 * @author Hibernate Tools
 */
public class BuildingsExtRefTHome {
	
	private static final Logger log = Logger.getLogger(BuildingsExtRefTHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public BuildingsExtRefT findById(BuildingsExtRefTId id) {
		log.debug("getting BuildingsExtRefT instance with id: " + id);
		try {
			BuildingsExtRefT instance = entityManager.find(BuildingsExtRefT.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public BuildingsExtRefT merge(BuildingsExtRefT detachedInstance) {
		log.debug("merging BuildingsExtRefT instance");
		try {
			BuildingsExtRefT result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(BuildingsExtRefT transientInstance) {
		log.debug("persisting BuildingsExtRefT instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(BuildingsExtRefT persistentInstance) {
		log.debug("removing BuildingsExtRefT instance");
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
