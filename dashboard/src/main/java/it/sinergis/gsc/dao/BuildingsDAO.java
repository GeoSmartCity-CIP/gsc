package it.sinergis.gsc.dao;

import it.sinergis.gsc.common.Constants;
import it.sinergis.gsc.common.Utils;
import it.sinergis.gsc.model.Buildings;
import it.sinergis.gsc.model.BuildingsHome;
import it.sinergis.gsc.persistence.PersistenceManager;

import java.util.List;

import javax.persistence.Query;

/**
 * Class that extends home object for domain model class Buildings.
 * 
 * @see it.sinergis.gsc.model.Buildings
 */
public class BuildingsDAO extends BuildingsHome {
	
	/**
	 * Query che recupera i consumi di ogni edificio raggruppati per anno. La tipologia di consumo è indicata nel
	 * parametro graphicDataType. I valori dei consumi estratti dal db vengono moltiplicati per un fattore di
	 * conversione in modo da ottenere i KWH corrispondenti.
	 * 
	 * @param listaEdifici
	 *            identificativi degli edifici
	 * @param graphicDataType
	 *            tipologia di consumo
	 * @return record consumi
	 */
	public List<Object> recuperaConsumiPerDashboard(List<String> listaEdifici, String graphicDataType) {
		try {
			entityManager = PersistenceManager.getInstance().getEntityManager();
			
			StringBuilder queryBuilder = new StringBuilder();
			
			queryBuilder.append("SELECT b.id.energyamountEYear, ROUND(SUM(");
			
			Utils.calculateSumEnergyAmount(queryBuilder);
			
			queryBuilder.append("), 3), b.buildings.volumeValue, b.id.uuid ");
			
			queryBuilder.append("FROM BuildingsEnergyamountT b ");
			
			queryBuilder.append("WHERE");
			
			Utils.createCondition(listaEdifici, 1, queryBuilder, false);
			
			if (graphicDataType.equalsIgnoreCase(Constants.TERMICO)) {
				queryBuilder
				        .append("AND (LOWER(b.id.energyamountESource) = LOWER('warmWaterOrStream') OR LOWER(b.id.energyamountESource) = LOWER('naturalGas') ");
				
				queryBuilder
				        .append("OR LOWER(b.id.energyamountESource) = LOWER('liquidPropaneGas') OR LOWER(b.id.energyamountESource) = LOWER('diesel')) ");
			}
			else if (graphicDataType.equalsIgnoreCase(Constants.ELETTRICO)) {
				queryBuilder.append("AND LOWER(b.id.energyamountESource) = LOWER('electricity') ");
			}
			
			queryBuilder.append("GROUP BY b.id.energyamountEYear, b.buildings.volumeValue, b.id.uuid ");
			
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
	 * Query che recupera gli oggetti Buildings corrispondenti agli identificativi passati come parametro in una lista.
	 * 
	 * @param listaEdifici
	 *            identificativi degli edifici
	 * @return lista di oggetti Buildings
	 */
	public List<Buildings> recuperaEdifici(List<String> listaEdifici) {
		try {
			entityManager = PersistenceManager.getInstance().getEntityManager();
			
			StringBuilder queryBuilder = new StringBuilder();
			
			queryBuilder.append("SELECT b FROM Buildings b WHERE");
			
			Utils.createCondition(listaEdifici, 1, queryBuilder, true);
			
			queryBuilder.append("ORDER BY b.uuid");
			
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