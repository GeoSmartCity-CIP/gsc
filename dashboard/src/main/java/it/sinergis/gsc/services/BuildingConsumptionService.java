package it.sinergis.gsc.services;

import it.sinergis.gsc.common.Constants;
import it.sinergis.gsc.common.PropertyReader;
import it.sinergis.gsc.dao.BuildingsDAO;
import it.sinergis.gsc.dao.InstallationDAO;
import it.sinergis.gsc.dao.ThermalzoneDAO;
import it.sinergis.gsc.exceptions.GSCServiceException;
import it.sinergis.gsc.model.Buildings;
import it.sinergis.gsc.model.BuildingsEnergyPerformanceT;
import it.sinergis.gsc.model.BuildingsEnergyamountT;
import it.sinergis.gsc.model.Installation;
import it.sinergis.gsc.model.Thermalzone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servizio per il recupero dei consumi degli edifici.
 * 
 * @author Giuseppe Giuffrida
 */
public class BuildingConsumptionService {
	
	/** Logger. */
	private static final Logger log = Logger.getLogger(BuildingConsumptionService.class);
	
	/** Object mapper. */
	private ObjectMapper om;
	
	/** Property reader. */
	private PropertyReader pr;
	
	/** Costruttore. */
	public BuildingConsumptionService() {
		om = new ObjectMapper();
		pr = new PropertyReader("error_messages.properties");
	}
	
	public PropertyReader getPr() {
		return pr;
	}
	
	/**
	 * Restituisce un JSON da inviare in risposta alla chiamata alla servlet con le informazioni sui certificati di un
	 * edificio o un messaggio di errore.
	 * 
	 * @param jsonParameter
	 *            JSON in input in formato stringa
	 * @return JSON di risposta in formato stringa
	 */
	public String recuperaJSONCertificatiEdificio(String jsonParameter) {
		
		try {
			ThermalzoneDAO thermalzoneDAO = new ThermalzoneDAO();
			
			Map<String, Object> mapParametri = estraiParametri(jsonParameter);
			
			List<Thermalzone> listaCert = thermalzoneDAO.recuperaCertificatiEdificio(((List<String>) mapParametri
			        .get(Constants.JSON_UUID_FIELD)).get(0));
			
			return buildJsonForCertificates(listaCert);
		}
		catch (GSCServiceException gsc) {
			log.error("Error in the method recuperaCertificatiEdificio", gsc);
			return buildJsonMessageResponse(true, gsc.getErrorDescription());
		}
	}
	
	/**
	 * Restituisce un JSON da inviare in risposta alla chiamata alla servlet con le informazioni sulle classi
	 * energetiche degli edifici indicati nel JSON in input o un messaggio di errore.
	 * 
	 * @param jsonParameter
	 *            JSON in input in formato stringa
	 * @return JSON di risposta in formato stringa
	 */
	public String recuperaJSONClassiEdifici(String jsonParameter) {
		try {
			BuildingsDAO buildingsDAO = new BuildingsDAO();
			
			Map<String, Object> mapParametri = estraiParametri(jsonParameter);
			
			List<Buildings> listaBuildings = null;
			
			if (!((List<String>) mapParametri.get(Constants.JSON_UUID_FIELD)).isEmpty()) {
				listaBuildings = buildingsDAO.recuperaEdifici((List<String>) mapParametri
				        .get(Constants.JSON_UUID_FIELD));
			}
			else {
				listaBuildings = new ArrayList<Buildings>();
			}
			
			return buildJsonClassiEdifici(listaBuildings,
			        ((List<String>) mapParametri.get(Constants.JSON_UUID_FIELD)).size());
		}
		catch (GSCServiceException gsc) {
			log.error("Error in the method recuperaJSONClassiEdifici", gsc);
			return buildJsonMessageResponse(true, gsc.getErrorDescription());
		}
	}
	
	/**
	 * Restituisce un JSON da inviare in risposta alla chiamata alla servlet con le informazioni sui consumi degli
	 * edifici indicati nel JSON in input o un messaggio di errore.
	 * 
	 * @param jsonParameter
	 *            JSON in input in formato stringa
	 * @return JSON di risposta in formato stringa
	 */
	public String recuperaJSONConsumiBuildingsPerDashboard(String jsonParameter, boolean singleBuilding) {
		try {
			BuildingsDAO buildingsDAO = new BuildingsDAO();
			
			Map<String, Object> mapParametri = estraiParametri(jsonParameter);
			
			List<Object> listaRecord = null;
			
			if ((String) mapParametri.get(Constants.GRAPHIC_DATA_TYPE) != null) {
				if (!((String) mapParametri.get(Constants.GRAPHIC_DATA_TYPE)).equalsIgnoreCase(Constants.ELETTRICO)
				        && !((String) mapParametri.get(Constants.GRAPHIC_DATA_TYPE))
				                .equalsIgnoreCase(Constants.TERMICO)
				        && !((String) mapParametri.get(Constants.GRAPHIC_DATA_TYPE)).equalsIgnoreCase(Constants.TOTALE)) {
					listaRecord = new ArrayList<Object>();
				}
				else {
					if (!((List<String>) mapParametri.get(Constants.JSON_UUID_FIELD)).isEmpty()) {
						listaRecord = buildingsDAO.recuperaConsumiPerDashboard(
						        (List<String>) mapParametri.get(Constants.JSON_UUID_FIELD),
						        (String) mapParametri.get(Constants.GRAPHIC_DATA_TYPE));
					}
					else {
						listaRecord = new ArrayList<Object>();
					}
				}
			}
			else {
				throw new GSCServiceException("Parameter graphicDataType absent",
				        pr.getValue(Constants.ERRORE_PARAMETRI_INPUT));
			}
			
			if (singleBuilding) {
				return buildJsonConsumiSingoloEdificio(listaRecord);
			}
			else {
				if ((String) mapParametri.get(Constants.SLD_TYPE) != null) {
					if ((String) mapParametri.get(Constants.VAL_SLIDER) != null) {
						Double arrayIntervalli[] = creaArrayIntervalliSld(
						        (String) mapParametri.get(Constants.GRAPHIC_DATA_TYPE),
						        (String) mapParametri.get(Constants.SLD_TYPE));
						return buildJsonConsumiEdificiSelezionati(listaRecord,
						        (String) mapParametri.get(Constants.VAL_SLIDER), arrayIntervalli,
						        ((List<String>) mapParametri.get(Constants.JSON_UUID_FIELD)).size());
					}
					else {
						throw new GSCServiceException("Parameter valSlider absent",
						        pr.getValue(Constants.ERRORE_PARAMETRI_INPUT));
					}
				}
				else {
					throw new GSCServiceException("Parameter sldType absent",
					        pr.getValue(Constants.ERRORE_PARAMETRI_INPUT));
				}
			}
		}
		catch (GSCServiceException gsc) {
			log.error("Error in the method recuperaJSONConsumiPerDashboard", gsc);
			return buildJsonMessageResponse(true, gsc.getErrorDescription());
		}
	}
	
