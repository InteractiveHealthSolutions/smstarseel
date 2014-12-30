package org.irdresearch.smstarseel;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jmatrix.eproperties.EProperties;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.irdresearch.smstarseel.autosys.AutomatedTasks;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.global.RequestParam.App_Service;
import org.irdresearch.smstarseel.global.RequestParam.ResponseCode;
import org.irdresearch.smstarseel.global.RequestParam.ResponseMessage;
import org.irdresearch.smstarseel.global.SmsTarseelGlobal;
import org.irdresearch.smstarseel.global.SmsTarseelResponse;
import org.irdresearch.smstarseel.handler.CallLogHandler;
import org.irdresearch.smstarseel.handler.InitDataHandler;
import org.irdresearch.smstarseel.handler.LoginHandler;
import org.irdresearch.smstarseel.handler.ReceiveFileHandler;
import org.irdresearch.smstarseel.handler.SmsHandler;
import org.irdresearch.smstarseel.web.util.WebGlobals.TarseelSetting;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.StringUtils;

public class SmsTarseel extends HttpServlet {

	private static String TOMCAT7_BUG_CHECKER_CONSTANT = null;
	private static Socket echoSocket = null;
	private static  Thread closeSocketOnShutdown;

	private static Date lastPortErrorEmailDate = new Date(1211111111111L);
	/*Thread t = new Thread(new Runnable() {
	
	@Override
	public void run() {
		try {
            Boolean end = false;
            ServerSocket ss = new ServerSocket(38300);
            while(!end){
                    //Server is waiting for client here, if needed
                    Socket s = ss.accept();
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    PrintWriter output = new PrintWriter(s.getOutputStream(),true); //Autoflush
                    String st = input.readLine();
                    output.println("Good bye and thanks for all the fish :)INPUT:"+st);
                    s.close();
                   
                    //if ( STOPPING conditions){ end = true; }
            }
            
            ss.close();
	    } 
	    catch (UnknownHostException e) {
	            e.printStackTrace();
	    } 
	    catch (IOException e) {
	            e.printStackTrace();
	    }

	}
});

t.start();
return true;*/

