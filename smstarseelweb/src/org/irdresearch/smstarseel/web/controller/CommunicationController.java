package org.irdresearch.smstarseel.web.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.CallLog;
import org.irdresearch.smstarseel.data.CallLog.CallStatus;
import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.InboundMessage;
import org.irdresearch.smstarseel.data.InboundMessage.InboundStatus;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;
import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.web.util.ResponseUtil;
import org.irdresearch.smstarseel.web.util.WebGlobals;
import org.irdresearch.smstarseel.web.util.WebGlobals.CallQueryParams;
import org.irdresearch.smstarseel.web.util.WebGlobals.CommunicationQueryParams;
import org.irdresearch.smstarseel.web.util.WebGlobals.InboundQueryParams;
import org.irdresearch.smstarseel.web.util.WebGlobals.OutboundQueryParams;
import org.irdresearch.smstarseel.web.util.WebGlobals.QueryParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import au.com.bytecode.opencsv.CSVReader;

import com.mysql.jdbc.StringUtils;

@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(value = "/communication/")
public class CommunicationController  extends DataDisplayController {

	private static String NAVIGATION_TYPE = "communication";

	@Override
	@RequestMapping(value="communication.htm", method = RequestMethod.GET)
	public String getData (Map modal) {
		return "communication";
	}

	@RequestMapping(value="viewOutbounds.htm", method = RequestMethod.GET)
	public String getOutboundData (Map modal) {
		return "viewOutbounds";
	}
	
	@RequestMapping(value="viewInbounds.htm", method = RequestMethod.GET)
	public String getInboundData (Map modal) {
		return "viewInbounds";
	}
	
	@RequestMapping(value="viewCalls.htm", method = RequestMethod.GET)
	public String getCallData (Map modal) {
		return "viewCalls";
	}
	
	@RequestMapping(value="sendSms.htm", method = RequestMethod.GET)
	public String sendSms (Map modal) {
		TarseelServices tsc = TarseelContext.getServices();
		List<Project> prjsl = tsc.getDeviceService().getAllProjects(0, Integer.MAX_VALUE);
		modal.put("projects", prjsl);
		return "sendSmsform";
	}
	
	@RequestMapping(value="smsscheduler.htm", method = RequestMethod.GET)
	public String uploadCsv (Map modal) {
		return "smsscheduler";
	}
	