	/**
	 * Restituisce un JSON da inviare in risposta alla chiamata alla servlet con le informazioni sui consumi degli
	 * impianti indicati nel JSON in input o un messaggio di errore.
	 * 
	 * @param jsonParameter
	 *            JSON in input in formato stringa
	 * @return JSON di risposta in formato stringa
	 */
	public String recuperaJSONConsumiInstallationPerDashboard(String jsonParameter, boolean singleBuilding) {
		try {
			InstallationDAO installationDAO = new InstallationDAO();
			
			Map<String, Object> mapParametri = estraiParametri(jsonParameter);
			
			Map<String, String> mapKey = new HashMap<String, String>();
			
			List<Object> listaRecord = null;
			
			if (!((List<String>) mapParametri.get(Constants.JSON_UUID_FIELD)).isEmpty()) {
				List<Installation> listaInstallation = installationDAO
				        .recuperaKeyThermalzone((List<String>) mapParametri.get(Constants.JSON_UUID_FIELD));
				
				if (!listaInstallation.isEmpty()) {
					for (Installation inst : listaInstallation) {
						mapKey.put(inst.getThermalzone().getUuid(), inst.getUuid());
					}
					
					listaRecord = installationDAO.recuperaConsumiInstallationPerDashboard(new ArrayList<String>(mapKey
					        .keySet()));
				}
				else {
					listaRecord = new ArrayList<Object>();
				}
			}
			else {
				listaRecord = new ArrayList<Object>();
			}
			
			if (singleBuilding) {
				return buildJsonConsumiSingoloImpianto(listaRecord);
			}
			else {
				if ((String) mapParametri.get(Constants.VAL_SLIDER) != null) {
					return buildJsonConsumiImpiantiSelezionati(listaRecord,
					        (String) mapParametri.get(Constants.VAL_SLIDER), mapKey);
				}
				else {
					throw new GSCServiceException("Parameter valSlider absent",
					        pr.getValue(Constants.ERRORE_PARAMETRI_INPUT));
				}
			}
		}
		catch (GSCServiceException gsc) {
			log.error("Error in the method recuperaJSONConsumiInstallationPerDashboard", gsc);
			return buildJsonMessageResponse(true, gsc.getErrorDescription());
		}
	}
	
	/**
	 * Restituisce un JSON da inviare in risposta alla chiamata alla servlet con le informazioni raggruppate per tipo di
	 * consumo o un messaggio di errore.
	 * 
	 * @param jsonParameter
	 *            JSON in input in formato stringa
	 * @return JSON di risposta in formato stringa
	 */
	public String recuperaJSONConsumiPerTipoDiConsumo(String jsonParameter) {
		
		try {
			BuildingsDAO buildingsDAO = new BuildingsDAO();
			
			Map<String, Object> mapParametri = estraiParametri(jsonParameter);
			
			List<Buildings> listaBuildings = null;
			
			if (!((List<String>) mapParametri.get(Constants.JSON_UUID_FIELD)).isEmpty()) {
				listaBuildings = buildingsDAO.recuperaEdifici((List<String>) mapParametri
				        .get(Constants.JSON_UUID_FIELD));
			}
			else {
				listaBuildings = new ArrayList<Buildings>();
			}
			
			return buildJsonForConsumptions(listaBuildings);
		}
		catch (GSCServiceException gsc) {
			log.error("Error in the method recuperaJSONConsumiPerTipoDiConsumo", gsc);
			return buildJsonMessageResponse(true, gsc.getErrorDescription());
		}
	}
	
	public void setPr(PropertyReader pr) {
		this.pr = pr;
	}
	
	/**
	 * Arrotonda il valore in input alla prima cifra decimale.
	 * 
	 * @param val
	 *            valore in input
	 * @return val arrotondato
	 */
	private Double arrotondaValorePrimaCifra(Double val) {
		Double valPerDieci = val * 10;
		return (calcolaIntero(valPerDieci) / 10);
	}
	
	/**
	 * Arrotonda il valore in input alla terza cifra decimale.
	 * 
	 * @param val
	 *            valore in input
	 * @return val arrotondato
	 */
	private Double arrotondaValoreTerzaCifra(Double val) {
		Double valPerMille = val * 1000;
		return (calcolaIntero(valPerMille) / 1000);
	}
	
