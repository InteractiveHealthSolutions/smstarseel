package org.irdresearch.smstarseel.web.util;

import javax.servlet.http.HttpServletRequest;

public class ExceptionHandlerUtil {

	public static String showException (HttpServletRequest request, Exception e) {
		request.setAttribute("exceptionTrace", "Exception message was \n"+e.getMessage()+"\n\nCheck logs for details and contact your system administrator.");
		return "exception";
	}
}
