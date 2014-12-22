package org.irdresearch.smstarseel.web.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import au.com.bytecode.opencsv.CSVReader;

@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(value = {"/smsscheduleer.htm"})
public class SmsSchedulerController extends DataManipulateController{

	@Override
	@RequestMapping(value = "/smsscheduleer.htm", method = RequestMethod.GET)
	public ModelAndView showForm (Map modal) {
		return new ModelAndView("smsscheduler");
	}
	
	
	@RequestMapping(value = "/smsscheduleer.htm", method = RequestMethod.POST)
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
					printWriter.print(processString(file));
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
		
		
		// return "redirect:/smsscheduler.htm";
		// return  "smsscheduler";
	}
	
	private String processString(String file) throws IOException, InstanceAlreadyExistsException, NumberFormatException, ParseException {
		
		CSVReader csvReader = new CSVReader(new StringReader(file));
		String[] row;
		String referenceNumber;
		// TarseelContext.instantiate(null, "smstarseel.cfg.xml");
		TarseelServices tarseelServices = TarseelContext.getServices();
		String resp = "";
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		while ((row = csvReader.readNext())!=null) {
				referenceNumber = tarseelServices.getSmsService()
						.createNewOutboundSms(row[4],                 // Recipient
								row[5],                               // Message text
								format.parse(row[1]),                           // Due date
								Priority.valueOf(row[6]),             // Priority
								Integer.parseInt(row[2]),             // Validity duration
								PeriodType.valueOf(row[3]),           // Validity duration type
								Integer.parseInt(row[0]),             // Project id
								row[7]);                              // Additional description
				
				resp+= row[0]+","+row[1]+","+row[2]+","+row[3]+","+row[4]+","+row[5]+","+referenceNumber+"\n";
				
			
		}
		tarseelServices.commitTransaction();
		tarseelServices.closeSession();
		csvReader.close();
		
		return resp;
	}

	
	
	@Override
	String submitForm(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
}
