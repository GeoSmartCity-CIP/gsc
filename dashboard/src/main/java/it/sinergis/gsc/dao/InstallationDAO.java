package it.sinergis.gsc.dao;

import it.sinergis.gsc.common.Utils;
import it.sinergis.gsc.model.Installation;
import it.sinergis.gsc.model.InstallationHome;
import it.sinergis.gsc.persistence.PersistenceManager;

import java.util.List;

import javax.persistence.Query;

/**
 * Class that extends home object for domain model class Installation.
 * 
 * @see it.sinergis.gsc.model.Installation
 */
public class InstallationDAO extends InstallationHome {
	
	/**
	 * Query che recupera i consumi di ogni impianto raggruppati per anno. I valori dei consumi estratti dal db vengono
	 * moltiplicati per un fattore di conversione in modo da ottenere i KWH corrispondenti.
	 * 
	 * @param listaEdifici
	 *            identificativi degli impianti
	 * @return record consumi
	 */
	public List<Object> recuperaConsumiInstallationPerDashboard(List<String> listaInstallation) {
		try {
			entityManager = PersistenceManager.getInstance().getEntityManager();
			
			StringBuilder queryBuilder = new StringBuilder();
			
			queryBuilder.append("SELECT b.id.energyamountEYear, ROUND(SUM(");
			
			Utils.calculateSumEnergyAmount(queryBuilder);
			
			queryBuilder.append("), 3), b.id.uuid ");
			
			queryBuilder.append("FROM ThermalzoneEnergyamountT b ");
			
			queryBuilder.append("WHERE");
			
			Utils.createCondition(listaInstallation, 1, queryBuilder, false);
			
			queryBuilder.append("GROUP BY b.id.energyamountEYear, b.id.uuid ");
			
			queryBuilder.append("ORDER BY b.id.energyamountEYear");
			
			Query query = entityManager.createQuery(queryBuilder.toString());
			
			return query.getResultList();
		}
		finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
	
	/**
	 * Query che recupera gli oggetti Installations corrispondenti agli identificativi passati come parametro in una
	 * lista.
	 * 
	 * @param listaEdifici
	 *            identificativi degli impianti
	 * @return lista di oggetti Installations
	 */
	public List<Installation> recuperaKeyThermalzone(List<String> listaInstallation) {
		try {
			entityManager = PersistenceManager.getInstance().getEntityManager();
			
			StringBuilder queryBuilder = new StringBuilder();
			
			queryBuilder.append("SELECT b FROM Installation b WHERE");
			
			Utils.createCondition(listaInstallation, 1, queryBuilder, true);
			
			Query query = entityManager.createQuery(queryBuilder.toString());
			
			return query.getResultList();
		}
		finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
}