package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class Conversion.
 * 
 * @see it.sinergis.gsc.model.Conversion
 * @author Hibernate Tools
 */
public class ConversionHome {
	
	private static final Logger log = Logger.getLogger(ConversionHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public Conversion findById(String id) {
		log.debug("getting Conversion instance with id: " + id);
		try {
			Conversion instance = entityManager.find(Conversion.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Conversion merge(Conversion detachedInstance) {
		log.debug("merging Conversion instance");
		try {
			Conversion result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(Conversion transientInstance) {
		log.debug("persisting Conversion instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(Conversion persistentInstance) {
		log.debug("removing Conversion instance");
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
