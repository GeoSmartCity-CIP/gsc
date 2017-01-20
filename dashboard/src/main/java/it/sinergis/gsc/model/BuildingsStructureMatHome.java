package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class BuildingsStructureMat.
 * 
 * @see it.sinergis.gsc.model.BuildingsStructureMat
 * @author Hibernate Tools
 */
public class BuildingsStructureMatHome {
	
	private static final Logger log = Logger.getLogger(BuildingsStructureMatHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public BuildingsStructureMat findById(BuildingsStructureMatId id) {
		log.debug("getting BuildingsStructureMat instance with id: " + id);
		try {
			BuildingsStructureMat instance = entityManager.find(BuildingsStructureMat.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public BuildingsStructureMat merge(BuildingsStructureMat detachedInstance) {
		log.debug("merging BuildingsStructureMat instance");
		try {
			BuildingsStructureMat result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(BuildingsStructureMat transientInstance) {
		log.debug("persisting BuildingsStructureMat instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(BuildingsStructureMat persistentInstance) {
		log.debug("removing BuildingsStructureMat instance");
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
