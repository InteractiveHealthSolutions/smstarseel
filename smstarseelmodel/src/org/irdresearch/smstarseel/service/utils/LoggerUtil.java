package org.irdresearch.smstarseel.service.utils;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;

public class LoggerUtil {

	private static Logger logger= Logger.getLogger("rollingFile");

	public static void logIt(String o){
		logger.error("\n"+new Date()+":"+o);
	}

	public static String getJVMInfo() {
		final long  MEGABYTE = 1024L * 1024L;
		StringBuilder sb=new StringBuilder();
		sb.append("\n*********IN MB*******" +
				  "\nAVAILABLE MEMORY : "+Runtime.getRuntime().freeMemory()/MEGABYTE);
		sb.append("\nTOTAL MEMORY   : "+Runtime.getRuntime().totalMemory()/MEGABYTE);
		sb.append("\nMAXIMUM MEMORY : "+Runtime.getRuntime().maxMemory()/MEGABYTE);
		return sb.toString();
	}
	public static StringBuilder getHibernatestats(Statistics stats){
		StringBuilder sb=new StringBuilder();
		sb.append("\nHIBERNATE STATISTICS");
		sb.append("\n**************************\n");

		//Number of connection requests. Note that this number represents 
		//the number of times Hibernate asked for a connection, and 
		//NOT the number of connections (which is determined by your 
		//pooling mechanism).
		sb.append("\n--ConnectCount : "+stats.getConnectCount());
		//Number of flushes done on the session (either by client code or 
		//by hibernate).
		sb.append("\n--FlushCount : "+stats.getFlushCount());
		//The number of completed transactions (failed and successful).
		sb.append("\n--TransactionCount : "+stats.getTransactionCount());
		//The number of transactions completed without failure
		sb.append("\n--SuccessfulTransactionCount : "+stats.getSuccessfulTransactionCount());
		//The number of sessions your code has opened.
		sb.append("\n--SessionOpenCount : "+stats.getSessionOpenCount());
		//The number of sessions your code has closed.
		sb.append("\n--SessionCloseCount : "+stats.getSessionCloseCount());
		//All of the queries that have executed.
		sb.append("\n--Queries : "+stats.getQueries());
		//Total number of queries executed.
		sb.append("\n--QueryExecutionCount : "+stats.getQueryExecutionCount());
		//Time of the slowest query executed.
		sb.append("\n--QueryExecutionMaxTime : "+stats.getQueryExecutionMaxTime());
		//the number of collections fetched from the DB.
		sb.append("\n--CollectionFetchCount : "+stats.getCollectionFetchCount());
		// The number of collections loaded from the DB.
		sb.append("\n--CollectionLoadCount : "+stats.getCollectionLoadCount());
		// The number of collections that were rebuilt
		sb.append("\n--CollectionRecreateCount : "+stats.getCollectionRecreateCount());
		// The number of collections that were 'deleted' batch.
		sb.append("\n--CollectionRemoveCount : "+stats.getCollectionRemoveCount());
		// The number of collections that were updated batch.
		sb.append("\n--CollectionUpdateCount : "+stats.getCollectionUpdateCount());
		 
		// The number of your objects deleted.
		sb.append("\n--EntityDeleteCount : "+stats.getEntityDeleteCount());
		// The number of your objects fetched.
		sb.append("\n--EntityFetchCount : "+stats.getEntityFetchCount());
		// The number of your objects actually loaded (fully populated).
		sb.append("\n--EntityLoadCount : "+stats.getEntityLoadCount());
		// The number of your objects inserted.
		sb.append("\n--EntityInsertCount : "+stats.getEntityInsertCount());
		// The number of your object updated.
		sb.append("\n--EntityUpdateCount : "+stats.getEntityUpdateCount());
		double queryCacheHitCount  = stats.getQueryCacheHitCount();
		double queryCacheMissCount = stats.getQueryCacheMissCount();
		double queryCacheHitRatio =
		  queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);
		sb.append("\n--queryCacheHitCount : "+queryCacheHitCount);
		sb.append("\n--queryCacheMissCount : "+queryCacheMissCount);
		sb.append("\n--queryCacheHitRatio : "+queryCacheHitRatio);
		
		sb.append("\n\n** ENTITY STATS **");

		for (String ent : stats.getEntityNames()) {
			sb.append("\n\nEntity Name : "+ent);
			EntityStatistics entityStats = stats.getEntityStatistics(ent); // or Sale.class.getName();
			//exactly the same as the global values, but for a single entity class.
			sb.append("\n--FetchCount : "+entityStats.getFetchCount());
			sb.append("\n--LoadCount : "+entityStats.getLoadCount());
			sb.append("\n--InsertCount : "+entityStats.getInsertCount());
			sb.append("\n--UpdateCount : "+entityStats.getUpdateCount());
			sb.append("\n--DeleteCount : "+entityStats.getDeleteCount());
		}
		sb.append("\n\n** Second Level Cache Stats **");
		for (String slv : stats.getSecondLevelCacheRegionNames()) {
			SecondLevelCacheStatistics cacheStats = stats.getSecondLevelCacheStatistics(slv);
			sb.append("\n--ElementCountInMemory : "+cacheStats.getElementCountInMemory());
			sb.append("\n--ElementCountOnDisk : "+cacheStats.getElementCountOnDisk());
			sb.append("\n--Entries : "+cacheStats.getEntries());
			sb.append("\n--HitCount : "+cacheStats.getHitCount());
			sb.append("\n--MissCount : "+cacheStats.getMissCount());
			sb.append("\n--PutCount : "+cacheStats.getPutCount());
			sb.append("\n--SizeInMemory : "+cacheStats.getSizeInMemory());
		}
		return sb;
	}
}
