package eu.geosmartcity.hub.wps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.geoserver.wps.WPSException;
import org.geoserver.wps.jts.SpringBeanProcessFactory;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.util.ProgressListener;

import eu.geosmartcity.hub.utils.Constants;
import eu.geosmartcity.hub.utils.DBService;
import eu.geosmartcity.hub.utils.ProjectProperties;

@DescribeProcess(title = "buildingsCo2EmissionEstimation", description = "Service that compute the buildings co2 emission estimation")
public class BuildingCo2EmissionsEstimation extends SpringBeanProcessFactory {
	
	private static final Logger LOGGER = Logger.getLogger(BuildingCo2EmissionsEstimation.class);
	
	public BuildingCo2EmissionsEstimation() {
		super(Constants.WPS_TITLE, Constants.WPS_NAMESPACE, BuildingCo2EmissionsEstimation.class);
	}
	
	@DescribeResult(name = "buildingsWithCo2", description = "buildings co2 emission")
	public SimpleFeatureCollection execute(
			@DescribeParameter(name = "buildings", description = "Buildings") SimpleFeatureCollection buildings,
			@DescribeParameter(name = "fieldIsConsumption", description = "Field name that indicates if input is the consumption", min = 0, defaultValue = "is_consumption") String fieldIsConsumption,
			@DescribeParameter(name = "fieldEnergyAmount", description = "Field name that indicates the energy amount", min = 0, defaultValue = "energy_amount") String fieldEnergyAmount,
			@DescribeParameter(name = "fieldEnergyUom", description = "Field name that indicates the unit of measure for energy", min = 0, defaultValue = "energy_uom") String fieldEnergyUom,
			@DescribeParameter(name = "fieldFuel", description = "Field name that indicates the fuel. Fuel is mandatory if the input is consumption ", min = 0, defaultValue = "fuel") String fieldFuel,
			@DescribeParameter(name = "fieldLocation", description = "Field name that indicates the location (indicate in table h_location)", min = 0, defaultValue = "location_city") String fieldLocation,
			@DescribeParameter(name = "fieldYear", description = "Field name that indicates the year", min = 0, defaultValue = "year") String fieldYear,
			ProgressListener progressListener) throws Exception {
		
		progressListener.started();
		
		//controllo parametri in input
		checkInputParameter(buildings);
		
		SimpleFeatureType builderFeatureType = createBuilder(buildings);
		List<SimpleFeature> listFeature = new ArrayList<SimpleFeature>();
		
		DBService dbService = new DBService();
		
		try {
			
			SimpleFeatureIterator sfi = buildings.features();
			
			while (sfi.hasNext()) {
				SimpleFeature sf = sfi.next();
				
				String isConsumption = getAttributeString(sf, fieldIsConsumption);
				
				String fuel = null;
				if (isConsumption != null && isConsumption.equalsIgnoreCase("true")) {
					//se si tratta di conusmo obbligatorio il campo fuel
					fuel = getAttributeString(sf, fieldFuel);
					if (fuel == null) {
						throw new WPSException("fuel is mandatory, if is consumption");
					}
				}
				
				String location = getAttributeString(sf, fieldLocation);
				if (location == null) {
					throw new WPSException("location is mandatory, check the field for location " + fieldLocation);
				}
				String uom = getAttributeString(sf, fieldEnergyUom);
				if (uom == null) {
					throw new WPSException("uom is mandatory, check the field for uom " + fieldEnergyUom);
				}
				
				//ricavo la data
				String date = getDate(sf.getAttribute(fieldYear));
				if (date == null) {
					throw new WPSException("date is mandatory, check the field for date " + fieldYear);
				}
				
				Double conversionValue = dbService
						.getValueFromConversionTable(location, date, fuel, uom, isConsumption);
				if (conversionValue == null) {
					LOGGER.error("impossibile recuperare il valore dalla conversion table, controllare location "
							+ location + " anno " + date + " fuel " + fuel);
					throw new WPSException("impossible to retrieve the value from conversion table");
				}
				
				Double energyAmount = Double.valueOf(getAttributeString(sf, fieldEnergyAmount));
				
				List<Object> listAttributes = sf.getAttributes();
				listAttributes.add(energyAmount * conversionValue);
				listAttributes.add(Constants.CO2_UOM);
				SimpleFeature sfNew = new SimpleFeatureImpl(listAttributes, builderFeatureType, sf.getIdentifier());
				
				listFeature.add(sfNew);
			}
		}
		catch (Exception e) {
			LOGGER.error("errore nel calcolo del co2");
			throw new WPSException(e.getMessage());
		}
		finally {
			dbService.closeConnection();
		}
		
		//nuova collection con il co2 amount e il co2 uom
		SimpleFeatureCollection collection = new ListFeatureCollection(builderFeatureType, listFeature);
		
		return collection;
		
	}
	
