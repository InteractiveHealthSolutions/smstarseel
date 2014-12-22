import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
 
public class FileUploadTest {
	final static long  MEGABYTE = 1024L * 1024L;

	public static void main(String[] args) throws IOException, URISyntaxException {
		System.gc();
		System.out.println("GC GC GC");

		StringBuilder sb1111=new StringBuilder();
		sb1111.append("\n*********IN MB*******" +
				  "\nAVAILABLE MEMORY : "+Runtime.getRuntime().freeMemory()/MEGABYTE);
		sb1111.append("\nTOTAL MEMORY   : "+Runtime.getRuntime().totalMemory()/MEGABYTE);
		sb1111.append("\nMAXIMUM MEMORY : "+Runtime.getRuntime().maxMemory()/MEGABYTE);
		
		System.out.println(sb1111.toString());
		
		try {
			Thread.sleep(2000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		uploadFileToServer("e:\\android-sdk-linux20120825.zip","http://localhost:8080/smstarseelweb/testFileUpload.jsp");
		//uploadFileToServer("e:\\testmaster20130624.zip","http://localhost:8080/smstarseelweb/testFileUpload.jsp");
	
		System.gc();
		System.out.println("GC GC GC");

		StringBuilder sb11111=new StringBuilder();
		sb11111.append("\n*********IN MB*******" +
				  "\nAVAILABLE MEMORY : "+Runtime.getRuntime().freeMemory()/MEGABYTE);
		sb11111.append("\nTOTAL MEMORY   : "+Runtime.getRuntime().totalMemory()/MEGABYTE);
		sb11111.append("\nMAXIMUM MEMORY : "+Runtime.getRuntime().maxMemory()/MEGABYTE);
		
		System.out.println(sb11111.toString());
		
		try {
			Thread.sleep(2000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String uploadFileToServer(String pathToFile, String targetUrl) {
	    String response = "error";
	    HttpURLConnection connection = null;
	    DataOutputStream outputStream = null;
 
	    String lineEnd = "\r\n";
	    String twoHyphens = "--";
	    String boundary = "*****";
 
	    int bytesRead, bytesAvailable, bufferSize;
	    byte[] buffer;
	    int maxBufferSize = 1 * 1024;
	    
	    File fl = new File(pathToFile);
	    
	    try {
	        URL url = new URL(targetUrl);
	        connection = (HttpURLConnection) url.openConnection();
 
	        // Allow Inputs & Outputs
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setUseCaches(false);
	        connection.setChunkedStreamingMode(1024);
	        // Enable POST method
	        connection.setRequestMethod("POST");
 
	        connection.setRequestProperty("Connection", "Keep-Alive");
	        connection.setRequestProperty("Content-Type",
	                "multipart/form-data; boundary=" + boundary);
 
	        outputStream = new DataOutputStream(connection.getOutputStream());
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
 
/*	        String taskId = "anyvalue";
	        outputStream.writeBytes("Content-Disposition: form-data; name=\"TaskID\"" + lineEnd);
	        outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
	        outputStream.writeBytes("Content-Length: " + taskId.length() + lineEnd);
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(taskId + lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);*/
 
	        String connstr = null;
	        connstr = "Content-Disposition: form-data; name=\"UploadFile\";filename=\""
	                + fl.getName() + "\"" + lineEnd;
 
	        outputStream.writeBytes(connstr);
	        outputStream.writeBytes(lineEnd);
 
	        FileInputStream fileInputStream = new FileInputStream(fl);

	      	bytesAvailable = fileInputStream.available();
	        bufferSize = Math.min(bytesAvailable, maxBufferSize);
	        buffer = new byte[bufferSize];
 
	        // Read file
	        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	        System.out.println("Image length " + bytesAvailable + "");
	        try {
	            while (bytesRead > 0) {
	                try {
	                    outputStream.write(buffer, 0, bufferSize);
	                } catch (OutOfMemoryError e) {
	                    e.printStackTrace();
	                    response = "outofmemoryerror";
	                    return response;
	                }
	                bytesAvailable = fileInputStream.available();
	                bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            response = "error";
	            return response;
	        }
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
 
	        // Responses from the server (code and message)
	        int serverResponseCode = connection.getResponseCode();
	        String serverResponseMessage = connection.getResponseMessage();
	        System.out.println("Server Response Code " + " " + serverResponseCode);
	        System.out.println("Server Response Message "+ serverResponseMessage);
 
	        if (serverResponseCode == 200) {
	            response = "true";
	        }else
	        {
	        	response = "false";
	        }
 
	        fileInputStream.close();
	        outputStream.flush();
 
	        //for android InputStream is = connection.getInputStream();
	        java.io.InputStream is = connection.getInputStream();
 
			int ch;
			StringBuffer b = new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}
 
			String responseString = b.toString();
			System.out.println("response string is" + responseString); //Here is the actual output
 
	        outputStream.close();
	        outputStream = null;
 
	    } catch (Exception ex) {
	        response = "error";
	        ex.printStackTrace();
	    }
	    return response;
	}
}