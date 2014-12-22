package org.irdresearch.smstarseel.comm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.irdresearch.smstarseel.constant.TarseelGlobals;
import org.irdresearch.smstarseel.constant.TarseelGlobals.COMM_MODE;
import org.irdresearch.smstarseel.global.RequestParam.LogFileParams;
import org.irdresearch.smstarseel.global.SmsTarseelGlobal;
import org.irdresearch.smstarseel.global.SmsTarseelResponse;
import org.irdresearch.smstarseel.util.FileUtil;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class HttpSender
{
	//private static HttpClient hc;
	//private static HttpPost post = new HttpPost(TarseelGlobals.SERVER_URL);
	private static String charset = "UTF-8";
	
	private static JSONObject sendViaHttp(Context context, SmsTarseelRequest request) throws IOException{
		HttpURLConnection con = null;
		URL url;
		try {
			String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
	        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

			url = new URL(TarseelGlobals.SERVER_URL(context));
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(1000*60*10 /* milliseconds */);
			con.setConnectTimeout(1000*30 /* milliseconds */);
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("Accept-Charset", charset);

            //con.setFixedLengthStreamingMode(request.toString().length());
			//con.addRequestProperty("Referer", "http://blog.dahanne.net");
			// Start the query
			con.connect();
			//OutputStream output = con.getOutputStream();
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), charset), true); // true = autoFlush, important!

			// Send normal param.
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\""+SmsTarseelGlobal.JSON_REQUEST_DATA_PARAM_NAME+"\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset ).append(CRLF);
		    writer.append(CRLF);
		    writer.append(request.toString()).append(CRLF).flush();
		    // End of multipart/form-data.
		    writer.append("--" + boundary + "--").append(CRLF);
		    if (writer != null) writer.close();

		    //if(output != null) output.close(); 
		    	
			JSONObject json = new JSONObject();
			if (con.getResponseCode() == HttpStatus.SC_OK)
			{
				InputStream ins = con.getInputStream();
				json = SmsTarseelResponse.convertToJson(ins);
				try{
					ins.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			con.disconnect();
			
			return json;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			TarseelGlobals.addTo_CONSOLE_BUFFER("HttpSender", "JSONException:"+e.getMessage());
	        FileUtil.writeLog("HttpSender", "JSONException:");
	        FileUtil.writeLog(e);
		}
		finally{
			
		}
		return null;
	}
	
/*	private static JSONObject sendViaSocket(Context context, SmsTarseelRequest request) throws IOException{
		Socket client = SocketService.getSocketConnection();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter outpw = new PrintWriter(client.getOutputStream(), true);
			
			outpw.println(request.toString());
			outpw.println(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG);
			outpw.flush();
			
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
				return SmsTarseelResponse.convertToJson(actualinputmessage);
			}
		} 
		catch (Exception e) {
			Log.d("HTTP_SENDER", "lost client");
			e.printStackTrace();
		}
		return null;
	}
	*/

	public static synchronized JSONObject sendLargeText(Context context, SmsTarseelRequest request) throws IOException 
	{
		String commType = TarseelGlobals.getPreference(context, TarseelGlobals.COMM_TYPE_PREF_NAME, null);
		if(commType.equalsIgnoreCase(COMM_MODE.WIFI.name())){
			return sendViaHttp(context, request);
		}
		/*else if(commType.equalsIgnoreCase(COMM_MODE.USB.name())){
			return sendViaSocket(context, request);
		}*/
		return null;
 
	}
	public static synchronized JSONObject sendFile(Context context, SmsTarseelRequest request) throws IOException {
		//TODO TarseelGlobals.resetWiFiSleepPolicy();
		
		HttpURLConnection con = null;
		URL url;
		try {
			String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
	        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

			url = new URL(TarseelGlobals.SERVER_URL(context));
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(1000*60*10 /* milliseconds */);
			con.setConnectTimeout(1000*30 /* milliseconds */);
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("Accept-Charset", charset);
            con.setChunkedStreamingMode(1024);

            //con.setFixedLengthStreamingMode(request.toString().length());
			//con.addRequestProperty("Referer", "http://blog.dahanne.net");
			// Start the query
			con.connect();
			//OutputStream output = con.getOutputStream();
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), charset), true); // true = autoFlush, important!

			// Send text file.
			/*String params = "";
			Iterator names = request.getRequestParams().keys();
			while (names.hasNext()) {
				Object obj = names.next();
				params += obj + "=" + "\""+request.getRequestParams().get(obj.toString()) + "\";";
			}*/
			
			//System.out.println(params);
			
			File file = new File(request.getRequestParams().getString(LogFileParams.FILE_NAME.KEY()));
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\""+request.toString()+"\"; filename=\"" + file.getName() + "\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
		    writer.append(CRLF).flush();
		    BufferedReader reader = null;
		    try {
		        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
		        for (String line; (line = reader.readLine()) != null;) {
		            writer.append(line).append(CRLF);
		        }
		    } finally {
		        if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {logOrIgnore.printStackTrace();}
		    }
		    writer.flush();

		    // End of multipart/form-data.
		    writer.append("--" + boundary + "--").append(CRLF);
		    if (writer != null) writer.close();

		    //if(output != null) output.close(); 

			JSONObject json = new JSONObject();
			if (con.getResponseCode() == HttpStatus.SC_OK)
			{
				InputStream ins = con.getInputStream();
				json = SmsTarseelResponse.convertToJson(ins);
				try{
					ins.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			con.disconnect();
			
			return json;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			TarseelGlobals.addTo_CONSOLE_BUFFER("HttpSender", "JSONException:"+e.getMessage());
	        FileUtil.writeLog("HttpSender", "JSONException:");
	        FileUtil.writeLog(e);
		}
		
		return null;
	}
	/*public static synchronized JSONObject postRequest (SmsTarseelRequest request) throws ClientProtocolException, IOException
	{
		TarseelGlobals.GLOBALS(context).resetWiFiSleepPolicy();
		
		if(hc == null){
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			// The default value is zero, that means the timeout is not used. 
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 30000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
			hc = new DefaultHttpClient(httpParameters);
		}
		
		try
		{
			// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(request.getRequestParams(),HTTP.UTF_8);
			StringEntity se = new StringEntity(request.toString(), charset);

			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

			post.setEntity(se);

			HttpResponse rp = hc.execute(post);

			JSONObject json = new JSONObject();
			if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				json = SmsTarseelResponse.convertToJson(rp.getEntity().getContent(), (int) rp.getEntity().getContentLength());
			}
			hc.getConnectionManager().closeExpiredConnections();
			return json;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}*/
}
