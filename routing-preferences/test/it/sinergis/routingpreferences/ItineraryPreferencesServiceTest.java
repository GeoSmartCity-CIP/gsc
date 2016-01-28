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
public class ItineraryPreferencesServiceTest 
{
	static String itineraryId;
	
	@Test
	public void test_01_SaveItinerary() {
		
		System.out.println("Test save itinerary ..." );
		
		ItineraryPreferencesService itineraryPreferencesService = new ItineraryPreferencesService();
		String testo = StringEscapeUtils.unescapeEcmaScript("{\"requestParameters\":{\"date\":\"11-17-2015\",\"mode\":\"TRANSIT,WALK\",\"arriveBy\":\"false\",\"wheelchair\":\"false\" ,\"fromPlace\":\"60.46060328697707,22.302932739257812\",\"toPlace\":\"60.45620208214136,22.258987426757812\" ,\"time\":\"10:07am\",\"maxWalkDistance\":\"804.672\",\"locale\":\"en\"},\"plan\":{\"date\":1447747620000,\"from\":{\"name\" :\"path\",\"lon\":22.302858254808324,\"lat\":60.46059975991341,\"orig\":\"\",\"vertexType\":\"NORMAL\"},\"to\":{\"name\" :\"Platform 2 & 3\",\"lon\":22.25931475941859,\"lat\":60.455955773601765,\"orig\":\"\",\"vertexType\":\"NORMAL\"},\"itineraries\" :[{\"duration\":1577,\"startTime\":1447748369000,\"endTime\":1447749946000,\"walkTime\":735,\"transitTime\":780 ,\"co2\":186,\"waitingTime\":62,\"walkDistance\":912.1807067577952,\"walkLimitExceeded\":false,\"elevationLost\" :0.0,\"elevationGained\":0.0,\"transfers\":1,\"legs\":[{\"startTime\":1447748369000,\"endTime\":1447748639000,\"departureDelay\" :0,\"arrivalDelay\":0,\"realTime\":false,\"distance\":330.7799999999999,\"co2\":0,\"pathway\":false,\"mode\":\"WALK\" ,\"route\":\"\",\"agencyTimeZoneOffset\":7200000,\"interlineWithPreviousLeg\":false,\"from\":{\"name\":\"path\",\"lon\" :22.302858254808324,\"lat\":60.46059975991341,\"departure\":1447748369000,\"orig\":\"\",\"vertexType\":\"NORMAL\" },\"to\":{\"name\":\"Suntiontie\",\"stopId\":\"16:1647\",\"lon\":22.29821230935111,\"lat\":60.45981359254044,\"arrival\" :1447748639000,\"departure\":1447748640000,\"stopIndex\":7,\"stopSequence\":7,\"vertexType\":\"TRANSIT\"},\"legGeometry\" :{\"points\":\"uu_pJy_cgCdAMAXvpPFRLJnhKpAObAETMbAHJCV\",\"length\":13},\"rentedBike\":false,\"transitLeg\" :false,\"duration\":270.0,\"steps\":[{\"distance\":37.779,\"relativeDirection\":\"DEPART\",\"streetName\":\"path\" ,\"absoluteDirection\":\"SOUTH\",\"stayOn\":false,\"area\":false,\"bogusName\":false,\"lon\":22.302858254808324,\"lat\" :60.46059975991341,\"elevation\":[]},{\"distance\":208.618,\"relativeDirection\":\"RIGHT\",\"streetName\":\"Elinantie\" ,\"absoluteDirection\":\"WEST\",\"stayOn\":false,\"area\":false,\"bogusName\":false,\"lon\":22.3029276,\"lat\":60.4602438 ,\"elevation\":[]},{\"distance\":70.892,\"relativeDirection\":\"RIGHT\",\"streetName\":\"bike path\",\"absoluteDirection\" :\"WEST\",\"stayOn\":false,\"area\":false,\"bogusName\":true,\"lon\":22.299615,\"lat\":60.4596293,\"elevation\":[] },{\"distance\":6.417,\"relativeDirection\":\"LEFT\",\"streetName\":\"service road\",\"absoluteDirection\":\"SOUTHWEST\" ,\"stayOn\":true,\"area\":false,\"bogusName\":true,\"lon\":22.298418,\"lat\":60.459862,\"elevation\":[]},{\"distance\" :7.074,\"relativeDirection\":\"RIGHT\",\"streetName\":\"Nummenpuistokatu\",\"absoluteDirection\":\"NORTHWEST\",\"stayOn\" :false,\"area\":false,\"bogusName\":false,\"lon\":22.298353,\"lat\":60.459814,\"elevation\":[]}]},{\"startTime\" :1447748640000,\"endTime\":1447749240000,\"departureDelay\":0,\"arrivalDelay\":0,\"realTime\":false,\"distance\" :2664.159061801866,\"co2\":149,\"pathway\":false,\"mode\":\"BUS\",\"route\":\"54\",\"agencyName\":\"Turun Kaupunkiliikenne Oy\",\"agencyUrl\":\"https://www.google.fi/\",\"agencyTimeZoneOffset\":7200000,\"routeColor\":\"000000\",\"routeType\" :3,\"routeId\":\"31\",\"routeTextColor\":\"ffffff\",\"interlineWithPreviousLeg\":false,\"tripBlockId\":\"5851generatedBlock\" ,\"headsign\":\"Papinsaari\",\"agencyId\":\"8\",\"tripId\":\"0000null__5851generatedBlock\",\"serviceDate\":\"20151117\" ,\"from\":{\"name\":\"Suntiontie\",\"stopId\":\"16:1647\",\"lon\":22.29821230935111,\"lat\":60.45981359254044,\"arrival\" :1447748639000,\"departure\":1447748640000,\"stopIndex\":7,\"stopSequence\":7,\"vertexType\":\"TRANSIT\"},\"to\" :{\"name\":\"Kauppatori\",\"stopId\":\"16:T41\",\"lon\":22.26535486358474,\"lat\":60.45030483780314,\"arrival\":1447749240000 ,\"departure\":1447749300000,\"stopIndex\":14,\"stopSequence\":14,\"vertexType\":\"TRANSIT\"},\"legGeometry\":{\"points\" :\"\",\"length\":95 },\"routeShortName\":\"54\",\"rentedBike\":false,\"transitLeg\":true,\"duration\":600.0,\"steps\":[]},{\"startTime\" :1447749300000,\"endTime\":1447749480000,\"departureDelay\":0,\"arrivalDelay\":0,\"realTime\":false,\"distance\" :670.1129612173811,\"co2\":37,\"pathway\":false,\"mode\":\"BUS\",\"route\":\"20\",\"agencyName\":\"Savonlinja Oy/SL-Autolinjat\" ,\"agencyUrl\":\"https://www.google.fi/\",\"agencyTimeZoneOffset\":7200000,\"routeColor\":\"000000\",\"routeType\" :3,\"routeId\":\"14\",\"routeTextColor\":\"ffffff\",\"interlineWithPreviousLeg\":false,\"tripBlockId\":\"3265generatedBlock\" ,\"headsign\":\"Muhkuri\",\"agencyId\":\"11\",\"tripId\":\"0000null__3265generatedBlock\",\"serviceDate\":\"20151117\" ,\"from\":{\"name\":\"Kauppatori\",\"stopId\":\"16:T41\",\"lon\":22.26535486358474,\"lat\":60.45030483780314,\"arrival\" :1447749240000,\"departure\":1447749300000,\"stopIndex\":0,\"stopSequence\":0,\"vertexType\":\"TRANSIT\"},\"to\" :{\"name\":\"Läntinen Pitkäkatu\",\"stopId\":\"16:150\",\"lon\":22.2565524186888,\"lat\":60.45262555805728,\"arrival\" :1447749480000,\"departure\":1447749481000,\"stopIndex\":3,\"stopSequence\":3,\"vertexType\":\"TRANSIT\"},\"legGeometry\" :{\"points\":\"\",\"length\":21},\"routeShortName\" :\"20\",\"rentedBike\":false,\"transitLeg\":true,\"duration\":180.0,\"steps\":[]},{\"startTime\":1447749481000,\"endTime\" :1447749946000,\"departureDelay\":0,\"arrivalDelay\":0,\"realTime\":false,\"distance\":581.1179999999999,\"co2\" :0,\"pathway\":false,\"mode\":\"WALK\",\"route\":\"\",\"agencyTimeZoneOffset\":7200000,\"interlineWithPreviousLeg\" :false,\"from\":{\"name\":\"Läntinen Pitkäkatu\",\"stopId\":\"16:150\",\"lon\":22.2565524186888,\"lat\":60.45262555805728 ,\"arrival\":1447749480000,\"departure\":1447749481000,\"stopIndex\":3,\"stopSequence\":3,\"vertexType\":\"TRANSIT\" },\"to\":{\"name\":\"Platform 2 & 3\",\"lon\":22.25931475941859,\"lat\":60.455955773601765,\"arrival\":1447749946000 ,\"orig\":\"\",\"vertexType\":\"NORMAL\"},\"legGeometry\":{\"points\":\"\",\"length\":16},\"rentedBike\":false,\"transitLeg\":false,\"duration\":465.0,\"steps\":[{\"distance\":261.914,\"relativeDirection\":\"DEPART\",\"streetName\":\"path\",\"absoluteDirection\":\"NORTHWEST\",\"stayOn\":false ,\"area\":false,\"bogusName\":true,\"lon\":22.256540729416283,\"lat\":60.45262149696433,\"elevation\":[]},{\"distance\" :319.204,\"relativeDirection\":\"CONTINUE\",\"streetName\":\"Platform 2 & 3\",\"absoluteDirection\":\"NORTHEAST\" ,\"stayOn\":false,\"area\":false,\"bogusName\":false,\"lon\":22.2544454,\"lat\":60.4543824,\"elevation\":[]}]}],\"tooSloped\" :false},{\"duration\":1791,\"startTime\":1447747755000,\"endTime\":1447749546000,\"walkTime\":529,\"transitTime\" :1260,\"co2\":231,\"waitingTime\":2,\"walkDistance\":671.887233078085,\"walkLimitExceeded\":false,\"elevationLost\" :0.0,\"elevationGained\":0.0,\"transfers\":1,\"legs\":[{\"startTime\":1447747755000,\"endTime\":1447747919000,\"departureDelay\" :0,\"arrivalDelay\":0,\"realTime\":false,\"distance\":209.41,\"co2\":0,\"pathway\":false,\"mode\":\"WALK\",\"route\" :\"\",\"agencyTimeZoneOffset\":7200000,\"interlineWithPreviousLeg\":false,\"from\":{\"name\":\"path\",\"lon\":22.302858254808324 ,\"lat\":60.46059975991341,\"departure\":1447747755000,\"orig\":\"\",\"vertexType\":\"NORMAL\"},\"to\":{\"name\":\"Elinantie\" ,\"stopId\":\"16:214\",\"lon\":22.30531512232241,\"lat\":60.4605499020207,\"arrival\":1447747919000,\"departure\" :1447747920000,\"stopIndex\":40,\"stopSequence\":40,\"vertexType\":\"TRANSIT\"},\"legGeometry\":{\"points\":\"\",\"length\":6},\"rentedBike\":false,\"transitLeg\":false,\"duration\":164.0,\"steps\":[{\"distance\" :37.779,\"relativeDirection\":\"DEPART\",\"streetName\":\"path\",\"absoluteDirection\":\"SOUTH\",\"stayOn\":false,\"area\" :false,\"bogusName\":false,\"lon\":22.302858254808324,\"lat\":60.46059975991341,\"elevation\":[]},{\"distance\" :39.186,\"relativeDirection\":\"SLIGHTLY_LEFT\",\"streetName\":\"Elinantie\",\"absoluteDirection\":\"SOUTHEAST\" ,\"stayOn\":false,\"area\":false,\"bogusName\":false,\"lon\":22.3029276,\"lat\":60.4602438,\"elevation\":[]},{\"distance\" :132.445,\"relativeDirection\":\"LEFT\",\"streetName\":\"Hakapellonkatu\",\"absoluteDirection\":\"NORTHEAST\",\"stayOn\" :false,\"area\":false,\"bogusName\":false,\"lon\":22.303252,\"lat\":60.4599298,\"elevation\":[]}]},{\"startTime\" :1447747920000,\"endTime\":1447748520000,\"departureDelay\":0,\"arrivalDelay\":0,\"realTime\":false,\"distance\" :2773.3690879664905,\"co2\":155,\"pathway\":false,\"mode\":\"BUS\",\"route\":\"P2\",\"agencyName\":\"V-S Bussipalvelut Oy\",\"agencyUrl\":\"https://www.google.fi/\",\"agencyTimeZoneOffset\":7200000,\"routeColor\":\"000000\",\"routeType\" :3,\"routeId\":\"61\",\"routeTextColor\":\"ffffff\",\"interlineWithPreviousLeg\":false,\"tripBlockId\":\"8402generatedBlock\" ,\"headsign\":\"Kauppatori\",\"agencyId\":\"55\",\"tripId\":\"0000null__8402generatedBlock\",\"serviceDate\":\"20151117\" ,\"from\":{\"name\":\"Elinantie\",\"stopId\":\"16:214\",\"lon\":22.30531512232241,\"lat\":60.4605499020207,\"arrival\" :1447747919000,\"departure\":1447747920000,\"stopIndex\":40,\"stopSequence\":40,\"vertexType\":\"TRANSIT\"},\"to\" :{\"name\":\"Brahenkatu\",\"stopId\":\"16:68\",\"lon\":22.27260809114815,\"lat\":60.45286445766308,\"arrival\":1447748520000 ,\"departure\":1447748520000,\"stopIndex\":50,\"stopSequence\":50,\"vertexType\":\"TRANSIT\"},\"legGeometry\":{\"points\" :\"\",\"length\" :135},\"routeShortName\":\"P2\",\"rentedBike\":false,\"transitLeg\":true,\"duration\":600.0,\"steps\":[]},{\"startTime\" :1447748520000,\"endTime\":1447749180000,\"departureDelay\":0,\"arrivalDelay\":0,\"realTime\":false,\"distance\" :1360.6090564551719,\"co2\":76,\"pathway\":false,\"mode\":\"BUS\",\"route\":\"32\",\"agencyName\":\"LS-Liikennelinjat Oy\",\"agencyUrl\":\"https://www.google.fi/\",\"agencyTimeZoneOffset\":7200000,\"routeColor\":\"000000\",\"routeType\" :3,\"routeId\":\"21\",\"routeTextColor\":\"ffffff\",\"interlineWithPreviousLeg\":false,\"tripBlockId\":\"4926generatedBlock\" ,\"headsign\":\"Pansio\",\"agencyId\":\"3\",\"tripId\":\"0000null__4926generatedBlock\",\"serviceDate\":\"20151117\" ,\"from\":{\"name\":\"Brahenkatu\",\"stopId\":\"16:68\",\"lon\":22.27260809114815,\"lat\":60.45286445766308,\"arrival\" :1447748520000,\"departure\":1447748520000,\"stopIndex\":25,\"stopSequence\":25,\"vertexType\":\"TRANSIT\"},\"to\" :{\"name\":\"Rautatieasema\",\"stopId\":\"16:151\",\"lon\":22.25337933315098,\"lat\":60.45353733461038,\"arrival\" :1447749180000,\"departure\":1447749181000,\"stopIndex\":30,\"stopSequence\":30,\"vertexType\":\"TRANSIT\"},\"legGeometry\" :{\"points\":\"\",\"length\":41},\"routeShortName\":\"32\",\"rentedBike\":false,\"transitLeg\":true ,\"duration\":660.0,\"steps\":[]},{\"startTime\":1447749181000,\"endTime\":1447749546000,\"departureDelay\":0,\"arrivalDelay\" :0,\"realTime\":false,\"distance\":462.334,\"co2\":0,\"pathway\":false,\"mode\":\"WALK\",\"route\":\"\",\"agencyTimeZoneOffset\" :7200000,\"interlineWithPreviousLeg\":false,\"from\":{\"name\":\"Rautatieasema\",\"stopId\":\"16:151\",\"lon\":22.25337933315098 ,\"lat\":60.45353733461038,\"arrival\":1447749180000,\"departure\":1447749181000,\"stopIndex\":30,\"stopSequence\" :30,\"vertexType\":\"TRANSIT\"},\"to\":{\"name\":\"Platform 2 & 3\",\"lon\":22.25931475941859,\"lat\":60.455955773601765 ,\"arrival\":1447749546000,\"orig\":\"\",\"vertexType\":\"NORMAL\"},\"legGeometry\":{\"points\":\"\",\"length\":10},\"rentedBike\":false,\"transitLeg\":false,\"duration\":365.0,\"steps\":[{\"distance\" :59.989999999999995,\"relativeDirection\":\"DEPART\",\"streetName\":\"Ratapihankatu\",\"absoluteDirection\":\"NORTHEAST\" ,\"stayOn\":false,\"area\":false,\"bogusName\":false,\"lon\":22.253429652304234,\"lat\":60.45350124922285,\"elevation\" :[]},{\"distance\":83.14,\"relativeDirection\":\"LEFT\",\"streetName\":\"path\",\"absoluteDirection\":\"NORTH\",\"stayOn\" :false,\"area\":false,\"bogusName\":true,\"lon\":22.2543312,\"lat\":60.4538069,\"elevation\":[]},{\"distance\":319.204,\"relativeDirection\":\"CONTINUE\",\"streetName\":\"Platform 2 & 3\",\"absoluteDirection\":\"NORTHEAST\",\"stayOn\" :false,\"area\":false,\"bogusName\":false,\"lon\":22.2544454,\"lat\":60.4543824,\"elevation\":[]}]}],\"tooSloped\" :false}]},\"debugOutput\":{\"precalculationTime\":628,\"pathCalculationTime\":944,\"pathTimes\":[545,316],\"renderingTime\" :67,\"totalTime\":1639,\"timedOut\":true}}");
		String responseJSON = itineraryPreferencesService.saveItinerary(testo,"1","1","No notes");
		Assert.assertTrue(responseJSON.indexOf("itineraryId")>0) ;
		itineraryId = responseJSON.substring(responseJSON.indexOf("itineraryId")+14).substring(0,responseJSON.indexOf("\"")+1);
			
		System.out.println("Save Itinerary response = " + responseJSON );
	}
	
	@Test
	public void test_02_GetItinerary() {
		
		System.out.println("Test get itinerary ..." );
		
		ItineraryPreferencesService itineraryPreferencesService = new ItineraryPreferencesService();
		String responseJSON = itineraryPreferencesService.getItineraries("'requestParameters'/'date' = '11-17-2015'");
		Assert.assertTrue(responseJSON.indexOf("\"duration\":1577")>0) ;
		
		System.out.println("Get Itinerary response = " + responseJSON );
	}

	@Test
	public void test_03_DeleteItinerary() {
		
		System.out.println("Test delete itinerary ..." );
		
		ItineraryPreferencesService itineraryPreferencesService = new ItineraryPreferencesService();
		String responseJSON = itineraryPreferencesService.deleteItinerary(Long.valueOf(itineraryId));
		Assert.assertTrue(responseJSON.indexOf("itineraryId")>0) ;
		
		System.out.println("Delete Itinerary response = " + responseJSON );
	}
}