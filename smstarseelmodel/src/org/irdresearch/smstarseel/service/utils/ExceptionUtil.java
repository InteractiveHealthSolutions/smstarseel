package org.irdresearch.smstarseel.service.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

    public static String getStackTrace(Exception e)
    {
    	try{
	    	String errorMsg=e.getMessage();
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw, true);
	        e.printStackTrace(pw);
	        pw.flush();
	        sw.flush();
	        return errorMsg+"\n"+sw.toString();
	    	}catch (Exception e1) {
				return e1.toString();
			}
	  }
}
