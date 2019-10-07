package org.irdresearch.smstarseel.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.web.util.ResponseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/outbound")
public class OutBoundMessageResource extends RestResource<OutboundMessage>{

	@RequestMapping(value="/{referenceNumber}", method=RequestMethod.GET)
	public @ResponseBody OutboundMessage getOutBoundByReferenceNumber(@PathVariable("referenceNumber") String referenceNumber) {
		TarseelServices tsc = TarseelContext.getServices();
		try {
			return ResponseUtil.prepareDataResponse(tsc.getSmsService().findOutboundMessageByReferenceNumber(referenceNumber, true), null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally{
			tsc.closeSession();
		}
	}
	
	@Override
    public Map<String, Object> create(OutboundMessage o) {
        Map<String, Object> m = new HashMap<>();
		TarseelServices tsc = TarseelContext.getServices();
		try {
			m.put("referenceNumber", tsc.getSmsService().createNewOutboundSms(o.getRecipient(), 
					o.getText(), o.getDueDate(), o.getPriority(), o.getValidityPeriod(), o.getPeriodType(), o.getProjectId(), o.getDescription()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally{
			tsc.closeSession();
		}
		return m;
	}

	@Override
	public List<String> requiredProperties() {
		List<String> p = new ArrayList<>();
		p.add("text");
		p.add("recipient");
		p.add("dueDate");
		p.add("priority");
		p.add("validityPeriod");
		p.add("periodType");
		p.add("projectId");
		return p;
	}
}
