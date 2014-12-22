package org.irdresearch.smstarseel.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.queries.DataQuery;
import org.irdresearch.smstarseel.service.UserServiceException;
import org.irdresearch.smstarseel.service.utils.DateUtils;
import org.irdresearch.smstarseel.service.utils.DateUtils.TIME_INTERVAL;
import org.irdresearch.smstarseel.web.util.UserSessionUtils;
import org.irdresearch.smstarseel.web.util.WebGlobals;
import org.irdresearch.smstarseel.web.util.WebGlobals.UserQueryParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(value = "/genericdata/")
public class GenericDataController extends DataDisplayController{

	@Override
	String getData (Map modal) {
		return null;
	}
	
	@RequestMapping(value="/get_projects.do")
	public @ResponseBody List<Project> getProjectsList(HttpServletRequest request){
		TarseelServices tsc = TarseelContext.getServices();
		List<Project> prjsl = tsc.getDeviceService().getAllProjects(0, Integer.MAX_VALUE);
		return prjsl;
	}
	
	@RequestMapping(value="/quick_summary.do")
	public @ResponseBody Map<String, Object> quickSummary(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();

		String outboundGrp = "Outbounds (outgoing sms)";
		String inboundGrp = "Inbounds (incoming sms)";
		String callGrp = "Calls";
		String dateRangeGrp = "Summary Date Range";

		String[][] coldColumns = new String[][]{
				{"dueToday", "Due today"},
				{"pendingToday", "Pending today"},
				{"sentToday", "Sent today"},
				{"latestSent", "Latest sent"},
				{"latestDue", "Latest due"},
				{"dueLastWeek", "Due Total"},
				{"sentLastWeek", "Sent Total"},
				{"recipientsToday", "# Recipients today"},
				{"avgDailySentRate", "Avg sent rate/day"},
				{"weeklySentPercent", "% sent"} };
		
		String[][] cildColumns = new String[][]{
				{"latestReceived", "Latest received"},
				{"readToday", "Read today"},
				{"unreadToday", "Unread today"},
				{"originatorsToday", "# Originators today"},
				{"receivedLastWeek", "Received Total"},
				{"avgDailyReceiveRate", "Avg receive rate/day"}};
		
		String[][] ccldColumns = new String[][]{
				{"latestCallDate", "Latest call"},
				{"readToday", "Read today"},
				{"unreadToday", "Unread today"},
				{"callersToday", "# Callers today"},
				{"callsLastWeek", "Calls Total"},
				{"avgDailyCallRate", "Avg rate/day"}};
		
		List datalist = new ArrayList();
		
		addProperty(WebGlobals.GLOBAL_SDF_DATE.format(DateUtils.subtractInterval(new Date(), 14, TIME_INTERVAL.DATE))+" - ", WebGlobals.GLOBAL_SDF_DATE.format(new Date()), null, dateRangeGrp, datalist);

		Session ss = TarseelContext.getNewSession();
		try{
			List obsl = ss.createSQLQuery(DataQuery.QUICK_SUMMARY_OUTBOUND_QUERY).list();
			Object[] cold = (Object[]) obsl.get(0);
			for (int i = 0; i < coldColumns.length; i++) {
				addProperty(coldColumns[i][1], cold[i], coldColumns[i][1], outboundGrp, datalist);
			}
			
			List ibsl = ss.createSQLQuery(DataQuery.QUICK_SUMMARY_INBOUND_QUERY).list();
			Object[] cild = (Object[]) ibsl.get(0);
			for (int i = 0; i < cildColumns.length; i++) {
				addProperty(cildColumns[i][1], cild[i], cildColumns[i][1], inboundGrp, datalist);
			}
			
			List csl = ss.createSQLQuery(DataQuery.QUICK_SUMMARY_INBOUND_QUERY).list();
			Object[] ccld = (Object[]) csl.get(0);
			for (int i = 0; i < ccldColumns.length; i++) {
				addProperty(ccldColumns[i][1], ccld[i], ccldColumns[i][1], callGrp, datalist);
			}
		}
		finally {
			ss.close();
		}
		
		map.put("rows", datalist);
	    map.put("total", datalist.size());
	    
		return map;
	}
	
	private void addProperty(String name, Object value, String title, String group, List datalist){
		Map<String, String> row = new HashMap<String, String>();
		row.put("name", name);
		if(value != null && value instanceof Date){
			value = WebGlobals.GLOBAL_SDF_DATETME.format(value);
		}
		row.put("value", value!=null?value.toString():"");
		row.put("group", group);
		row.put("title", title);
		datalist.add(row);
	}
	@Override
	void setNavigationType (HttpServletRequest request) {
		// TODO Auto-generated method stub
	}
}
