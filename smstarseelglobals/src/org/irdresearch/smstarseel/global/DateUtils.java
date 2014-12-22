package org.irdresearch.smstarseel.global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static SimpleDateFormat tarseelDF = new SimpleDateFormat();
	
	public static String formatRequestDate(Date date){
		tarseelDF.applyPattern(SmsTarseelGlobal.DEFAULT_DATE_FORMAT);
		tarseelDF.setLenient(false);
		return tarseelDF.format(date);
	}
	
	public static Date parseRequestDate(String date) throws ParseException{
		tarseelDF.applyPattern(SmsTarseelGlobal.DEFAULT_DATE_FORMAT);
		tarseelDF.setLenient(false);
		return tarseelDF.parse(date);
	}
}
