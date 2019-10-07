package org.irdresearch.smstarseel.its;

import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matchers;
import org.irdresearch.smstarseel.TarseelWebGlobals.SmsServiceConstants;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelGlobals;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.InboundMessage;
import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.data.Service;
import org.irdresearch.smstarseel.rest.ServiceResource;
import org.irdresearch.smstarseel.rest.its.ITSInboundServiceJob;
import org.irdresearch.smstarseel.rest.its.ITSOutboundResource;
import org.irdresearch.smstarseel.rest.telenor.Authenticate;
import org.irdresearch.smstarseel.rest.telenor.TelenorOutboundResource;
import org.irdresearch.smstarseel.rest.util.HttpUtil;
import org.irdresearch.smstarseel.service.CustomQueryService;
import org.irdresearch.smstarseel.service.DeviceService;
import org.irdresearch.smstarseel.service.SMSService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mock.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ TarseelContext.class })
@PowerMockIgnore({"org.apache.http.conn.ssl.*", "javax.net.ssl.*"})
public class ITSInboundIntegrationTest {
	private ITSInboundServiceJob iisj;
	
	@Mock
	private ServiceResource serviceResource;
	
	@Mock
	TarseelServices tarseelServices;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(TarseelContext.class);

		iisj = new ITSInboundServiceJob();
		
		List<Project> projectl = new ArrayList<>();
		Project p = new Project(1);
		p.setName("karachi");
		projectl.add(p);

		DeviceService devService = mock(DeviceService.class);
		CustomQueryService custService = mock(CustomQueryService.class);
		SMSService smsService = mock(SMSService.class);
		
		BDDMockito.given(TarseelContext.getServices()).willReturn(tarseelServices);
		BDDMockito.given(tarseelServices.getDeviceService()).willReturn(devService);
		BDDMockito.given(tarseelServices.getCustomQueryService()).willReturn(custService);
		BDDMockito.given(tarseelServices.getSmsService()).willReturn(smsService);
		BDDMockito.given(devService.findProject(any(String.class))).willReturn(projectl);
		BDDMockito.given(custService.save(any(Object.class))).willReturn(12);
		Mockito.doNothing().when(smsService).saveInbound(any(InboundMessage.class));
	}
		
	@Test
	public void test() throws Exception {
        BDDMockito.given(TarseelContext.getServices()).willReturn(tarseelServices);
       
        Service s = new Service();
        Project p = new Project(1);
        p.setName("Test");
        s.setProject(p);
		when(serviceResource.authenticatedService(any(HttpServletRequest.class))).thenReturn(s);

		iisj.run();
	}
}
