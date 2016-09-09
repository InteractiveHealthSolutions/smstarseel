package org.irdresearch.smstarseel.web.validator;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.irdresearch.smstarseel.data.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class ServiceValidator implements Validator{

	@Override
	public boolean supports(Class<?> cls) {
		return cls.equals(Service.class);
	}

	@Override
	public void validate(Object cmd, Errors errors) {
		Service s = (Service) cmd;
		if(StringUtils.isEmptyOrWhitespaceOnly(s.getServiceName())){
			errors.rejectValue("serviceName", null, "required");
		}
		if(StringUtils.isEmptyOrWhitespaceOnly(s.getServiceIdentifier())
				&& !s.getServiceIdentifier().matches("^[-a-zA-Z0-9_]")){
			errors.rejectValue("serviceIdentifier", null, "required (alphanumeric; only hyphen and underscore allowed)");
		}
		if(s.getProject() == null || s.getProject().getProjectId() == null){
			errors.rejectValue("project.projectId", null, "required");
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(s.getDeliveryReportUrl())){
			try{
				URI.create(s.getDeliveryReportUrl());
			}catch(IllegalArgumentException e){
				errors.rejectValue("deliveryReportUrl", null, "invalid url pattern");				
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(s.getInboundReportUrl())){
			try{
				URI.create(s.getInboundReportUrl());
			}catch(IllegalArgumentException e){
				errors.rejectValue("inboundReportUrl", null, "invalid url pattern");				
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(s.getOutboundFailureReportUrl())){
			try{
				URI.create(s.getOutboundFailureReportUrl());
			}catch(IllegalArgumentException e){
				errors.rejectValue("outboundFailureReportUrl", null, "invalid url pattern");				
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(s.getOutboundSuccessReportUrl())){
			try{
				URI.create(s.getOutboundSuccessReportUrl());
			}catch(IllegalArgumentException e){
				errors.rejectValue("outboundSuccessReportUrl", null, "invalid url pattern");				
			}
		}
	}

}