	/**
	 * metodo che restituisce gli attributi come stringa
	 * 
	 * @param sf
	 * @param attributeName
	 * @return
	 */
	private String getAttributeString(SimpleFeature sf, String attributeName) {
		
		if (sf.getAttribute(attributeName) instanceof Boolean) {
			return String.valueOf((Boolean) sf.getAttribute(attributeName));
		}
		else if (sf.getAttribute(attributeName) instanceof Integer) {
			return String.valueOf((Integer) sf.getAttribute(attributeName));
		}
		else if (sf.getAttribute(attributeName) instanceof Double) {
			return String.valueOf((Double) sf.getAttribute(attributeName));
		}
		else if (sf.getAttribute(attributeName) instanceof String) {
			return (String) sf.getAttribute(attributeName);
		}
		else {
			throw new WPSException("check type input parameter " + attributeName);
		}
	}
	
	/**
	 * crea il builder con i nuovi attributi visualizzati nel risultato
	 * 
	 * @param buildings
	 * @return
	 */
	private SimpleFeatureType createBuilder(SimpleFeatureCollection buildings) {
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName(buildings.getSchema().getName());
		builder.setNamespaceURI("http://www.geosmartcity.eu");
		
		//metto gli stessi attributi della featurecollection in input + aggiungo il co2_amount e il co2_uom
		builder.setAttributes(buildings.getSchema().getAttributeDescriptors());
		builder.add(new AttributeDescriptorImpl(new AttributeTypeImpl(new NameImpl(Constants.FIELD_CO2_AMOUNT),
				Double.class, false, false, null, null, null), new NameImpl(Constants.FIELD_CO2_AMOUNT), 0, 1, true,
				null));
		builder.add(new AttributeDescriptorImpl(new AttributeTypeImpl(new NameImpl(Constants.FIELD_CO2_UOM),
				String.class, false, false, null, null, null), new NameImpl(Constants.FIELD_CO2_UOM), 0, 1, true, null));
		return builder.buildFeatureType();
	}
	
	/**
	 * restituisce la data
	 * 
	 * @param attribute
	 */
	private String getDate(Object attribute) {
		String date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(ProjectProperties.loadByName("date_format"));
			GregorianCalendar gc = new GregorianCalendar();
			//setto la data attuale per ricavare l'anno attuale -> se assente si assume il più recente
			gc.setTime(new Date());
			//setto il primo giorno dell'anno attuale (es: 01-01-2016)
			gc.set(gc.get(GregorianCalendar.YEAR), 0, 1);
			
			//se ho l'anno nel gml uso quello
			if (attribute instanceof Integer) {
				Integer year = (Integer) attribute;
				gc.set(year, 0, 1);
				
			}
			else if (attribute instanceof String) {
				String dateFromGml = (String) attribute;
				//e' l'anno
				if (dateFromGml.length() == 4) {
					gc.set(Integer.parseInt(dateFromGml), 0, 1);
				} else {
					gc.setTime(sdf.parse(dateFromGml));
					gc.set(gc.get(GregorianCalendar.YEAR), 0, 1);
				}
				
			}
			date = sdf.format(gc.getTime());
		}
		catch (Exception e) {
			LOGGER.error("errore durante il calcolo dell'anno " + attribute, e);
			throw new WPSException("error while process calculates the date");
		}
		return date;
	}
	
	/**
	 * controlla i parametri in input
	 */
	private void checkInputParameter(SimpleFeatureCollection buildings) {
		
		if (buildings == null) {
			throw new WPSException("specify the building, it is mandatory");
		}
		
		if (buildings.size() <= 0) {
			throw new WPSException("no building defined");
		}
		
	}
	
}
