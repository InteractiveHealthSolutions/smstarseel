package org.irdresearch.smstarseel.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public abstract class DataDisplayController {

	abstract String getData(Map modal);
	
	abstract void setNavigationType(HttpServletRequest request);
}
