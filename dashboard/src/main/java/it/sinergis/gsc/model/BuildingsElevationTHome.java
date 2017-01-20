package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class BuildingsElevationT.
 * 
 * @see it.sinergis.gsc.model.BuildingsElevationT
 * @author Hibernate Tools
 */
public class BuildingsElevationTHome {
	
	private static final Logger log = Logger.getLogger(BuildingsElevationTHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public BuildingsElevationT findById(BuildingsElevationTId id) {
		log.debug("getting BuildingsElevationT instance with id: " + id);
		try {
			BuildingsElevationT instance = entityManager.find(BuildingsElevationT.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public BuildingsElevationT merge(BuildingsElevationT detachedInstance) {
		log.debug("merging BuildingsElevationT instance");
		try {
			BuildingsElevationT result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(BuildingsElevationT transientInstance) {
		log.debug("persisting BuildingsElevationT instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(BuildingsElevationT persistentInstance) {
		log.debug("removing BuildingsElevationT instance");
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
