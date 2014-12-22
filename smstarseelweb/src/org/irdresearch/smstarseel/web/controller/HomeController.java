package org.irdresearch.smstarseel.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.data.Device.DeviceStatus;
import org.irdresearch.smstarseel.data.Setting;
import org.irdresearch.smstarseel.queries.DataQuery;
import org.irdresearch.smstarseel.service.utils.DateUtils;
import org.irdresearch.smstarseel.service.utils.DateUtils.TIME_INTERVAL;
import org.irdresearch.smstarseel.web.util.WebGlobals;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/home/", method = RequestMethod.GET)
public class HomeController extends DataDisplayController 
{
	private static String NAVIGATION_TYPE = "home";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@RequestMapping(value="home.htm", method = RequestMethod.GET)
	public String getData (Map modal) {
		Session ss = TarseelContext.getNewSession();
		try{
			modal.put("summaryDateRange", WebGlobals.GLOBAL_SDF_DATE.format(DateUtils.subtractInterval(new Date(), 7, TIME_INTERVAL.DATE))+" - "+WebGlobals.GLOBAL_SDF_DATE.format(new Date()));
		
			List oblist = ss.createSQLQuery(DataQuery.SUMMARY_1BY7_OUTBOUND_QUERY).list();
			modal.put("outboundSummary", oblist);
		
			List iblist = ss.createSQLQuery(DataQuery.SUMMARY_1BY7_INBOUND_QUERY).list();
			modal.put("inboundSummary", iblist);
		
			List cllist = ss.createSQLQuery(DataQuery.SUMMARY_1BY7_CALLLOG_QUERY).list();
			modal.put("calllogSummary", cllist);
		
			modal.put("projects", "Projects: "+(ss.createSQLQuery("SELECT GROUP_CONCAT(name, ' ') FROM project").list().get(0)));
		
			modal.put("devices", ss.createQuery("FROM Device WHERE status='"+DeviceStatus.ACTIVE+"'").list());
			
			Collection<Setting> setl = TarseelContext.refreshAndGetSettings().values();
			List<Map<Object, Object>> ml = new ArrayList<Map<Object,Object>>();
			for (Setting st : setl) {
				ml.add(makeKeyValue(st.getDisplayName(), st.getValue()));
			}
			
			modal.put("settings", ml);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			ss.close();
		}
		
		return "home";
	}

	private Map<Object, Object> makeKeyValue(Object key, Object value){
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(key, value);
		return map;
	}
	
	@Override
	@ModelAttribute
	void setNavigationType (HttpServletRequest request) {
		request.setAttribute("navigationType", NAVIGATION_TYPE);
		
		//System.out.println(NAVIGATION_TYPE);
	}
}
