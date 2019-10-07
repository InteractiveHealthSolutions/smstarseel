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

	public static void main(String[] args) throws MalformedURLException {
		System.out.println(new URL("http://localhost:8181/unfepi/ws/api/reminder/failed?id={{id}}"));
	}
	
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
				new URI(s.getDeliveryReportUrl());
			}catch(IllegalArgumentException | URISyntaxException e){
				errors.rejectValue("deliveryReportUrl", null, "invalid url pattern");				
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(s.getInboundReportUrl())){
			try{
				new URL(s.getInboundReportUrl());
			}catch(IllegalArgumentException | MalformedURLException e){
				errors.rejectValue("inboundReportUrl", null, "invalid url pattern");				
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(s.getOutboundFailureReportUrl())){
			try{
				new URL(s.getOutboundFailureReportUrl());
			}catch(IllegalArgumentException | MalformedURLException e){
				errors.rejectValue("outboundFailureReportUrl", null, "invalid url pattern");				
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(s.getOutboundSuccessReportUrl())){
			try{
				new URL(s.getOutboundSuccessReportUrl());
			}catch(IllegalArgumentException | MalformedURLException e){
				errors.rejectValue("outboundSuccessReportUrl", null, "invalid url pattern");				
			}
		}
	}

}
