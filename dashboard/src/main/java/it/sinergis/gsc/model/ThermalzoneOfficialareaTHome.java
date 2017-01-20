package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class ThermalzoneOfficialareaT.
 * 
 * @see it.sinergis.gsc.model.ThermalzoneOfficialareaT
 * @author Hibernate Tools
 */
public class ThermalzoneOfficialareaTHome {
	
	private static final Logger log = Logger.getLogger(ThermalzoneOfficialareaTHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public ThermalzoneOfficialareaT findById(ThermalzoneOfficialareaTId id) {
		log.debug("getting ThermalzoneOfficialareaT instance with id: " + id);
		try {
			ThermalzoneOfficialareaT instance = entityManager.find(ThermalzoneOfficialareaT.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public ThermalzoneOfficialareaT merge(ThermalzoneOfficialareaT detachedInstance) {
		log.debug("merging ThermalzoneOfficialareaT instance");
		try {
			ThermalzoneOfficialareaT result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(ThermalzoneOfficialareaT transientInstance) {
		log.debug("persisting ThermalzoneOfficialareaT instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(ThermalzoneOfficialareaT persistentInstance) {
		log.debug("removing ThermalzoneOfficialareaT instance");
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
