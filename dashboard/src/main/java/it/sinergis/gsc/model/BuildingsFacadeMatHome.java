package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class BuildingsFacadeMat.
 * 
 * @see it.sinergis.gsc.model.BuildingsFacadeMat
 * @author Hibernate Tools
 */
public class BuildingsFacadeMatHome {
	
	private static final Logger log = Logger.getLogger(BuildingsFacadeMatHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public BuildingsFacadeMat findById(BuildingsFacadeMatId id) {
		log.debug("getting BuildingsFacadeMat instance with id: " + id);
		try {
			BuildingsFacadeMat instance = entityManager.find(BuildingsFacadeMat.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public BuildingsFacadeMat merge(BuildingsFacadeMat detachedInstance) {
		log.debug("merging BuildingsFacadeMat instance");
		try {
			BuildingsFacadeMat result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(BuildingsFacadeMat transientInstance) {
		log.debug("persisting BuildingsFacadeMat instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(BuildingsFacadeMat persistentInstance) {
		log.debug("removing BuildingsFacadeMat instance");
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
