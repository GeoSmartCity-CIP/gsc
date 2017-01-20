package it.sinergis.gsc.support;

import org.opengis.feature.simple.SimpleFeature;

public class Utils {
	   
	   public static String getFeatureString(SimpleFeature feature, String name){
		   if (name !=null && name.length()> 0 ){
			   return (String)feature.getAttribute(name);
	       }else return null;
	   }
	   
	   public static Integer getFeatureInteger(SimpleFeature feature, String name){
		   if (name !=null && name.length()> 0 ){
			   Object tmp = feature.getAttribute(name);
			   if (tmp instanceof Integer ){
				   return (Integer) tmp;
			   }else if (tmp instanceof Double ){
				   return Integer.valueOf(((Double) tmp).intValue());
			   }else if (tmp instanceof String ){
				   return Integer.parseInt(((String)tmp).trim());
			   }
			   return null;
	       }else return null;
	   }
	   
	   public static Boolean getFeatureBoolean(SimpleFeature feature, String name){
		   if (name !=null && name.length()> 0 ){
			   Object tmp = feature.getAttribute(name);
			   if (tmp instanceof Integer ){
				   Integer i = (Integer) tmp;
				   if (i==null) return null;
				   if (i==0) return false;
				   return true ;
			   }else if (tmp instanceof Boolean ){
				   return Boolean.valueOf(((Boolean) tmp).booleanValue());
			   }else if (tmp instanceof String ){
				   String stmp = ((String)tmp).trim();
				   if (stmp.equalsIgnoreCase("t" ))return true;
				   if (stmp.equalsIgnoreCase("true" ))return true;
				   if (stmp.equalsIgnoreCase("f" ))return true;
				   if (stmp.equalsIgnoreCase("false" ))return false;
				   return Boolean.parseBoolean(((String)tmp).trim());
			   }
			   return null;
	       }else return null;
	   }
	   
	   
	   
	   
	   public static Double getFeatureDouble(SimpleFeature feature, String name){
		   if (name !=null && name.length()> 0 ){		   
			   Object tmp = feature.getAttribute(name);
			   if (tmp instanceof Double ){
				   return (Double) tmp;
			   }else if (tmp instanceof Integer ){
				   return Double.valueOf(((Integer) tmp).intValue());
			   }else if (tmp instanceof String ){
				   return Double.parseDouble((String)tmp);
			   }
			   return null;
	       }else return null;
	   }
	   
}
