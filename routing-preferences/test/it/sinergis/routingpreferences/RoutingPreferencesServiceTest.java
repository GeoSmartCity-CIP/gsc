package it.sinergis.routingpreferences;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 * JUnit test case for Itinerary Preferences Service
 * 
 * @author Andrea Di Nora
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoutingPreferencesServiceTest 
{
	@Test
	public void test_01_SaveOrUpdatePreferences() {
		
		System.out.println("Test save update preferences ..." );
		
		RoutingPreferencesService routingPreferencesService = new RoutingPreferencesService();
		String testo = StringEscapeUtils.unescapeEcmaScript("{\"userId\":1,\"maxWalkingDistance\":10,\"walkingSpeed\":10,\"maxBikingDistance\":10,\"bikingSpeed\":10}");
		String responseJSON = routingPreferencesService.saveOrUpdatePreferences(testo);
		Assert.assertTrue(responseJSON.indexOf("userId")>0) ;
			
		System.out.println("Save preferencese response = " + responseJSON );
	}
	
	@Test
	public void test_02_Preferences() {
		
		System.out.println("Test get preferences ..." );
		
		RoutingPreferencesService routingPreferencesService = new RoutingPreferencesService();
		String responseJSON = routingPreferencesService.getPreferences("{\"userId\":1}");
		Assert.assertTrue(responseJSON.indexOf("\"maxWalkingDistance\":10")>0) ;
		
		System.out.println("Get preferences response = " + responseJSON );
	}

	@Test
	public void test_03_DeleteItinerary() {
		
		System.out.println("Test delete preferences ..." );
		
		RoutingPreferencesService routingPreferencesService = new RoutingPreferencesService();
		String responseJSON = routingPreferencesService.deletePreferences("{\"userId\":1}");
		Assert.assertTrue(responseJSON.indexOf("\"userId\":\"1\"")>0) ;
		
		System.out.println("Delete preferences response = " + responseJSON );
	}
}