	/**
	 * Costruisce il JSON con le informazioni sulle classi energetiche degli edifici a partire dalla lista degli edifici
	 * selezionati.
	 * 
	 * @param listaBuildings
	 *            lista degli edifici selezionati
	 * @param totalUuid
	 *            numero di edifici selezionati
	 * @return JSON con le informazioni sulle classi degli edifici
	 * @throws GSCServiceException
	 */
	private String buildJsonClassiEdifici(List<Buildings> listaBuildings, int totalUuid) throws GSCServiceException {
		try {
			Map<String, List<Map<String, Object>>> mapMsg = new HashMap<String, List<Map<String, Object>>>();
			
			List<Map<String, Object>> buildings = new ArrayList<Map<String, Object>>();
			
			List<Map<String, Object>> listaClass = new ArrayList<Map<String, Object>>();
			
			Map<String, Double> mapClassCount = new HashMap<String, Double>();
			
			for (Buildings build : listaBuildings) {
				Map<String, Object> mapBuilding = new HashMap<String, Object>();
				
				mapBuilding.put(Constants.JSON_UUID_FIELD, build.getUuid());
				
				if (!build.getBuildingsEnergyPerformanceTs().isEmpty()) {
					Iterator iter = build.getBuildingsEnergyPerformanceTs().iterator();
					
					while (iter.hasNext()) {
						BuildingsEnergyPerformanceT bep = (BuildingsEnergyPerformanceT) iter.next();
						mapBuilding.put(Constants.JSON_ENERGYPERFORMANCE_VALUE_FIELD, arrotondaValoreTerzaCifra(bep
						        .getId().getEnergyperformancePerfValue()));
						
						String classe = bep.getId().getEnergyperformancePerfClass();
						
						Double count = mapClassCount.get(classe) != null ? mapClassCount.get(classe) : 0.0;
						Double newCount = count + 1.0;
						mapClassCount.put(classe, newCount);
						break;
					}
				}
				else {
					mapBuilding.put(Constants.JSON_ENERGYPERFORMANCE_VALUE_FIELD, Constants.NON_DISPONIBILE);
					Double count = mapClassCount.get(Constants.NON_DISPONIBILE) != null ? mapClassCount
					        .get(Constants.NON_DISPONIBILE) : 0.0;
					Double newCount = count + 1.0;
					mapClassCount.put(Constants.NON_DISPONIBILE, newCount);
				}
				
				buildings.add(mapBuilding);
			}
			
			for (String keyClass : new TreeSet<String>(mapClassCount.keySet())) {
				listaClass.add(createMapClass(keyClass, mapClassCount, totalUuid));
			}
			
			mapMsg.put(Constants.JSON_BUILDINGS_FIELD, buildings);
			mapMsg.put(Constants.JSON_CLASS_FIELD, listaClass);
			
			return buildJsonMessageResponse(false, om.writeValueAsString(mapMsg));
		}
		catch (Exception e) {
			log.error("Error in the method buildJsonConsumiEdificiSelezionati", e);
			throw new GSCServiceException(e.getMessage(), pr.getValue(Constants.ERRORE_RECUPERO_CLASSI));
		}
	}
	
	/**
	 * Costruisce il JSON con le informazioni sui consumi degli edifici selezionati. In particolare viene ritornata la
	 * somma dei consumi di tutti gli edifici anno per anno, i singoli valori di consumo nell'anno indicato nel
	 * parametro valSlider e il numero e la percentuale di edifici appartenenti alle classi XXS, XS, S, M, L, XL, XXL.
	 * 
	 * @param result
	 *            lista dei record sui consumi
	 * @param valSlider
	 *            anno selezionato
	 * @param arrayIntervalli
	 *            array con gli estremi degli intervalli (consumi normalizzati) caratterizzanti le classi
	 * @param totalUuid
	 *            numero di edifici selezionati
	 * @return JSON con le informazioni sui consumi degli edifici
	 * @throws GSCServiceException
	 */
	private String buildJsonConsumiEdificiSelezionati(List<Object> result, String valSlider, Double arrayIntervalli[],
	        int totalUuid) throws GSCServiceException {
		try {
			Map<String, List<Map<String, Object>>> mapMsg = new HashMap<String, List<Map<String, Object>>>();
			
			List<Map<String, Object>> listaBuildings = new ArrayList<Map<String, Object>>();
			
			List<Map<String, Object>> listaSumBuildings = new ArrayList<Map<String, Object>>();
			
			List<Map<String, Object>> listaClass = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> mapUuidConsumo = new HashMap<String, Object>();
			
			Map<String, Map<String, Double>> mapYearConsumi = new HashMap<String, Map<String, Double>>();
			
			Map<String, Double> mapClassCount = new HashMap<String, Double>();
			
			for (Object o : result) {
				Object[] arrayResult = (Object[]) o;
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String anno = sdf.format((Date) arrayResult[0]).substring(0, 4);
				
				Double consumoAssoluto = Double.parseDouble(arrayResult[1].toString());
				Double volume = Double.parseDouble(arrayResult[2].toString());
				
				Double consumoNormalizzato = consumoAssoluto / volume;
				String uuid = arrayResult[3].toString();
				
				if (anno.equalsIgnoreCase(valSlider)) {
					mapUuidConsumo.put(uuid, arrotondaValoreTerzaCifra(consumoNormalizzato));
					String classe = calcolaClasseConsumo(consumoNormalizzato, arrayIntervalli);
					
					Double count = mapClassCount.get(classe) != null ? mapClassCount.get(classe) : 0.0;
					Double newCount = count + 1.0;
					mapClassCount.put(classe, newCount);
				}
				
				Map<String, Double> mapConsumi = mapYearConsumi.get(anno) != null ? mapYearConsumi.get(anno)
				        : new HashMap<String, Double>();
				
				Double absolute = mapConsumi.get(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD) != null ? mapConsumi
				        .get(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD) : 0.0;
				Double newAbsolute = absolute + consumoAssoluto;
				mapConsumi.put(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD, newAbsolute);
				
				Double normalized = mapConsumi.get(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD) != null ? mapConsumi
				        .get(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD) : 0.0;
				Double newNormalized = normalized + arrotondaValoreTerzaCifra(consumoNormalizzato);
				mapConsumi.put(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD, newNormalized);
				
				mapYearConsumi.put(anno, mapConsumi);
			}
			
			for (String keyUuid : new TreeSet<String>(mapUuidConsumo.keySet())) {
				Double consumo = (Double) mapUuidConsumo.get(keyUuid);
				
				Map<String, Object> mapBuilding = new HashMap<String, Object>();
				mapBuilding.put(Constants.JSON_UUID_FIELD, keyUuid);
				mapBuilding.put(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD, consumo);
				
				listaBuildings.add(mapBuilding);
			}
			
			for (String keyYear : new TreeSet<String>(mapYearConsumi.keySet())) {
				Double absoluteConsumption = mapYearConsumi.get(keyYear)
				        .get(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD);
				Double normalizedConsumption = mapYearConsumi.get(keyYear).get(
				        Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD);
				
				Map<String, Object> mapSumBuildings = new HashMap<String, Object>();
				mapSumBuildings.put(Constants.JSON_YEAR_FIELD, keyYear);
				mapSumBuildings.put(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD,
				        arrotondaValoreTerzaCifra(absoluteConsumption));
				mapSumBuildings.put(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD,
				        arrotondaValoreTerzaCifra(normalizedConsumption));
				
				listaSumBuildings.add(mapSumBuildings);
			}
			
			if (mapClassCount.get(Constants.XXS) != null) {
				listaClass.add(createMapClass(Constants.XXS, mapClassCount, totalUuid));
			}
			if (mapClassCount.get(Constants.XS) != null) {
				listaClass.add(createMapClass(Constants.XS, mapClassCount, totalUuid));
			}
			if (mapClassCount.get(Constants.S) != null) {
				listaClass.add(createMapClass(Constants.S, mapClassCount, totalUuid));
			}
			if (mapClassCount.get(Constants.M) != null) {
				listaClass.add(createMapClass(Constants.M, mapClassCount, totalUuid));
			}
			if (mapClassCount.get(Constants.L) != null) {
				listaClass.add(createMapClass(Constants.L, mapClassCount, totalUuid));
			}
			if (mapClassCount.get(Constants.XL) != null) {
				listaClass.add(createMapClass(Constants.XL, mapClassCount, totalUuid));
			}
			if (mapClassCount.get(Constants.XXL) != null) {
				listaClass.add(createMapClass(Constants.XXL, mapClassCount, totalUuid));
			}
			
			mapMsg.put(Constants.JSON_BUILDINGS_FIELD, listaBuildings);
			mapMsg.put(Constants.JSON_SUM_CONSUMPTION_ALL_BUILDINGS_FIELD, listaSumBuildings);
			mapMsg.put(Constants.JSON_CLASS_FIELD, listaClass);
			
			return buildJsonMessageResponse(false, om.writeValueAsString(mapMsg));
		}
		catch (Exception e) {
			log.error("Error in the method buildJsonConsumiEdificiSelezionati", e);
			throw new GSCServiceException(e.getMessage(), pr.getValue(Constants.ERRORE_RECUPERO_CONSUMI));
		}
	}
	
