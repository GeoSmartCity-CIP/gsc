package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class BuildingsEnergyPerformanceT.
 * 
 * @see it.sinergis.gsc.model.BuildingsEnergyPerformanceT
 * @author Hibernate Tools
 */
public class BuildingsEnergyPerformanceTHome {
	
	private static final Logger log = Logger.getLogger(BuildingsEnergyPerformanceTHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public BuildingsEnergyPerformanceT findById(BuildingsEnergyPerformanceTId id) {
		log.debug("getting BuildingsEnergyPerformanceT instance with id: " + id);
		try {
			BuildingsEnergyPerformanceT instance = entityManager.find(BuildingsEnergyPerformanceT.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public BuildingsEnergyPerformanceT merge(BuildingsEnergyPerformanceT detachedInstance) {
		log.debug("merging BuildingsEnergyPerformanceT instance");
		try {
			BuildingsEnergyPerformanceT result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(BuildingsEnergyPerformanceT transientInstance) {
		log.debug("persisting BuildingsEnergyPerformanceT instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(BuildingsEnergyPerformanceT persistentInstance) {
		log.debug("removing BuildingsEnergyPerformanceT instance");
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
