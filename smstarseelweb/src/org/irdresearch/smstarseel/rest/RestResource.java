package org.irdresearch.smstarseel.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.irdresearch.smstarseel.service.utils.ExceptionUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	public abstract List<String> requiredProperties();
	
	public abstract Map<String, Object> create(T entity) ;
}