	/**
	 * Costruisce il JSON con le informazioni sui consumi degli impianti selezionati. In particolare viene ritornata la
	 * somma dei consumi di tutti gli impianti anno per anno e i singoli valori di consumo nell'anno indicato nel
	 * parametro valSlider.
	 * 
	 * @param result
	 *            lista dei record sui consumi
	 * @param valSlider
	 *            anno selezionato
	 * @param mapKey
	 *            map che contiene le associazioni uuid tabella THERMALZONE_ENERGYAMOUNT_T e uuid INSTALLATION
	 * @return JSON con le informazioni sui consumi degli impianti
	 * @throws GSCServiceException
	 */
	private String buildJsonConsumiImpiantiSelezionati(List<Object> result, String valSlider, Map<String, String> mapKey)
	        throws GSCServiceException {
		try {
			Map<String, List<Map<String, Object>>> mapMsg = new HashMap<String, List<Map<String, Object>>>();
			
			List<Map<String, Object>> listaInstallations = new ArrayList<Map<String, Object>>();
			
			List<Map<String, Object>> listaSumInstallations = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> mapUuidConsumo = new HashMap<String, Object>();
			
			Map<String, Map<String, Double>> mapYearConsumi = new HashMap<String, Map<String, Double>>();
			
			for (Object o : result) {
				Object[] arrayResult = (Object[]) o;
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String anno = sdf.format((Date) arrayResult[0]).substring(0, 4);
				
				Double consumoAssoluto = Double.parseDouble(arrayResult[1].toString());
				String uuid = arrayResult[2].toString();
				
				if (anno.equalsIgnoreCase(valSlider)) {
					mapUuidConsumo.put(uuid, consumoAssoluto);
				}
				
				Map<String, Double> mapConsumi = mapYearConsumi.get(anno) != null ? mapYearConsumi.get(anno)
				        : new HashMap<String, Double>();
				
				Double absolute = mapConsumi.get(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD) != null ? mapConsumi
				        .get(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD) : 0.0;
				Double newAbsolute = absolute + consumoAssoluto;
				mapConsumi.put(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD, newAbsolute);
				
				mapYearConsumi.put(anno, mapConsumi);
			}
			
			Map<String, Map<String, Object>> mapSort = new HashMap<String, Map<String, Object>>();
			
			for (String keyUuid : mapUuidConsumo.keySet()) {
				Double consumo = (Double) mapUuidConsumo.get(keyUuid);
				
				Map<String, Object> mapInstallation = new HashMap<String, Object>();
				mapInstallation.put(Constants.JSON_UUID_FIELD, mapKey.get(keyUuid));
				mapInstallation.put(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD, consumo);
				
				mapSort.put(mapKey.get(keyUuid), mapInstallation);
			}
			
			for (String keyInst : new TreeSet<String>(mapSort.keySet())) {
				listaInstallations.add(mapSort.get(keyInst));
			}
			
			for (String keyYear : new TreeSet<String>(mapYearConsumi.keySet())) {
				Double absoluteConsumption = mapYearConsumi.get(keyYear)
				        .get(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD);
				
				Map<String, Object> mapSumInstallations = new HashMap<String, Object>();
				mapSumInstallations.put(Constants.JSON_YEAR_FIELD, keyYear);
				mapSumInstallations.put(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD,
				        arrotondaValoreTerzaCifra(absoluteConsumption));
				
				listaSumInstallations.add(mapSumInstallations);
			}
			
			mapMsg.put(Constants.JSON_INSTALLATIONS_FIELD, listaInstallations);
			mapMsg.put(Constants.JSON_SUM_CONSUMPTION_ALL_INSTALLATIONS_FIELD, listaSumInstallations);
			
			return buildJsonMessageResponse(false, om.writeValueAsString(mapMsg));
		}
		catch (Exception e) {
			log.error("Error in the method buildJsonConsumiImpiantiSelezionati", e);
			throw new GSCServiceException(e.getMessage(), pr.getValue(Constants.ERRORE_RECUPERO_CONSUMI));
		}
	}
	
