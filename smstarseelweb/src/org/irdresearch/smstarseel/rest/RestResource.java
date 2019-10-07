package org.irdresearch.smstarseel.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.irdresearch.smstarseel.rest.util.Utils;
import org.irdresearch.smstarseel.service.utils.ExceptionUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public abstract class RestResource <T>{
	@RequestMapping(method=RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	protected Map<String, Object> createNew(@RequestBody T entity) {
		try {
			Utils.verifyRequiredProperties(requiredProperties(), entity);
			return create(entity);
		} catch (Exception e) {
			e.printStackTrace();
			HashMap<String, Object> m = new HashMap<>();
			m.put("ERROR", e.getMessage());
			m.put("TRACE", ExceptionUtil.getStackTrace(e));
			return m;
		}
	}
	
	@RequestMapping(value="/{referenceNumber}", method=RequestMethod.GET)
	@ResponseBody
	protected T getByReferenceId(@PathVariable("referenceNumber") String referenceNumber){
		return getByReferenceNumber(referenceNumber);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	protected List<T> searchBy(@RequestParam(required=true,value="projectId") Integer projectId, HttpServletRequest request){
		return search(projectId, request);
	}
	
	public abstract List<T> search(Integer projectId, HttpServletRequest request);
	
	public abstract T getByReferenceNumber(String referenceNumber);
	
	public abstract List<String> requiredProperties();

	public abstract Map<String, Object> create(T entity) ;
}
