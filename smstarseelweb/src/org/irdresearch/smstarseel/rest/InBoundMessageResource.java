package org.irdresearch.smstarseel.rest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.InboundMessage;
import org.irdresearch.smstarseel.data.InboundMessage.InboundStatus;
import org.irdresearch.smstarseel.rest.util.Utils;
import org.irdresearch.smstarseel.web.util.ResponseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/inbound")
public class InBoundMessageResource extends RestResource<InboundMessage>{

	public InboundMessage getByReferenceNumber(String referenceNumber) {
		TarseelServices tsc = TarseelContext.getServices();
		try {
			return ResponseUtil.prepareDataResponse(tsc.getSmsService().findInboundMessageByReferenceNumber(referenceNumber, true), null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally{
			tsc.closeSession();
		}
	}
	
	@Override
    public Map<String, Object> create(InboundMessage i) {
        throw new UnsupportedOperationException("Can not create an inbound message remotely");
	}

	@Override
	public List<String> requiredProperties() {
		return null;
	}

	@Override
	public List<InboundMessage> search(Integer projectId, HttpServletRequest request) {
		TarseelServices tsc = TarseelContext.getServices();
		try {
			InboundStatus status = (InboundStatus) Utils.getEnumFilter("status", InboundStatus.class, request);
			return ResponseUtil.prepareDataResponse(tsc.getSmsService().findInbound(null, null, status, null, null, null, projectId, false), null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally{
			tsc.closeSession();
		}
	}
}