	/**
	 * Costruisce il JSON con le informazioni sui consumi dell'edificio selezionato (consumi assoluti e normalizzati
	 * anno per anno).
	 * 
	 * @param result
	 *            lista dei record sui consumi
	 * @return JSON con le informazioni sui consumi dell'edificio selezionato
	 * @throws GSCServiceException
	 */
	private String buildJsonConsumiSingoloEdificio(List<Object> result) throws GSCServiceException {
		try {
			List<Map<String, Object>> listConsumption = new ArrayList<Map<String, Object>>();
			
			for (Object o : result) {
				Object[] arrayResult = (Object[]) o;
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String anno = sdf.format((Date) arrayResult[0]).substring(0, 4);
				
				Double consumoAssoluto = Double.parseDouble(arrayResult[1].toString());
				Double volume = Double.parseDouble(arrayResult[2].toString());
				
				Map<String, Object> mapConsumption = new HashMap<String, Object>();
				mapConsumption.put(Constants.JSON_YEAR_FIELD, anno);
				mapConsumption.put(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD, consumoAssoluto);
				mapConsumption.put(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD,
				        arrotondaValoreTerzaCifra(consumoAssoluto / volume));
				
				listConsumption.add(mapConsumption);
			}
			return buildJsonMessageResponse(false, om.writeValueAsString(listConsumption));
		}
		catch (Exception e) {
			log.error("Error in the method buildJsonConsumiSingoloEdificio", e);
			throw new GSCServiceException(e.getMessage(), pr.getValue(Constants.ERRORE_RECUPERO_CONSUMI));
		}
	}
	
	/**
	 * Costruisce il JSON con le informazioni sui consumi dell'impianto selezionato (consumi assoluti anno per anno).
	 * 
	 * @param result
	 *            lista dei record sui consumi
	 * @return JSON con le informazioni sui consumi dell'impianto selezionato
	 * @throws GSCServiceException
	 */
	private String buildJsonConsumiSingoloImpianto(List<Object> result) throws GSCServiceException {
		try {
			List<Map<String, Object>> listConsumption = new ArrayList<Map<String, Object>>();
			
			for (Object o : result) {
				Object[] arrayResult = (Object[]) o;
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String anno = sdf.format((Date) arrayResult[0]).substring(0, 4);
				
				Double consumoAssoluto = Double.parseDouble(arrayResult[1].toString());
				
				Map<String, Object> mapConsumption = new HashMap<String, Object>();
				mapConsumption.put(Constants.JSON_YEAR_FIELD, anno);
				mapConsumption.put(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD, consumoAssoluto);
				
				listConsumption.add(mapConsumption);
			}
			return buildJsonMessageResponse(false, om.writeValueAsString(listConsumption));
		}
		catch (Exception e) {
			log.error("Error in the method buildJsonConsumiSingoloImpianto", e);
			throw new GSCServiceException(e.getMessage(), pr.getValue(Constants.ERRORE_RECUPERO_CONSUMI));
		}
	}
	
	/**
	 * Costruisce il JSON con le informazioni sui certificati di un edificio, raccolte tramite select sul db (svolta dal
	 * BuildingDAO).
	 * 
	 * @param listaCert
	 *            lista dei risultati ottenuti da una select sul db (svolta dal BuildingDAO)
	 * @return JSON con le informazioni sui certificati di un edificio
	 * @throws GSCServiceException
	 */
	private String buildJsonForCertificates(List<Thermalzone> listaCert) throws GSCServiceException {
		
		try {
			List<Map<String, Object>> listObjectCert = new ArrayList<Map<String, Object>>();
			
			for (Thermalzone cert : listaCert) {
				Map<String, Object> mapCert = new HashMap<String, Object>();
				mapCert.put(Constants.JSON_UUID_FIELD, cert.getUuid());
				
				mapCert.put(Constants.JSON_CLASS_FIELD, cert.getDPerfClass().getCode());
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String anno = sdf.format(cert.getEnergyperformancePerfDate()).substring(0, 4);
				mapCert.put(Constants.JSON_YEAR_FIELD, anno);
				
				StringBuilder sb = new StringBuilder();
				sb.append(cert.getEnergyperformancePerfValue().toString());
				sb.append(" ");
				sb.append(cert.getHUom().getCode());
				mapCert.put(Constants.JSON_PERFORMANCE_FIELD, sb.toString());
				
				mapCert.put(Constants.JSON_VOLUME_FIELD, arrotondaValoreTerzaCifra(cert.getVolumeValue()));
				
				listObjectCert.add(mapCert);
			}
			
			return buildJsonMessageResponse(false, om.writeValueAsString(listObjectCert));
		}
		catch (Exception e) {
			log.error("Error in the method buildJsonForCertificates", e);
			throw new GSCServiceException(e.getMessage(), pr.getValue(Constants.ERRORE_RECUPERO_CERTIFICATI));
		}
	}
	
