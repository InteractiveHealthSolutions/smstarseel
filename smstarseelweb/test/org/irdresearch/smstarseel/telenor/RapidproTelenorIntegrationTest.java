package org.irdresearch.smstarseel.telenor;

import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matchers;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.Project;
import org.irdresearch.smstarseel.data.Service;
import org.irdresearch.smstarseel.rest.ServiceResource;
import org.irdresearch.smstarseel.rest.telenor.Authenticate;
import org.irdresearch.smstarseel.rest.telenor.TelenorOutboundResource;
import org.irdresearch.smstarseel.rest.util.HttpUtil;
import org.irdresearch.smstarseel.service.DeviceService;
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
public class RapidproTelenorIntegrationTest {
	private String BASE_URL = "/rest/telenor/outbound";
	private TelenorOutboundResource tor;
	
	@Mock
	private ServiceResource serviceResource;
	
	@Mock
	TarseelServices tarseelServices;

	@Mock
	private Authenticate authenticator;
	
	@SuppressWarnings("deprecation")
	AnnotationMethodHandlerAdapter handlerAdapter;
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(TarseelContext.class);

		tor = new TelenorOutboundResource(authenticator, serviceResource);
		
		handlerAdapter = new AnnotationMethodHandlerAdapter();
		HttpMessageConverter[] messageConverters = { new MappingJacksonHttpMessageConverter() };
		handlerAdapter.setMessageConverters(messageConverters);
		
		BDDMockito.given(TarseelContext.getServices()).willReturn(tarseelServices);
	}
	
	@Test
	public void shouldRejectIfNoAPIKeyProvided() throws Exception {
		when(serviceResource.authenticatedService(any(HttpServletRequest.class))).thenCallRealMethod();

		MockHttpServletRequest mockRequest = createRequest(BASE_URL+"/send", RequestMethod.GET);
		mockRequest.addParameter("to", "+923788123123");
		mockRequest.addParameter("from", "923468244244");
		mockRequest.addParameter("channel", "16");
		mockRequest.addParameter("id", "2599235");
		mockRequest.addParameter("text", "THRIVE TEST TEXT");
		//mockRequest.addParameter("mask", "IRD-PSZ");
		
		Map<String, Object> mapResp = executeRequest(mockRequest, tor);
		
		assertThat(mapResp, Matchers.<String, Object>hasEntry("ERROR", true));
		assertThat(mapResp, hasKey("ERROR_MESSAGE"));
		assertThat(mapResp.get("ERROR_MESSAGE").toString().toLowerCase(), allOf(
				containsString("auth"), containsString("api_key"), containsString("service")));
	}
	
	@Test
	public void shouldRejectIfNoMaskProvided() throws Exception {
		when(serviceResource.authenticatedService(any(HttpServletRequest.class))).thenReturn(new Service());
		when(tarseelServices.getDeviceService()).thenReturn(mock(DeviceService.class));
		when(tarseelServices.getDeviceService().findProject(any(String.class))).thenReturn(Collections.singletonList(new Project(1)));

		MockHttpServletRequest mockRequest = createRequest(BASE_URL+"/send", RequestMethod.GET);
		mockRequest.addParameter("to", "+923788123123");
		mockRequest.addParameter("from", "923468244244");
		mockRequest.addParameter("channel", "16");
		mockRequest.addParameter("id", "2599235");
		mockRequest.addParameter("text", "THRIVE TEST TEXT");
		mockRequest.addParameter("project", "THRIVE TEST TEXT");
		
		Map<String, Object> mapResp = executeRequest(mockRequest, tor);
		
		assertThat(mapResp, Matchers.<String, Object>hasEntry("ERROR", true));
		assertThat(mapResp, hasKey("ERROR_MESSAGE"));
		assertThat(mapResp.get("ERROR_MESSAGE").toString().toLowerCase(), allOf(
				containsString("mask"), containsString("param")));	
	}
	
	@Test
	public void test() throws Exception {
        BDDMockito.given(TarseelContext.getServices()).willReturn(tarseelServices);

		when(serviceResource.authenticatedService(any(HttpServletRequest.class))).thenReturn(new Service());

		MockHttpServletRequest mockRequest = createRequest(BASE_URL+"/send", RequestMethod.GET);
		mockRequest.addParameter("to", "+923788123123");
		mockRequest.addParameter("from", "923468244244");
		mockRequest.addParameter("channel", "16");
		mockRequest.addParameter("id", "2599235");
		mockRequest.addParameter("text", "THRIVE TEST TEXT");
		mockRequest.addParameter("mask", "IRD-PSZ");
		
		Map<String, Object> mapResp = executeRequest(mockRequest, tor);
		
		assertThat(mapResp, Matchers.<String, Object>hasEntry("ERROR", true));
		assertThat(mapResp, hasKey("ERROR_MESSAGE"));
		assertThat(mapResp.get("ERROR_MESSAGE").toString().toLowerCase(), allOf(
				containsString("recipient"), containsString("to"), containsString("param")));	
	}

	private MockHttpServletRequest createRequest(String URL, RequestMethod method) {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		// mockRequest.setContentType(MediaType.APPLICATION_JSON.toString());
		mockRequest.setMethod(method.name());
		mockRequest.setRequestURI(URL);
		mockRequest.setAttribute(HandlerMapping.class.getName()+".introspectTypeLevelMapping", true);
		return mockRequest;
	}
	
	private Map<String, Object> executeRequest(MockHttpServletRequest mockRequest, Object resource) throws Exception {
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		handlerAdapter.handle(mockRequest , mockResponse, resource);

		Map<String, Object> mapResp = new Gson().fromJson(mockResponse.getContentAsString(), new TypeToken<HashMap<String, Object>>() {}.getType());

		System.out.println(mapResp);
		return mapResp;
	}
}
