package it.sinergis.gsc.common;

import java.util.List;

public class Utils {
	
	/**
	 * Porzione della query che si occupa di moltiplicare i vari energyamountEAmount delle diverse sorgenti per il
	 * relativo fattore di conversione in KWH.
	 * 
	 * @param queryBuilder
	 *            string builder della query
	 */
	public static void calculateSumEnergyAmount(StringBuilder queryBuilder) {
		queryBuilder.append("CASE ");
		
		queryBuilder
		        .append("WHEN LOWER(b.id.energyamountESource) = LOWER('naturalGas') THEN (b.id.energyamountEAmount * 9.781) ");
		
		queryBuilder
		        .append("WHEN LOWER(b.id.energyamountESource) = LOWER('liquidPropaneGas') THEN (b.id.energyamountEAmount * 6.520) ");
		
		queryBuilder
		        .append("WHEN LOWER(b.id.energyamountESource) = LOWER('diesel') THEN (b.id.energyamountEAmount * 10.960) ");
		
		queryBuilder.append("ELSE b.id.energyamountEAmount ");
		
		queryBuilder.append("END");
	}
	
	/**
	 * Definisce la condizione della query che specifica quali edifici o impianti (in base all'uuid) devono essere
	 * considerati. E' necessario splittare la lista in più blocchi per evitare uno StackOverflowError dovuto ad un
	 * grande numero di uuid inserito all'interno della clausola IN.
	 * 
	 * @param listaUuid
	 *            lista degli uuid degli edifici o degli impianti selezionati
	 * @param block
	 *            indica il numero del blocco di uuid in questione
	 * @param queryBuilder
	 *            string builder della query
	 * @param entityBuildingsInstallations
	 *            booleano per indicare se b è l'entity Buildings (o Installations) o meno
	 */
	public static void createCondition(List<String> listaUuid, int block, StringBuilder queryBuilder,
	        boolean entityBuildingsInstallations) {
		int limit = block * 3000;
		if (entityBuildingsInstallations) {
			queryBuilder.append(" b.uuid IN (");
		}
		else {
			queryBuilder.append(" b.id.uuid IN (");
		}
		
		for (int i = (limit - 3000); i < (listaUuid.size() > limit ? limit : listaUuid.size()); i++) {
			queryBuilder.append("'");
			queryBuilder.append(listaUuid.get(i));
			queryBuilder.append("'");
			queryBuilder.append(",");
		}
		queryBuilder.deleteCharAt(queryBuilder.length() - 1);
		queryBuilder.append(") ");
		
		if (listaUuid.size() > limit) {
			queryBuilder.append("OR");
			createCondition(listaUuid, block + 1, queryBuilder, entityBuildingsInstallations);
		}
	}
}