	/**
	 * Costruisce il JSON con le informazioni raggruppate per tipo di consumo a partire dalla lista degli edifici
	 * selezionati.
	 * 
	 * @param listaBuildings
	 *            lista degli edifici selezionati
	 * @return JSON con le informazioni raggruppate per tipo di consumo
	 * @throws GSCServiceException
	 */
	private String buildJsonForConsumptions(List<Buildings> listaBuildings) throws GSCServiceException {
		try {
			Map<String, List<Map<String, Object>>> mapMsg = new HashMap<String, List<Map<String, Object>>>();
			
			List<Map<String, Object>> listaConsumiEdifici = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> sumConsumptionsAllBuildings = new ArrayList<Map<String, Object>>();
			
			Map<String, Map<String, Object>> sourceSumConsumptionsBuildings = new HashMap<String, Map<String, Object>>();
			
			for (Buildings build : listaBuildings) {
				
				Map<String, Object> mapBuildings = new HashMap<String, Object>();
				
				if (!build.getBuildingsEnergyamountTs().isEmpty()) {
					Map<String, Object> mapSource = new HashMap<String, Object>();
					
					Iterator iter = build.getBuildingsEnergyamountTs().iterator();
					
					while (iter.hasNext()) {
						BuildingsEnergyamountT bea = (BuildingsEnergyamountT) iter.next();
						
						Map<String, Object> mapYear = mapSource.get(bea.getId().getEnergyamountESource()) != null ? (Map<String, Object>) mapSource
						        .get(bea.getId().getEnergyamountESource()) : new HashMap<String, Object>();
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String anno = sdf.format(bea.getId().getEnergyamountEYear()).substring(0, 4);
						
						Double consumoAssoluto = arrotondaValoreTerzaCifra(bea.getId().getEnergyamountEAmount());
						Double consumoNormalizzato = 0.0;
						
						if (build.getVolumeValue() != null) {
							consumoNormalizzato = arrotondaValoreTerzaCifra(bea.getId().getEnergyamountEAmount()
							        / build.getVolumeValue());
						}
						
						Map<String, Object> mapVal = new HashMap<String, Object>();
						mapVal.put(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD, consumoAssoluto);
						mapVal.put(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD,
						        build.getVolumeValue() != null ? consumoNormalizzato : Constants.NOT_AVAILABLE);
						mapVal.put(
						        Constants.JSON_CO2_FIELD,
						        bea.getEnergyamountEstimatedco2() != null ? arrotondaValoreTerzaCifra(bea
						                .getEnergyamountEstimatedco2()) : Constants.NOT_AVAILABLE);
						
						mapYear.put(anno, mapVal);
						
						if (mapYear.get(Constants.JSON_UOM_FIELD) == null) {
							mapYear.put(Constants.JSON_UOM_FIELD, bea.getId().getEnergyamountEUom());
						}
						
						mapSource.put(bea.getId().getEnergyamountESource(), mapYear);
						
						Map<String, Object> yearSumConsumptionsBuildings = sourceSumConsumptionsBuildings.get(bea
						        .getId().getEnergyamountESource()) != null ? sourceSumConsumptionsBuildings.get(bea
						        .getId().getEnergyamountESource()) : new HashMap<String, Object>();
						
						Map<String, Double> valuesSumConsumptionsBuildings = yearSumConsumptionsBuildings.get(anno) != null ? (Map<String, Double>) yearSumConsumptionsBuildings
						        .get(anno) : new HashMap<String, Double>();
						
						Double absoluteSumConsumptionsBuildings = valuesSumConsumptionsBuildings
						        .get(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD) != null ? valuesSumConsumptionsBuildings
						        .get(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD) : 0.0;
						Double newAbsoluteSumConsumptionsBuildings = absoluteSumConsumptionsBuildings + consumoAssoluto;
						
						valuesSumConsumptionsBuildings.put(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD,
						        newAbsoluteSumConsumptionsBuildings);
						
						if (build.getVolumeValue() != null) {
							Double normalizedSumConsumptionsBuildings = valuesSumConsumptionsBuildings
							        .get(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD) != null ? valuesSumConsumptionsBuildings
							        .get(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD) : 0.0;
							Double newNormalizedSumConsumptionsBuildings = normalizedSumConsumptionsBuildings
							        + consumoNormalizzato;
							
							valuesSumConsumptionsBuildings.put(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD,
							        newNormalizedSumConsumptionsBuildings);
						}
						
						yearSumConsumptionsBuildings.put(anno, valuesSumConsumptionsBuildings);
						
						if (yearSumConsumptionsBuildings.get(Constants.JSON_UOM_FIELD) == null) {
							yearSumConsumptionsBuildings.put(Constants.JSON_UOM_FIELD, bea.getId()
							        .getEnergyamountEUom());
						}
						
						sourceSumConsumptionsBuildings.put(bea.getId().getEnergyamountESource(),
						        yearSumConsumptionsBuildings);
					}
					
					List<Map<String, Object>> listConsumptions = new ArrayList<Map<String, Object>>();
					
					for (String keySource : new TreeSet<String>(mapSource.keySet())) {
						Map<String, Object> mapConsumptions = new HashMap<String, Object>();
						
						List<Map<String, Object>> listValues = new ArrayList<Map<String, Object>>();
						Map<String, Object> mapSourceYear = (Map<String, Object>) mapSource.get(keySource);
						
						for (String keyYear : new TreeSet<String>(mapSourceYear.keySet())) {
							
							if (keyYear.equalsIgnoreCase(Constants.JSON_UOM_FIELD)) {
								String uom = (String) mapSourceYear.get(keyYear);
								mapConsumptions.put(Constants.JSON_UOM_FIELD, uom);
							}
							else {
								Map<String, Object> mapVal = (Map<String, Object>) mapSourceYear.get(keyYear);
								
								Map<String, Object> mapValues = new HashMap<String, Object>();
								mapValues.put(Constants.JSON_YEAR_FIELD, keyYear);
								mapValues.put(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD,
								        mapVal.get(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD));
								mapValues.put(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD,
								        mapVal.get(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD));
								mapValues.put(Constants.JSON_CO2_FIELD, mapVal.get(Constants.JSON_CO2_FIELD));
								
								listValues.add(mapValues);
							}
						}
						mapConsumptions.put(Constants.JSON_SOURCE_FIELD, keySource);
						mapConsumptions.put(Constants.JSON_VALUES_FIELD, listValues);
						
						listConsumptions.add(mapConsumptions);
					}
					
					mapBuildings.put(Constants.JSON_CONSUMPTIONS_FIELD, listConsumptions);
				}
				else {
					mapBuildings.put(Constants.JSON_CONSUMPTIONS_FIELD, Constants.NOT_AVAILABLE);
				}
				
				mapBuildings.put(Constants.JSON_NUM_UIU_FIELD, build.getUnits() != null ? build.getUnits()
				        : Constants.NOT_AVAILABLE);
				mapBuildings.put(Constants.JSON_VOLUME_FIELD,
				        build.getVolumeValue() != null ? arrotondaValoreTerzaCifra(build.getVolumeValue())
				                : Constants.NOT_AVAILABLE);
				mapBuildings.put(Constants.JSON_UUID_FIELD, build.getUuid());
				
				if (!build.getBuildingsEnergyPerformanceTs().isEmpty()) {
					Iterator iterPerf = build.getBuildingsEnergyPerformanceTs().iterator();
					
					while (iterPerf.hasNext()) {
						BuildingsEnergyPerformanceT perf = (BuildingsEnergyPerformanceT) iterPerf.next();
						
						mapBuildings.put(Constants.JSON_CLASS_FIELD, perf.getId().getEnergyperformancePerfClass());
						break;
					}
				}
				else {
					mapBuildings.put(Constants.JSON_CLASS_FIELD, Constants.NOT_AVAILABLE);
				}
				
				listaConsumiEdifici.add(mapBuildings);
			}
			
			for (String source : new TreeSet<String>(sourceSumConsumptionsBuildings.keySet())) {
				List<Map<String, Object>> listValues = new ArrayList<Map<String, Object>>();
				Map<String, Object> mapSources = new HashMap<String, Object>();
				
				mapSources.put(Constants.JSON_SOURCE_FIELD, source);
				
				Map<String, Object> mapYear = sourceSumConsumptionsBuildings.get(source);
				
				for (String keyYear : new TreeSet<String>(mapYear.keySet())) {
					
					if (keyYear.equalsIgnoreCase(Constants.JSON_UOM_FIELD)) {
						String uom = (String) mapYear.get(keyYear);
						mapSources.put(Constants.JSON_UOM_FIELD, uom);
					}
					else {
						Map<String, Double> mapVal = (Map<String, Double>) mapYear.get(keyYear);
						
						Map<String, Object> mapValues = new HashMap<String, Object>();
						mapValues.put(Constants.JSON_YEAR_FIELD, keyYear);
						mapValues.put(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD,
						        mapVal.get(Constants.JSON_ABSOLUTE_ENERGYAMOUNT_FIELD));
						
						if (mapVal.get(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD) != null) {
							mapValues.put(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD,
							        mapVal.get(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD));
						}
						else {
							mapValues.put(Constants.JSON_NORMALIZED_ENERGYAMOUNT_FIELD, Constants.NOT_AVAILABLE);
						}
						
						listValues.add(mapValues);
					}
				}
				mapSources.put(Constants.JSON_VALUES_FIELD, listValues);
				
				sumConsumptionsAllBuildings.add(mapSources);
			}
			
			mapMsg.put(Constants.JSON_BUILDINGS_FIELD, listaConsumiEdifici);
			mapMsg.put(Constants.JSON_SUM_CONSUMPTION_ALL_BUILDINGS_FIELD, sumConsumptionsAllBuildings);
			
			return buildJsonMessageResponse(false, om.writeValueAsString(mapMsg));
		}
		catch (Exception e) {
			log.error("Error in the method buildJsonForConsumptions", e);
			throw new GSCServiceException(e.getMessage(), pr.getValue(Constants.ERRORE_RECUPERO_CONSUMI));
		}
	}
	
