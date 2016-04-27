package eu.geosmartcity.hub.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.geoserver.wps.WPSException;

public class DateSolarUtils {
	
	private static final Logger LOGGER = Logger.getLogger(DateSolarUtils.class);
	
	/**
	 * restituisce il giorno dell'anno
	 * 
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	public static int getDayOfYear(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date date = sdf.parse(dateString);
		GregorianCalendar gc = new GregorianCalendar();
		
		gc.setTime(date);
		int dayOfYear = gc.get(GregorianCalendar.DAY_OF_YEAR);
		LOGGER.info("Day of year " + dayOfYear);
		return dayOfYear;
	}
	
	/**
	 * restituisce il range di giorni per il calcolo del r.sun
	 * 
	 * @param startDay
	 * @param endDay
	 * @return il range di giorni indicato in questo formato startDay-endDay (es: 172-174)
	 */
	public static String getRangeDay(String startDay, String endDay) {
		try {
			int startDayOfYear = getDayOfYear(startDay);
			
			int endDayOfYear = startDayOfYear;
			if (endDay != null && !endDay.equals("")) {
				endDayOfYear = getDayOfYear(endDay);
			}
			
			if (startDayOfYear > endDayOfYear) {
				throw new WPSException("the start day " + startDay + " is more big than end date " + endDay);
			}
			
			return startDayOfYear + "-" + endDayOfYear;
		}
		catch (ParseException e) {
			throw new WPSException("date formats for start day " + startDay + " or for end date " + endDay
					+ " are wrong, the valid format is " + Constants.DATE_FORMAT);
		}
		
	}
}
