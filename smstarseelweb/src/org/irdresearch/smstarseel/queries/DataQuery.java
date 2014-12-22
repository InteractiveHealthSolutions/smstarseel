package org.irdresearch.smstarseel.queries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataQuery {

	public static String SUMMARY_1BY7_OUTBOUND_QUERY = getQuery("Summary1by7OutboundQuery.sql");
	public static String SUMMARY_1BY7_INBOUND_QUERY = getQuery("Summary1by7InboundQuery.sql");
	public static String SUMMARY_1BY7_CALLLOG_QUERY = getQuery("Summary1by7CalllogQuery.sql");
	public static String QUICK_SUMMARY_OUTBOUND_QUERY = getQuery("QuickSummaryOutboundQuery.sql");
	public static String QUICK_SUMMARY_INBOUND_QUERY = getQuery("QuickSummaryInboundQuery.sql");
	public static String QUICK_SUMMARY_CALLLOG_QUERY = getQuery("QuickSummaryCallLogQuery.sql");
	public static String DAILY_SUMMARY_INBOUND_QUERY = getQuery("DailySummaryInboundQuery.sql");
	public static String DAILY_SUMMARY_OUTBOUND_QUERY = getQuery("DailySummaryOutboundQuery.sql");
	public static String DAILY_SUMMARY_CALLLOG_QUERY = getQuery("DailySummaryCallLogQuery.sql");


	private static String getQuery(String fileName){
		try{
			InputStream in = DataQuery.class.getResourceAsStream("/org/irdresearch/smstarseel/queries/"+fileName);
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			StringBuilder stringJson = new StringBuilder();
	
			int chunksize = 1024;
			char[] charBuffer = new char[chunksize];
		    int count = 0;
	
		    do {
		    	count = r.read(charBuffer, 0, chunksize);
	
		    	if (count >= 0) {
		    		stringJson.append(charBuffer, 0, count);
		    	}
		    } while (count>0);
		    
		    r.close();
		        
			return stringJson.toString();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
 