package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class InstallationElevationT.
 * 
 * @see it.sinergis.gsc.model.InstallationElevationT
 * @author Hibernate Tools
 */
public class InstallationElevationTHome {
	
	private static final Logger log = Logger.getLogger(InstallationElevationTHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public InstallationElevationT findById(InstallationElevationTId id) {
		log.debug("getting InstallationElevationT instance with id: " + id);
		try {
			InstallationElevationT instance = entityManager.find(InstallationElevationT.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public InstallationElevationT merge(InstallationElevationT detachedInstance) {
		log.debug("merging InstallationElevationT instance");
		try {
			InstallationElevationT result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(InstallationElevationT transientInstance) {
		log.debug("persisting InstallationElevationT instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(InstallationElevationT persistentInstance) {
		log.debug("removing InstallationElevationT instance");
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
