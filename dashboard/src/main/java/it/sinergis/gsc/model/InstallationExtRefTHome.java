package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class InstallationExtRefT.
 * 
 * @see it.sinergis.gsc.model.InstallationExtRefT
 * @author Hibernate Tools
 */
public class InstallationExtRefTHome {
	
	private static final Logger log = Logger.getLogger(InstallationExtRefTHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public InstallationExtRefT findById(InstallationExtRefTId id) {
		log.debug("getting InstallationExtRefT instance with id: " + id);
		try {
			InstallationExtRefT instance = entityManager.find(InstallationExtRefT.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public InstallationExtRefT merge(InstallationExtRefT detachedInstance) {
		log.debug("merging InstallationExtRefT instance");
		try {
			InstallationExtRefT result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(InstallationExtRefT transientInstance) {
		log.debug("persisting InstallationExtRefT instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(InstallationExtRefT persistentInstance) {
		log.debug("removing InstallationExtRefT instance");
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