	@RequestMapping(value = "/smsscheduler.htm", method = RequestMethod.POST)
	public void submitFormm (HttpServletRequest request, HttpServletResponse response) {
		
		try {
			String contentType = request.getContentType();
			request.setAttribute("errorMessage", "null");
			if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
				
				DataInputStream in;
				
				in = new DataInputStream(request.getInputStream());
				
				int formDataLength = request.getContentLength();
				byte dataBytes[] = new byte[formDataLength];
				int byteRead = 0;
				int totalBytesRead = 0;
		
				while (totalBytesRead < formDataLength) {
					byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
					totalBytesRead += byteRead;
				}
				String file = new String(dataBytes);
		
				int pos = 0;		
				pos = file.indexOf("\n", pos) + 1;
				pos = file.indexOf("\n", pos) + 1;
				pos = file.indexOf("\n", pos) + 1;
				pos = file.indexOf("\n", pos) + 1;
				int startPos = pos;
				pos = file.indexOf("----", pos)-1;
				file = file.substring(startPos, pos);
				System.out.println(startPos+file+pos);
				
				response.setContentType("text/csv");
		        response.setHeader("Content-Disposition", "attachment; filename=\"data.csv\"");
		        try {
					PrintWriter printWriter = response.getWriter();
					String jsonString = processCsvString(file);
					printWriter.print(jsonString);
			        printWriter.flush();
			        printWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			request.setAttribute("message", e.getMessage());
			e.printStackTrace();
		}
	}
	
	private String processCsvString(String file) throws IOException, InstanceAlreadyExistsException {
		String resp = "";
		JSONArray jsonArray = new JSONArray();
		JSONObject responseJson = new JSONObject();

		CSVReader csvReader = new CSVReader(new StringReader(file));
		String[] row;
		
		// TarseelContext.instantiate(null, "smstarseel.cfg.xml");
		TarseelServices tarseelServices = TarseelContext.getServices();
		try {
			while ((row = csvReader.readNext()) != null) {
				if(row.length ==8) {
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					
					String description = row[7];
					String message = row[5];
					String recipient = row[4];
					Date dueDate;
		
					int validityDuration = Integer.parseInt(row[2]);
					PeriodType periodType = PeriodType.valueOf(row[3]);
					Priority priority = Priority.valueOf(row[6]);
					dueDate = dateFormat.parse(row[1]);
					int projectId = Integer.parseInt(row[0]);
					
					if (description.length() > 255) {
						throw new Exception("Description text too large for \nMessage: '" + message + "' \nRecipient: '" + recipient + "'");
					}
					
					String referenceNumber = tarseelServices.getSmsService().createNewOutboundSms(recipient, message, dueDate, priority, validityDuration, periodType, projectId, description); 
					
					JSONObject data = new JSONObject();
					data.put("projectid", projectId);
					data.put("duedate", dateFormat.format(dueDate));
					data.put("validityduration", validityDuration);
					data.put("durationtype", periodType.toString());
					data.put("recipient", recipient);
					data.put("message", message);
					data.put("priority", priority.toString());
					data.put("description", description);
					data.put("referencenumber", referenceNumber);
					jsonArray.put(data);
				}
			}
			
			tarseelServices.commitTransaction();
			tarseelServices.closeSession();
			
			responseJson.put("total", jsonArray.length());
			responseJson.put("rows", jsonArray);
			resp = responseJson.toString();
			
		} catch (JSONException e) {
			e.printStackTrace();
			resp = "Invalid JSON";
			
		}  catch (ParseException e) {
			e.printStackTrace();
			resp = "Invalid due date format \nshould be like '30/01/2014 12:00:00'";
			
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			resp = "Invalid csv format\nplease contact SMS-Tarseel admin for correct format";
		} catch (Exception e) {
			e.printStackTrace();
			resp = e.getMessage();
			
		} finally {
			if(csvReader!=null) {
				csvReader.close();
			}
		}

		return resp;
	}

	@RequestMapping(value="/traverse_outbounds.do")
	public @ResponseBody Map<String, Object> traverseOutbounds(HttpServletRequest request){
		Map queryParams = request.getParameterMap();
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<OutboundMessage> items = new ArrayList<OutboundMessage>();
		
		TarseelServices tsc = TarseelContext.getServices();
		try {
			String rfnum = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.REFERENCE_NUMBER, queryParams, false);
			if(!StringUtils.isEmptyOrWhitespaceOnly(rfnum)){
				OutboundMessage obj = tsc.getSmsService().findOutboundMessageByReferenceNumber(rfnum, true);
				if(obj != null) items.add(obj);
			}
			else{
				String st = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.STATUS, queryParams, false);
				OutboundStatus obst = !StringUtils.isEmptyOrWhitespaceOnly(st)? OutboundStatus.valueOf(st):null;
				String recipient = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.REFERRED_NUMBER, queryParams, false);
				String imei = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.IMEI, queryParams, false);
				
				String duedateF = SmsTarseelUtil.getSingleParamFromRequestMap(OutboundQueryParams.DUEDATE_FROM, queryParams, false);
				String duedateT = SmsTarseelUtil.getSingleParamFromRequestMap(OutboundQueryParams.DUEDATE_TO, queryParams, false);
				String sentdateF = SmsTarseelUtil.getSingleParamFromRequestMap(OutboundQueryParams.SENTDATE_FROM, queryParams, false);
				String sentdateT = SmsTarseelUtil.getSingleParamFromRequestMap(OutboundQueryParams.SENTDATE_TO, queryParams, false);
				
				Integer pageSize = Integer.parseInt(SmsTarseelUtil.getSingleParamFromRequestMap(QueryParams.PAGE_SIZE, queryParams, false));
				Integer pageNumber = Integer.parseInt(SmsTarseelUtil.getSingleParamFromRequestMap(QueryParams.PAGE_NUMBER, queryParams, false));

				Date duedateFrom = StringUtils.isEmptyOrWhitespaceOnly(duedateF)?null:WebGlobals.GLOBAL_SDF_DATE.parse(duedateF);
				Date duedateTo = StringUtils.isEmptyOrWhitespaceOnly(duedateT)?null:WebGlobals.GLOBAL_SDF_DATE.parse(duedateT);
				Date sentdateFrom = StringUtils.isEmptyOrWhitespaceOnly(sentdateF)?null:WebGlobals.GLOBAL_SDF_DATE.parse(sentdateF);
				Date sentdateTo = StringUtils.isEmptyOrWhitespaceOnly(sentdateT)?null:WebGlobals.GLOBAL_SDF_DATE.parse(sentdateT);
				
