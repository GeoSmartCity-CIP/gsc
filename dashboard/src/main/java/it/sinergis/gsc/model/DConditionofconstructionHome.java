package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DConditionofconstruction.
 * 
 * @see it.sinergis.gsc.model.DConditionofconstruction
 * @author Hibernate Tools
 */
public class DConditionofconstructionHome {
	
	private static final Logger log = Logger.getLogger(DConditionofconstructionHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DConditionofconstruction findById(String id) {
		log.debug("getting DConditionofconstruction instance with id: " + id);
		try {
			DConditionofconstruction instance = entityManager.find(DConditionofconstruction.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DConditionofconstruction merge(DConditionofconstruction detachedInstance) {
		log.debug("merging DConditionofconstruction instance");
		try {
			DConditionofconstruction result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DConditionofconstruction transientInstance) {
		log.debug("persisting DConditionofconstruction instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DConditionofconstruction persistentInstance) {
		log.debug("removing DConditionofconstruction instance");
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
