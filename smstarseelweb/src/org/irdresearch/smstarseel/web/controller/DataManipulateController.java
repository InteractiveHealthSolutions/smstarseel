package org.irdresearch.smstarseel.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public abstract class DataManipulateController {

	abstract ModelAndView showForm(Map modal);
	
	abstract String submitForm(HttpServletRequest request, HttpServletResponse response);
}
