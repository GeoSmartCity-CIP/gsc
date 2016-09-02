package it.sinergis.gsc.model;

// Generated Aug 5, 2016 5:19:18 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class ThermalzoneHeatingsource.
 * 
 * @see it.sinergis.gsc.model.ThermalzoneHeatingsource
 * @author Hibernate Tools
 */
public class ThermalzoneHeatingsourceHome {
	
	private static final Logger log = Logger.getLogger(ThermalzoneHeatingsourceHome.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public ThermalzoneHeatingsource findById(ThermalzoneHeatingsourceId id) {
		log.debug("getting ThermalzoneHeatingsource instance with id: " + id);
		try {
			ThermalzoneHeatingsource instance = entityManager.find(ThermalzoneHeatingsource.class, id);
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public ThermalzoneHeatingsource merge(ThermalzoneHeatingsource detachedInstance) {
		log.debug("merging ThermalzoneHeatingsource instance");
		try {
			ThermalzoneHeatingsource result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		}
		catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void persist(ThermalzoneHeatingsource transientInstance) {
		log.debug("persisting ThermalzoneHeatingsource instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		}
		catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void remove(ThermalzoneHeatingsource persistentInstance) {
		log.debug("removing ThermalzoneHeatingsource instance");
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
