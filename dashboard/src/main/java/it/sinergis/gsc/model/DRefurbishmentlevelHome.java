package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class DRefurbishmentlevel.
 * 
 * @see it.sinergis.gsc.model.DRefurbishmentlevel
 * @author Hibernate Tools
 */
public class DRefurbishmentlevelHome {
	
	private static final Logger log = Logger.getLogger(DRefurbishmentlevelHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public DRefurbishmentlevel findById(String id) {
		log.debug("getting DRefurbishmentlevel instance with id: " + id);
		try {
			DRefurbishmentlevel instance = entityManager.find(DRefurbishmentlevel.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public DRefurbishmentlevel merge(DRefurbishmentlevel detachedInstance) {
		log.debug("merging DRefurbishmentlevel instance");
		try {
			DRefurbishmentlevel result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(DRefurbishmentlevel transientInstance) {
		log.debug("persisting DRefurbishmentlevel instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(DRefurbishmentlevel persistentInstance) {
		log.debug("removing DRefurbishmentlevel instance");
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