	/**
	 * Costruisce il JSON da inviare in risposta alla chiamata alla servlet.
	 * 
	 * @param error
	 *            booleano che indica se restituire un messaggio d'errore o meno
	 * @param msg
	 *            messaggio da restituire
	 * @return JSON da inviare in risposta
	 */
	private String buildJsonMessageResponse(boolean error, String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"");
		sb.append(Constants.JSON_SUCCESS_FIELD);
		sb.append("\": ");
		if (error) {
			sb.append("false");
		}
		else {
			sb.append("true");
		}
		sb.append(", \"");
		sb.append(Constants.JSON_MSG_FIELD);
		sb.append("\": ");
		if (error) {
			sb.append("\"");
		}
		sb.append(msg);
		if (error) {
			sb.append("\"");
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Ritorna la classe di appartenenza di un edificio in base al consumo normalizzato e agli estremi contenuti
	 * nell'array arrayIntervalli.
	 * 
	 * @param consumoNormalizzato
	 *            valore di consumo normalizzato
	 * @param arrayIntervalli
	 *            array con gli estremi degli intervalli (consumi normalizzati) caratterizzanti le classi
	 * @return classe di appartenenza di un edificio
	 */
	private String calcolaClasseConsumo(Double consumoNormalizzato, Double arrayIntervalli[]) {
		if (consumoNormalizzato < arrayIntervalli[0]) {
			return Constants.XXS;
		}
		else if (consumoNormalizzato >= arrayIntervalli[0] && consumoNormalizzato < arrayIntervalli[1]) {
			return Constants.XS;
		}
		else if (consumoNormalizzato >= arrayIntervalli[1] && consumoNormalizzato < arrayIntervalli[2]) {
			return Constants.S;
		}
		else if (consumoNormalizzato >= arrayIntervalli[2] && consumoNormalizzato < arrayIntervalli[3]) {
			return Constants.M;
		}
		else if (consumoNormalizzato >= arrayIntervalli[3] && consumoNormalizzato < arrayIntervalli[4]) {
			return Constants.L;
		}
		else if (consumoNormalizzato >= arrayIntervalli[4] && consumoNormalizzato < arrayIntervalli[5]) {
			return Constants.XL;
		}
		else {
			return Constants.XXL;
		}
	}
	
	/**
	 * Calcola il valore intero di un numero da approssimare.
	 * 
	 * @param val
	 *            numero da approssimare
	 * @return intero
	 */
	private Double calcolaIntero(Double val) {
		Double intero = Math.floor(val);
		Double decimal = val - intero;
		if (decimal > 0.5) {
			intero = intero + 1;
		}
		return intero;
	}
	
	/**
	 * Crea l'array con gli estremi degli intervalli (consumi normalizzati) caratterizzanti le classi.
	 * 
	 * @param graphicDataType
	 *            tipologia di consumo
	 * @param sldType
	 *            tipologia di sld (in base a tale parametro cambiano gli intervalli anche per la stessa tipologia di
	 *            consumo)
	 * @return array con gli estremi degli intervalli
	 * @throws GSCServiceException
	 */
	private Double[] creaArrayIntervalliSld(String graphicDataType, String sldType) throws GSCServiceException {
		
		if (graphicDataType.equalsIgnoreCase(Constants.TERMICO) && sldType.equalsIgnoreCase(Constants.RESIDENZIALE)) {
			Double arrayTermRes[] = { 8.0, 16.0, 30.0, 44.0, 60.0, 80.0 };
			return arrayTermRes;
		}
		
		if (graphicDataType.equalsIgnoreCase(Constants.ELETTRICO) && sldType.equalsIgnoreCase(Constants.RESIDENZIALE)) {
			Double arrayEleRes[] = { 5.0, 10.0, 15.0, 20.0, 25.0, 30.0 };
			return arrayEleRes;
		}
		
		if (graphicDataType.equalsIgnoreCase(Constants.ELETTRICO) && sldType.equalsIgnoreCase(Constants.COMMERCIALE)) {
			Double arrayEleCom[] = { 10.0, 20.0, 40.0, 60.0, 85.0, 120.0 };
			return arrayEleCom;
		}
		
		if (graphicDataType.equalsIgnoreCase(Constants.ELETTRICO) && sldType.equalsIgnoreCase(Constants.AUSILIARIO)) {
			Double arrayEleAus[] = { 20.0, 40.0, 80.0, 120.0, 170.0, 240.0 };
			return arrayEleAus;
		}
		
		if (graphicDataType.equalsIgnoreCase(Constants.TOTALE) && sldType.equalsIgnoreCase(Constants.RESIDENZIALE)) {
			Double arrayTotRes[] = { 15.0, 40.0, 60.0, 80.0, 100.0, 120.0 };
			return arrayTotRes;
		}
		
		if (graphicDataType.equalsIgnoreCase(Constants.TOTALE) && sldType.equalsIgnoreCase(Constants.COMMERCIALE)) {
			Double arrayTotCom[] = { 30.0, 60.0, 120.0, 180.0, 250.0, 360.0 };
			return arrayTotCom;
		}
		
		if (graphicDataType.equalsIgnoreCase(Constants.TOTALE) && sldType.equalsIgnoreCase(Constants.AUSILIARIO)) {
			Double arrayTotAus[] = { 110.0, 220.0, 440.0, 660.0, 920.0, 1300.0 };
			return arrayTotAus;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Combination graphicDataType:");
		sb.append(graphicDataType);
		sb.append(" and sldType:");
		sb.append(sldType);
		sb.append(" doesn't exist");
		throw new GSCServiceException(sb.toString(), pr.getValue(Constants.ERRORE_PARAMETRI_INPUT));
	}
	
	/**
	 * Crea l'oggetto con le informazioni sulle classi (nome, numero di edifici e percentuale).
	 * 
	 * @param keyClass
	 *            nome classe
	 * @param mapClassCount
	 *            mappa contenente il numero di edifici per ogni classe
	 * @param totalUuid
	 *            numero di edifici selezionati
	 * @return oggetto con le informazioni sulle classi
	 */
	private Map<String, Object> createMapClass(String keyClass, Map<String, Double> mapClassCount, int totalUuid) {
		Map<String, Object> mapClass = new HashMap<String, Object>();
		
		mapClass.put(Constants.JSON_NAME_FIELD, keyClass);
		mapClass.put(Constants.JSON_COUNT_FIELD, mapClassCount.get(keyClass).intValue());
		mapClass.put(Constants.JSON_PERCENT_FIELD,
		        arrotondaValorePrimaCifra((mapClassCount.get(keyClass) / totalUuid) * 100.0));
		
		return mapClass;
	}
	
	/**
	 * Estrae i parametri inviati in input alla servlet.
	 * 
	 * @param jsonParameter
	 *            JSON in input in formato stringa
	 * @return map con i valori estratti
	 * @throws GSCServiceException
	 */
	private Map<String, Object> estraiParametri(String jsonParameter) throws GSCServiceException {
		try {
			List<String> listUuid = new ArrayList<String>();
			
			JsonNode nodeParameter = om.readTree(jsonParameter);
			
			JsonNode arrayUuid = nodeParameter.get(Constants.JSON_UUID_FIELD);
			if (arrayUuid.isArray()) {
				for (JsonNode uuid : arrayUuid) {
					listUuid.add(uuid.asText());
				}
			}
			else {
				listUuid.add(arrayUuid.asText());
			}
			
			Map<String, Object> mapParametri = new HashMap<String, Object>();
			mapParametri.put(Constants.JSON_UUID_FIELD, listUuid);
			
			if (nodeParameter.get(Constants.GRAPHIC_DATA_TYPE) != null) {
				mapParametri.put(Constants.GRAPHIC_DATA_TYPE, nodeParameter.get(Constants.GRAPHIC_DATA_TYPE).asText());
			}
			
			if (nodeParameter.get(Constants.VAL_SLIDER) != null) {
				mapParametri.put(Constants.VAL_SLIDER, nodeParameter.get(Constants.VAL_SLIDER).asText());
			}
			
			if (nodeParameter.get(Constants.SLD_TYPE) != null) {
				mapParametri.put(Constants.SLD_TYPE, nodeParameter.get(Constants.SLD_TYPE).asText());
			}
			
			return mapParametri;
		}
		catch (Exception e) {
			log.error("Error in the method estraiParametri", e);
			throw new GSCServiceException(e.getMessage(), pr.getValue(Constants.ERRORE_PARAMETRI_INPUT));
		}
	}
}