	@Override
	public void init() throws ServletException 
	{
		if(TOMCAT7_BUG_CHECKER_CONSTANT == null){
		System.out.println("......TOMCAT7_BUG_CHECKER_CONSTANT WAS NULL......");
		try {
			System.out.println(">>>>LOADING SYSTEM PROPERTIES...");
			InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream("smstarseel.properties");
			// Java Properties donot seem to support substitutions hence EProperties are used to accomplish the task
			EProperties root = new EProperties();
			root.load(f);

			// Java Properties to send to context and other APIs for configuration
			Properties prop = new Properties();
			prop.putAll(SmsTarseelUtil.convertEntrySetToMap(root.entrySet()));

			TarseelContext.instantiate(prop, null);

			System.out.println("......PROPERTIES LOADED SUCCESSFULLY......");
				
			EmailEngine.instantiateEmailEngine(prop, TarseelContext.getSetting(TarseelSetting.ADMIN_EMAIL_ADDRESS.NAME(), "maimoona.kausar@ihsinformatics.org"));
			
			System.out.println("......EMAIL ENGINE LOADED SUCCESSFULLY......");
			
			AutomatedTasks.instantiate();
			System.out.println("......AUTOMATED TASKS LOADED SUCCESSFULLY......");
			
			Timer timer = new Timer();
			timer.schedule(new ServiceCrashAlert(), new Date(), 1000*60*60*2);
			System.out.println("......SERVICE CRASH HANDLERS SCHEDULED SUCCESSFULLY......");
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
		TOMCAT7_BUG_CHECKER_CONSTANT = "ACCESSED";
		}
		// TODO uncomment if doing via socket initSocket();
	}

	public static boolean setPort(String command)
	{
		// run the adb bridge
		try {
			Process p=Runtime.getRuntime().exec(command/*"C:\\android-sdk\\platform-tools\\adb.exe forward tcp:38300 tcp:38300"*/);
		
			Scanner sc = new Scanner(p.getErrorStream());
			if (sc.hasNext()) {
				while (sc.hasNext()) System.out.println(sc.next());
				
				System.out.println("Cannot start the Android debug bridge");
				return false;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("EXCEPTION: "+e.toString());
			return false;
		}
		return true;
	}
	
/*	public static boolean initSocket() {

		System.out.println("EchoClient.main()");

		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				while (true)
				{
					try {
					if(echoSocket == null || echoSocket.isClosed())
					{
						PrintStream out = null;
						BufferedReader in = null;
						
						TarseelServices ts = TarseelContext.getServices();
						Setting set = ts.getSettingService().getSetting("android.command.port-forward");
						String command = set == null ? null : set.getValue();
						boolean portinited = false;
						if(command != null){
							portinited = setPort(command);
						}
						
						if(portinited){
							echoSocket = new Socket("localhost", 38300);
							out = new PrintStream(echoSocket.getOutputStream());
							in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
							
							System.out.println("connected!!");
							
							// add a shutdown hook to close the socket if system crashes or exits unexpectedly
							if(echoSocket != null && closeSocketOnShutdown == null){
								closeSocketOnShutdown = new Thread() {
								public void run() {
									System.out.println(new Date()+" Shutdown ran");
									try {
										echoSocket.shutdownInput();
									} catch (IOException e) {
										e.printStackTrace();
									}
									try {
										echoSocket.shutdownOutput();
									} catch (IOException e) {
										e.printStackTrace();
									}
									try {
										echoSocket.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								};
								
								Runtime.getRuntime().addShutdownHook(closeSocketOnShutdown);
							}
					//}
							//BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
							// String userInput;
		
							while (true) {
									try {
										//out.println("client server: lets start"+new Date());
										//out.println(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG);
										
										String receivedinputmsg = null;
										String actualinputmessage = "";
										boolean readMore = true;
										while (readMore) {
											receivedinputmsg = in.readLine();
											System.out.println("receivedinputmsg: " + receivedinputmsg);
											
											if(receivedinputmsg == null || receivedinputmsg.endsWith(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG)){
												readMore = false;
											}
											
											if(receivedinputmsg != null){
												actualinputmessage += receivedinputmsg.replaceAll(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG, "");
											}
											
											System.out.println("actualinputmessage: " + actualinputmessage);
										}
										if(actualinputmessage != null && !actualinputmessage.equals("")){
											JSONObject jReq = SmsTarseelResponse.convertToJson(actualinputmessage);
										
											out.println(handleRequests(jReq, null));
											out.println(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG);
										}
										else {
											try {
												echoSocket.shutdownInput();
											} catch (IOException e) {
												e.printStackTrace();
											}
											try {
												echoSocket.shutdownOutput();
											} catch (IOException e) {
												e.printStackTrace();
											}
											try {
												echoSocket.close();
											} catch (IOException e) {
												e.printStackTrace();
											}

											break;
										}
										
										System.out.println("write and read successfully");
									} catch (IOException e) {
										//e.printStackTrace();
									} catch (JSONException e) {
										e.printStackTrace();
									} catch (Exception e) {
										e.printStackTrace();
									}
									finally{
										ts.closeSession();
									}
								}
							
							}
							else{
								if(DateUtils.differenceBetweenIntervals(new Date(), lastPortErrorEmailDate, TIME_INTERVAL.HOUR) > 1){
									EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error port lost", "SmsTarseel: Error port lost. Port not able to be instantiated while trying adb");
									lastPortErrorEmailDate = new Date();
								}
								
								try {
									Thread.sleep(1000*60*1);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
						} 
						catch (UnknownHostException e) 
						{
							System.err.println("Don't know about host: localhost.");
							//System.exit(1);
						} 
						catch (IOException e) 
						{
							System.err.println("Couldn't get I/O for " + "the connection to: localhost.");
							//System.exit(1);
						}
						finally{
							
						}
				}
			}
		});
		
		t.start();
		return true;
	}*/

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException
	{
		doPost(req, resp);
	}

	private static void handleRequests(JSONObject jReq, HttpServletResponse response, FileItem file) throws IOException, JSONException
	{
		SmsTarseelUtil.PHONECOMMLOGGER.info("request:"+jReq.toString());
		String service = jReq.getString(App_Service.NAME);
			
		if (StringUtils.isEmptyOrWhitespaceOnly(service))
		{
			SmsTarseelUtil.sendResponse(response, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.INAVLID_REQUEST, "").jsonToString());
		}
		else if (service.equalsIgnoreCase(App_Service.LOGIN.VALUE()))
		{
			LoginHandler.handleDeviceLogin(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.REGISTER_DEVICE_PROJECT.VALUE()))
		{
			LoginHandler.registerDevice(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.QUERY_PROJECTLIST.VALUE()))
		{
			InitDataHandler.getProjectList(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.SEND_LOG.VALUE()))
		{
			ReceiveFileHandler.receiveLogFileData(jReq, response, file);
		}
		else if (service.equalsIgnoreCase(App_Service.FETCH_PENDING_SMS.VALUE()))
		{
			SmsHandler.getPendingSmsTillNow(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.SUBMIT_SMS_SEND_ATTEMPT_RESULT.VALUE()))
		{
			SmsHandler.submitSmsSendAttemptResult(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.SUBMIT_RECIEVED_SMS.VALUE()))
		{
			SmsHandler.submitRecivedSms(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.SUBMIT_CALL_LOG.VALUE()))
		{
			CallLogHandler.submitCallLog(jReq, response);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException
	{
		/*String init = req.getParameter("sockinit");
		if(init!=null && init.equalsIgnoreCase("doit")){
			initSocket();
			return;
		}
		else if(init!=null && init.equalsIgnoreCase("portfwd")){
			setPort();
			return;
		}*/
		
		try
		{
			String stringJson = "";
			FileItem file = null;
			boolean isMultipart = ServletFileUpload.isMultipartContent( req );
			boolean isJsonRequest = true;
			
			if (isMultipart) {
				// Create a factory for disk-based file items
				FileItemFactory factory = new DiskFileItemFactory();

				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);

				// Parse the request
				List /* FileItem */ items = upload.parseRequest(req);
				
				Iterator iter = items.iterator();
				while ( iter.hasNext() ) {
					FileItem item = (FileItem) iter.next();

					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase( SmsTarseelGlobal.JSON_REQUEST_DATA_PARAM_NAME )) {
							stringJson = item.getString();
						}
					}
					else {
						isJsonRequest = true;
						stringJson = item.getFieldName();
						file = item;
					}
				}
			}
			else{
				byte[] b = new byte[req.getContentLength()];
				req.getInputStream().read(b);
				stringJson = new String(b);
			}
			
			//JSONObject jReq = SmsTarseelResponse.convertToJson(req.getInputStream(), req.getContentLength());
			JSONObject jReq = null ;
			if(isJsonRequest){
				jReq = SmsTarseelResponse.convertToJson(stringJson);

				try{
					handleRequests(jReq, resp, file);
				}
				catch (Exception e) {
					e.printStackTrace();
					SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,e.getMessage()).jsonToString());
				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