				items = tsc.getSmsService().findOutbound(duedateFrom, duedateTo, sentdateFrom, sentdateTo, obst, recipient, null, imei, null, false, false, (pageNumber-1)*pageSize, pageSize);
			}
			
			map.put("rows", ResponseUtil.prepareDataResponse((ArrayList<OutboundMessage>) items, null));
		    map.put("total", tsc.getSmsService().LAST_QUERY_TOTAL_ROW__COUNT(OutboundMessage.class).intValue());
		}
		catch (DataException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e1) {
			e1.printStackTrace();
		}
		catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		finally{
			tsc.closeSession();
		}
	    
		return map;
	}
	
	@RequestMapping(value="/traverse_inbounds.do")
	public @ResponseBody Map<String, Object> traverseInbounds(HttpServletRequest request){
		Map queryParams = request.getParameterMap();
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<InboundMessage> items = new ArrayList<InboundMessage>();
		
		TarseelServices tsc = TarseelContext.getServices();
		try {
			String rfnum = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.REFERENCE_NUMBER, queryParams, false);
			if(!StringUtils.isEmptyOrWhitespaceOnly(rfnum)){
				InboundMessage ibj = tsc.getSmsService().findInboundMessageByReferenceNumber(rfnum, true);
				if(ibj != null) items.add(ibj);
			}
			else{
				String st = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.STATUS, queryParams, false);
				InboundStatus ibst = !StringUtils.isEmptyOrWhitespaceOnly(st)? InboundStatus.valueOf(st):null;
				String originator = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.REFERRED_NUMBER, queryParams, false);
				String imei = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.IMEI, queryParams, false);
				
				String receivedateF = SmsTarseelUtil.getSingleParamFromRequestMap(InboundQueryParams.RECEIVEDATE_FROM, queryParams, false);
				String receivedateT = SmsTarseelUtil.getSingleParamFromRequestMap(InboundQueryParams.RECEIVEDATE_TO, queryParams, false);
				
				Integer pageSize = Integer.parseInt(SmsTarseelUtil.getSingleParamFromRequestMap(QueryParams.PAGE_SIZE, queryParams, false));
				Integer pageNumber = Integer.parseInt(SmsTarseelUtil.getSingleParamFromRequestMap(QueryParams.PAGE_NUMBER, queryParams, false));

				Date receivedateFrom = StringUtils.isEmptyOrWhitespaceOnly(receivedateF)?null:WebGlobals.GLOBAL_SDF_DATE.parse(receivedateF);
				Date receivedateTo = StringUtils.isEmptyOrWhitespaceOnly(receivedateT)?null:WebGlobals.GLOBAL_SDF_DATE.parse(receivedateT);
				
				items = tsc.getSmsService().findInbound(receivedateFrom, receivedateTo, ibst, null, originator, imei, null, false, (pageNumber-1)*pageSize, pageSize);
			}
			
			map.put("rows", ResponseUtil.prepareDataResponse((ArrayList<InboundMessage>) items, null));
			map.put("total", tsc.getSmsService().LAST_QUERY_TOTAL_ROW__COUNT(InboundMessage.class).intValue());
		}
		catch (DataException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e1) {
			e1.printStackTrace();
		}
		catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		finally{
			tsc.closeSession();
		}
	    
		return map;
	}
	
	@RequestMapping(value="/traverse_calls.do")
	public @ResponseBody Map<String, Object> traverseCalls(HttpServletRequest request){
		Map queryParams = request.getParameterMap();
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<CallLog> items = new ArrayList<CallLog>();
		
		TarseelServices tsc = TarseelContext.getServices();
		try {
			String rfnum = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.REFERENCE_NUMBER, queryParams, false);
			if(!StringUtils.isEmptyOrWhitespaceOnly(rfnum)){
				CallLog cbj = tsc.getCallService().findByReferenceNumber(rfnum, true);
				if(cbj != null) items.add(cbj);
			}
			else{
				String st = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.STATUS, queryParams, false);
				CallStatus clst = !StringUtils.isEmptyOrWhitespaceOnly(st)? CallStatus.valueOf(st):null;
				String caller = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.REFERRED_NUMBER, queryParams, false);
				String imei = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.IMEI, queryParams, false);
				
				String calldateF = SmsTarseelUtil.getSingleParamFromRequestMap(CallQueryParams.CALLDATE_FROM, queryParams, false);
				String calldateT = SmsTarseelUtil.getSingleParamFromRequestMap(CallQueryParams.CALLDATE_TO, queryParams, false);
				
				Integer pageSize = Integer.parseInt(SmsTarseelUtil.getSingleParamFromRequestMap(QueryParams.PAGE_SIZE, queryParams, false));
				Integer pageNumber = Integer.parseInt(SmsTarseelUtil.getSingleParamFromRequestMap(QueryParams.PAGE_NUMBER, queryParams, false));

				Date calldateFrom = StringUtils.isEmptyOrWhitespaceOnly(calldateF)?null:WebGlobals.GLOBAL_SDF_DATE.parse(calldateF);
				Date calldateTo = StringUtils.isEmptyOrWhitespaceOnly(calldateT)?null:WebGlobals.GLOBAL_SDF_DATE.parse(calldateT);
				
				items = tsc.getCallService().findCall(calldateFrom, calldateTo, null, null, caller, false, clst, imei, null, (pageNumber-1)*pageSize, pageSize);
			}
			
			map.put("rows", ResponseUtil.prepareDataResponse((ArrayList<CallLog>) items, null));
		    map.put("total", tsc.getCallService().LAST_QUERY_TOTAL_ROW__COUNT(CallLog.class).intValue());
		}
		catch (DataException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e1) {
			e1.printStackTrace();
		}
		catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		finally{
			tsc.closeSession();
		}
	    
		return map;
	}
	
	@RequestMapping(value="/queue_sms.dm")
	public @ResponseBody Map<String, Object> queueSmses(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		TarseelServices tsc = TarseelContext.getServices();

		try {
			Map queryParams = request.getParameterMap();
			
			List<CallLog> items = new ArrayList<CallLog>();
			
			String textToSend = SmsTarseelUtil.getSingleParamFromRequestMap(OutboundQueryParams.TEXT, queryParams, false);
			String[] recipients = SmsTarseelUtil.getArrayParamFromRequestMap(CommunicationQueryParams.REFERRED_NUMBER, queryParams);
			String project = SmsTarseelUtil.getSingleParamFromRequestMap(CommunicationQueryParams.PROJECT, queryParams, false);
			int projId = tsc.getDeviceService().findProject(project).get(0).getProjectId();
			String addNote = SmsTarseelUtil.getSingleParamFromRequestMap(OutboundQueryParams.ADD_NOTE, queryParams, false);
			
			Integer validityPeriod = Integer.parseInt(SmsTarseelUtil.getSingleParamFromRequestMap(OutboundQueryParams.VALIDITY, queryParams, false));
			String periodType = SmsTarseelUtil.getSingleParamFromRequestMap(OutboundQueryParams.VALIDITY_TYPE, queryParams, false);
			PeriodType validityPType = !StringUtils.isEmptyOrWhitespaceOnly(periodType)? PeriodType.valueOf(periodType):null;
	
			for (String recp : recipients) {
				if(!StringUtils.isEmptyOrWhitespaceOnly(recp)){
					tsc.getSmsService().createNewOutboundSms(recp, textToSend, new Date(), Priority.MEDIUM, validityPeriod, validityPType, projId, addNote);
				}
			}
			
			tsc.commitTransaction();
			map.put("message", "Smses queued successfully");
		}
		catch (Exception e1) {
			e1.printStackTrace();
			map.put("message", "Error:"+e1.getMessage());
		}
		finally{
			tsc.closeSession();
		}
	    
		return map;
	}
	
	@Override
	@ModelAttribute
	void setNavigationType (HttpServletRequest request) {
		/*System.out.println("----------------------------------------------------------");
		Enumeration rhs = request.getHeaderNames();
		while (rhs.hasMoreElements()) {
			Object object = (Object) rhs.nextElement();
			System.out.println(object+": "+request.getHeader((String) object));
		}
		System.out.println("getQueryString():"+request.getQueryString());*/
		
		request.setAttribute("navigationType", NAVIGATION_TYPE);
		
		//System.out.println(NAVIGATION_TYPE);
	}
}
