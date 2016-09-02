package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class BuildingsRenewablePotentiaT.
 * 
 * @see it.sinergis.gsc.model.BuildingsRenewablePotentiaT
 * @author Hibernate Tools
 */
public class BuildingsRenewablePotentiaTHome {
	
	private static final Logger log = Logger.getLogger(BuildingsRenewablePotentiaTHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public BuildingsRenewablePotentiaT findById(BuildingsRenewablePotentiaTId id) {
		log.debug("getting BuildingsRenewablePotentiaT instance with id: " + id);
		try {
			BuildingsRenewablePotentiaT instance = entityManager.find(BuildingsRenewablePotentiaT.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public BuildingsRenewablePotentiaT merge(BuildingsRenewablePotentiaT detachedInstance) {
		log.debug("merging BuildingsRenewablePotentiaT instance");
		try {
			BuildingsRenewablePotentiaT result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(BuildingsRenewablePotentiaT transientInstance) {
		log.debug("persisting BuildingsRenewablePotentiaT instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(BuildingsRenewablePotentiaT persistentInstance) {
		log.debug("removing BuildingsRenewablePotentiaT instance");
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
