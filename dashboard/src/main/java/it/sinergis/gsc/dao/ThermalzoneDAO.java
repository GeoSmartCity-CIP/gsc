package it.sinergis.gsc.dao;

import it.sinergis.gsc.exceptions.GSCServiceException;
import it.sinergis.gsc.model.Thermalzone;
import it.sinergis.gsc.model.ThermalzoneHome;
import it.sinergis.gsc.persistence.PersistenceManager;

import java.util.List;

import javax.persistence.Query;

/**
 * Class that extends home object for domain model class Thermalzone.
 * 
 * @see it.sinergis.gsc.model.Thermalzone
 */
public class ThermalzoneDAO extends ThermalzoneHome {
	
	/**
	 * Esegue la query su DB per recuperare i certificati dell'edificio il cui identificatore è passato come parametro.
	 * 
	 * @param uuidBuilding
	 *            identificativo edificio
	 * @param pr
	 *            property reader per il file error_messages.properties
	 * @return
	 * @throws GSCServiceException
	 */
	public List<Thermalzone> recuperaCertificatiEdificio(String uuidBuilding) {
		
		try {
			entityManager = PersistenceManager.getInstance().getEntityManager();
			
			StringBuilder queryBuilder = new StringBuilder();
			
			queryBuilder
			        .append("SELECT t FROM Thermalzone t WHERE t.energyperformancePerfValue IS NOT NULL AND t.buildings.uuid = :uuid");
			
			Query query = entityManager.createQuery(queryBuilder.toString()).setParameter("uuid", uuidBuilding);
			
			return query.getResultList();
		}
		finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
}