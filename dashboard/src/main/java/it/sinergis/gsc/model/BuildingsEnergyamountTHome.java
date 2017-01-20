package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class BuildingsEnergyamountT.
 * 
 * @see it.sinergis.gsc.model.BuildingsEnergyamountT
 * @author Hibernate Tools
 */
public class BuildingsEnergyamountTHome {
	
	private static final Logger log = Logger.getLogger(BuildingsEnergyamountTHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public BuildingsEnergyamountT findById(BuildingsEnergyamountTId id) {
		log.debug("getting BuildingsEnergyamountT instance with id: " + id);
		try {
			BuildingsEnergyamountT instance = entityManager.find(BuildingsEnergyamountT.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public BuildingsEnergyamountT merge(BuildingsEnergyamountT detachedInstance) {
		log.debug("merging BuildingsEnergyamountT instance");
		try {
			BuildingsEnergyamountT result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(BuildingsEnergyamountT transientInstance) {
		log.debug("persisting BuildingsEnergyamountT instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(BuildingsEnergyamountT persistentInstance) {
		log.debug("removing BuildingsEnergyamountT instance");
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
