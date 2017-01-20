package it.sinergis.gsc.wps;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.geoserver.wps.jts.SpringBeanProcessFactory;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
//import org.geotools.process.gs.GSProcess;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;
//import org.geoserver.wps.
import org.postgresql.util.PGobject;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;

import it.sinergis.gsc.db.EnergyDBService;
import it.sinergis.gsc.support.Utils;
import it.sinergis.gsc.wps.model.EnergyModel;
import it.sinergis.gsmc.exception.ClimaticZoneException;

@DescribeProcess(title="EnergyPerformanceWPS", description="EnergyPerformanceWPS sample")
public class EnergyPerformanceWPS extends SpringBeanProcessFactory {
	//implements GSProcess
  final static Logger logger = Logger.getLogger(EnergyPerformanceWPS.class);
  public EnergyPerformanceWPS () {//Catalog catalog
	  super("EnergyPerformanceWPS" ,"gsc", EnergyPerformanceWPS.class);
	}
  
  @DescribeResult(name="result", description="mappa di energy performance ")
   public SimpleFeatureCollection execute(
		   @DescribeParameter(name="climaticZone", min = 1, max=1, description="name of the TABULA climatic zone, for more info check the web page http://hub.geosmartcity.eu/tabulaRest/rest/tabula") String climaticZone,		   
		   @DescribeParameter(name="begin", min = 0, max=1, description="name of the attribute that contains the begin construction year of buildings") String begin,		   
		   @DescribeParameter(name="end", min = 1, max=1, description="name of the attribute that contains the end construction year of buildings. If the begin field is used, the data considered by the computation is the middle of the 2 values") String end,		   
		   @DescribeParameter(name="height", min = 0, max=1, description="name of the attribute that contains the heigtht of the building.") String height,	   
		   @DescribeParameter(name="floors", min = 0, max=1, description="name of the attribute that contains the number of floors of the buildings.") String floors,	   
		   @DescribeParameter(name="aveFloor", min = 0, max=1, description="name of the attribute that contains the information of the average floor heigth of the building.") String aveFloor,
		   @DescribeParameter(name="refurbihment", min = 0, max=1, description="name of the attribute that contains the information about the refurbihment level. The values on the layer can be: 0 -> no refurbihment 1 -> standard 2 -> advance If this field is not edited all buildings use the standard refurbihment.") String refurbihment,
		   @DescribeParameter(name="residential", min = 0, max=1, description="name of the attribute that indicates whether the building is residential. If this field is not edited all building is considered as residential") String residential,
		   @DescribeParameter(name="id", min = 0, max=1, description="field that indicate a key to add at the layer that will be generate.") String id,
		   @DescribeParameter(name="layer",  description="the building layer")SimpleFeatureCollection layer
		   
		   ) {
	   //Start exstraction info from layer
	 
	   final SimpleFeatureType original = layer.getSchema();
	   CoordinateReferenceSystem coordinatesystem = original.getCoordinateReferenceSystem();
	   Set<ReferenceIdentifier> sid =coordinatesystem.getIdentifiers();
	   
	   String epscCode = null;
	   
	   for (ReferenceIdentifier ri:sid){
		   if(ri.getCode()!=null){
			   epscCode=ri.getCode();
			   break;

		   }
	   }
	   
	   //variable name:
	   //residential
	   //String id = "id";
	   //String floor = "floors";
	   //String height = "height_val";
	   //String date = "date";
	   //String aveFloor =null;
	   //String refurbihment = "refurbihment";
	   //String residential = "residential";
	   
	   //original.ge
	  Date data = new Date();
	  String nomeTabella = "tmp"+data.getTime();
	  
 
	   
	   try {
		   EnergyDBService  dbEnergy= new EnergyDBService("wps_energy",nomeTabella,epscCode);
		   
		   dbEnergy.createEnergyTable();
		   logger.debug("creata tabella");
		   LinkedList<EnergyModel> listaBuilding=generateListeFeature(layer,  id,  floors, height, begin,end, refurbihment, residential, aveFloor);
		   String code =dbEnergy.getClimaticCode(climaticZone);
		   dbEnergy.insertInto(listaBuilding,code);
		   logger.debug("caricato il layer");
		   
		   dbEnergy.updateALL(code);
		   logger.debug("calcolo degli indici di ep completato ");
		   LinkedList<EnergyModel> listaCertificate=dbEnergy.getResult();
		   SimpleFeatureCollection createSimpleFeatureCollection = createSimpleFeatureCollection(listaCertificate,layer);
		   dbEnergy.deleteEnergytable();
		   logger.debug("creato layer di ritorno");
	//	   dbEnergy.insertInto(buildingList);
		   return createSimpleFeatureCollection;

	   } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 logger.error("errore sql controllare le funzioni");
			return null;
	   }
	   //SimpleFeatureCollection result = new S
	   catch (SchemaException e) {
			  //TODO Auto-generated catch block
			  e.printStackTrace();
			  logger.error("schema error");
			  return null;
	   } catch (ParseException e) {
		// TODO Auto-generated catch block
		   logger.error("ParseException");
		e.printStackTrace();
		return null;
	} catch (ClimaticZoneException e) {
		// TODO Auto-generated catch block
		logger.error("the code does't exist");
		e.printStackTrace();
		return null;
	}

   }
   
   public static SimpleFeatureCollection createSimpleFeatureCollection(LinkedList<EnergyModel> listaBuilding, SimpleFeatureCollection features) throws SchemaException{
	   GeometryFactory geomFactory = new GeometryFactory();
       List<SimpleFeature> list = new ArrayList<SimpleFeature>();
       //SimpleFeatureType type = DataUtilities.createType("building","geom:MultiPolygon:srid=3044,name:String,age:Integer");

       	SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName(features.getSchema().getName());
		tb.setCRS(features.getSchema().getCoordinateReferenceSystem());
				        
		tb.add("geom", MultiPolygon.class);
		tb.add("id", String.class);
		tb.add("year", Integer.class);
		tb.add("floor", Integer.class);
		tb.add("heigth", Integer.class);
		tb.add("refurbishm", String.class);
		tb.add("area",Double.class);
		tb.add("perimeter", Double.class);
		tb.add("commonPer",Double.class);
		tb.add("typology",Double.class);
		tb.add("residential",Boolean.class);
		
		tb.add("uRoof",Double.class);
		tb.add("uFloor",Double.class);
		tb.add("pWind",Double.class);
		tb.add("uWall",Double.class);
		tb.add("uWin",Double.class);
		tb.add("delataU",Double.class);
		
		tb.add("epi",Double.class);
		tb.add("epe",Double.class);
		tb.add("ephw",Double.class);
		
				        
		tb.setName(features.getSchema().getName());
		SimpleFeatureType type = tb.buildFeatureType();
       

       for (EnergyModel em:listaBuilding){
    	   PGobject pg = new PGobject();
    	   
    	   //em.getGeometry().
    	   SimpleFeature feature1 = SimpleFeatureBuilder.build( type, new Object[]{ 
    			   em.getGeometry(),em.getId(), em.getYearOfCostruction(), em.getFloor(), em.getHeigth(), em.getRefurbishment(),
    			   em.getArea(), em.getPerimeter(), em.getCommonPer(),em.getTypology(), em.getResidential(),
    			   em.getuRoof(),em.getuFloor(),em.getpWind(), em.getuWall(), em.getuWin(), em.getDeltaU(),
    			   em.getEpi(), em.getEpe(),em.getEphw()
    	   }, null );
    	   list.add(feature1);
       }
       
       
       //SimpleFeatureCollection collection = DataUtilities.collection( list );    
       SimpleFeatureCollection collection =new ListFeatureCollection(type,list );
	   
	   return collection;
       
    
	   
	   
   }

   
   public static LinkedList<EnergyModel>  generateListeFeature(SimpleFeatureCollection map, String id, String floor,String height,String begin , String end,String refurbihment,String residential,String avg_floor){
	   SimpleFeatureIterator iteratoreFeature = map.features();
	   LinkedList<EnergyModel> buildingList = new LinkedList<EnergyModel>();
	   while( iteratoreFeature.hasNext() ){
   		   
	        SimpleFeature feature = iteratoreFeature.next();
	        EnergyModel em = new EnergyModel();
	        
	        //classid,zone,ave_floor,\"end\",floors,height_val) values

	        em.setId(Utils.getFeatureString(feature,id));
	        em.setFloor(Utils.getFeatureDouble(feature, floor));
	        em.setHeigth(Utils.getFeatureDouble(feature, height));
	        
	        Integer beginInt = Utils.getFeatureInteger(feature, begin);
	        Integer endInt = Utils.getFeatureInteger(feature, end);
	        Integer date = 0;
	         
	        if (beginInt != null && beginInt> 0 ){
	        	date = beginInt;
	        	if (endInt!=null && endInt>0 ){
	        		date = (endInt +beginInt )/2;
	        	}
	        		
	        }else{
	        	date = endInt;	        	
	        }
	        em.setYearOfCostruction(date);	       
	        em.setAve_flor(Utils.getFeatureDouble(feature, avg_floor));
	        	
	        if(residential == null ||residential.trim().equals("")){
	        	em.setResidential(true);
	        }else{
	        	em.setResidential(Utils.getFeatureBoolean(feature,residential));
	        }
	       
	        if(refurbihment == null ||refurbihment.trim().equals("")){
	        	em.setRefurbishment("standard");
	        }else{
	 	       Integer re = Utils.getFeatureInteger(feature, refurbihment);
	           if(re==null||re==1) em.setRefurbishment("standard");
	           else if (re == 0 ) em.setRefurbishment("norefurbishment");
	           else if (re == 2) em.setRefurbishment("advanced");
	           else {}//TODO lanciare un ecezione}
	        }
	        //TODO aggiungere residential and refurbishment
	        
	        
	        
	        //TODO controllare bene che tipo di conversione va fatta
	        Geometry myGeom = (Geometry) feature.getDefaultGeometry();
	        em.setGeometry(myGeom);
	        buildingList.add(em);
	        
	        
	    }
	return buildingList;
   }

   
   
   
   
   
}