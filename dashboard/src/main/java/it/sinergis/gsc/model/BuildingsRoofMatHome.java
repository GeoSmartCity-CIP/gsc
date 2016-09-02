package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class BuildingsRoofMat.
 * 
 * @see it.sinergis.gsc.model.BuildingsRoofMat
 * @author Hibernate Tools
 */
public class BuildingsRoofMatHome {
	
	private static final Logger log = Logger.getLogger(BuildingsRoofMatHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public BuildingsRoofMat findById(BuildingsRoofMatId id) {
		log.debug("getting BuildingsRoofMat instance with id: " + id);
		try {
			BuildingsRoofMat instance = entityManager.find(BuildingsRoofMat.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public BuildingsRoofMat merge(BuildingsRoofMat detachedInstance) {
		log.debug("merging BuildingsRoofMat instance");
		try {
			BuildingsRoofMat result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(BuildingsRoofMat transientInstance) {
		log.debug("persisting BuildingsRoofMat instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(BuildingsRoofMat persistentInstance) {
		log.debug("removing BuildingsRoofMat instance");
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
