package org.irdresearch.smstarseel.telenor;

import java.nio.channels.SeekableByteChannel;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.irdresearch.smstarseel.rest.telenor.Authenticate;
import org.irdresearch.smstarseel.rest.telenor.TelenorOutboundResource;
import org.irdresearch.smstarseel.rest.util.HttpUtil;
import org.json.JSONObject;
import org.json.XML;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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

public class TelenorOutboundResourceTest {

	private String BASE_URL = "/rest/telenor/outbound";
	MockHttpServletRequest req;

	@Mock
	private MockHttpServletResponse resp = new MockHttpServletResponse();

	@Autowired
	private TelenorOutboundResource tor;
	
	AnnotationMethodHandlerAdapter handlerAdapter;
	
	@Before
	public void setup() {
		tor = new TelenorOutboundResource();
		
		handlerAdapter = new AnnotationMethodHandlerAdapter();
		HttpMessageConverter[] messageConverters = { new MappingJacksonHttpMessageConverter() };
		handlerAdapter.setMessageConverters(messageConverters);
	}

	@Test
	public void shouldSendErrorIfReceipientIsEmptyOrInvalid() throws Exception {
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		
		//Empty recipient
		MockHttpServletRequest mockRequest = createRequest(BASE_URL+"/send", RequestMethod.GET);
		handlerAdapter.handle(mockRequest , mockResponse, tor);

		Map<String, Object> mapResp = new Gson().fromJson(mockResponse.getContentAsString(), new TypeToken<HashMap<String, Object>>() {}.getType());

		System.out.println(mapResp);
		
		assertThat(mapResp, Matchers.<String, Object>hasEntry("ERROR", true));
		assertThat(mapResp, hasKey("ERROR_MESSAGE"));
		assertThat(mapResp.get("ERROR_MESSAGE").toString().toLowerCase(), allOf(
				containsString("recipient"), containsString("to"), containsString("param")));
		
		//Empty recipient
		mockResponse = new MockHttpServletResponse();
		mockRequest = createRequest(BASE_URL+"/send", RequestMethod.GET);
		mockRequest.addParameter("to", "");
		handlerAdapter.handle(mockRequest , mockResponse, tor);
		
		mapResp = new Gson().fromJson(mockResponse.getContentAsString(), new TypeToken<HashMap<String, Object>>() {}.getType());

		System.out.println(mapResp);
		
		assertThat(mapResp, Matchers.<String, Object>hasEntry("ERROR", true));
		assertThat(mapResp, hasKey("ERROR_MESSAGE"));
		assertThat(mapResp.get("ERROR_MESSAGE").toString().toLowerCase(), allOf(
				containsString("recipient"), containsString("to"), containsString("param")));
		
		//Invalid recipient
		mockResponse = new MockHttpServletResponse();
		mockRequest = createRequest(BASE_URL+"/send", RequestMethod.GET);
		mockRequest.addParameter("to", "283");
		handlerAdapter.handle(mockRequest , mockResponse, tor);
		
		mapResp = new Gson().fromJson(mockResponse.getContentAsString(), new TypeToken<HashMap<String, Object>>() {}.getType());
		
		System.out.println(mapResp);
		assertThat(mapResp, Matchers.<String, Object>hasEntry("ERROR", true));
		assertThat(mapResp, hasKey("ERROR_MESSAGE"));
		assertThat(mapResp.get("ERROR_MESSAGE").toString().toLowerCase(), allOf(
				containsString("recipient"), containsString("cell number"), containsString("format")));
	}
	
	@Test
	public void shouldSendErrorIfTextIsEmpty() throws Exception {
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		
		//No text
		MockHttpServletRequest mockRequest = createRequest(BASE_URL+"/send", RequestMethod.GET);
		mockRequest.addParameter("to", "923453232323");
		handlerAdapter.handle(mockRequest , mockResponse, tor);

		Map<String, Object> mapResp = new Gson().fromJson(mockResponse.getContentAsString(), new TypeToken<HashMap<String, Object>>() {}.getType());

		System.out.println(mapResp);
		
		assertThat(mapResp, Matchers.<String, Object>hasEntry("ERROR", true));
		assertThat(mapResp, hasKey("ERROR_MESSAGE"));
		assertThat(mapResp.get("ERROR_MESSAGE").toString().toLowerCase(), allOf(
				containsString("text"), containsString("param")));
		
		//Empty text
		mockResponse = new MockHttpServletResponse();
		mockRequest = createRequest(BASE_URL+"/send", RequestMethod.GET);
		mockRequest.addParameter("to", "923453232323");
		mockRequest.addParameter("text", "");
		handlerAdapter.handle(mockRequest , mockResponse, tor);
		
		mapResp = new Gson().fromJson(mockResponse.getContentAsString(), new TypeToken<HashMap<String, Object>>() {}.getType());

		System.out.println(mapResp);
		
		assertThat(mapResp, Matchers.<String, Object>hasEntry("ERROR", true));
		assertThat(mapResp, hasKey("ERROR_MESSAGE"));
		assertThat(mapResp.get("ERROR_MESSAGE").toString().toLowerCase(), allOf(
				containsString("text"), containsString("param")));		
	}
	
	@Test
	public void shouldSendErrorIfNoAuthentication() throws Exception {
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		
		//No authentication
		tor.auth = null;
		
		MockHttpServletRequest mockRequest = createRequest(BASE_URL+"/send", RequestMethod.GET);
		mockRequest.addParameter("to", "923453232323");
		mockRequest.addParameter("text", "test text");
		handlerAdapter.handle(mockRequest , mockResponse, tor);

		Map<String, Object> mapResp = new Gson().fromJson(mockResponse.getContentAsString(), new TypeToken<HashMap<String, Object>>() {}.getType());

		System.out.println(mapResp);
		
		assertThat(mapResp, Matchers.<String, Object>hasEntry("ERROR", true));
		assertThat(mapResp, hasKey("ERROR_MESSAGE"));
		assertThat(mapResp.get("ERROR_MESSAGE").toString().toLowerCase(), allOf(
				containsString("authent"), containsString("unhandled"), containsString("error")));
		
		//No authentication
		tor.auth = new Authenticate();
		
		mockResponse = new MockHttpServletResponse();
		mockRequest = createRequest(BASE_URL+"/send", RequestMethod.GET);
		mockRequest.addParameter("to", "923453232323");
		mockRequest.addParameter("text", "test text");
		handlerAdapter.handle(mockRequest , mockResponse, tor);

		mapResp = new Gson().fromJson(mockResponse.getContentAsString(), new TypeToken<HashMap<String, Object>>() {}.getType());

		System.out.println(mapResp);
		
		assertThat(mapResp, Matchers.<String, Object>hasEntry("ERROR", true));
		assertThat(mapResp, hasKey("ERROR_MESSAGE"));
		assertThat(mapResp.get("ERROR_MESSAGE").toString().toLowerCase(), allOf(
				containsString("authent"), containsString("unhandled"), containsString("error")));
	}
	
	@Test //TODO
	public void shouldSendSuccessIfEverythingPasses() throws Exception {
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		
		//No authentication
		tor.outboundSendUrl = "https://telenorcsms.com.pk:27677/corporate_sms2/api/sendsms.jsp";
		tor.auth = new Authenticate();
		tor.auth.authenticatorUrl = "https://telenorcsms.com.pk:27677/corporate_sms2/api/auth.jsp";
		tor.auth.pingerUrl = "https://telenorcsms.com.pk:27677/corporate_sms2/api/ping.jsp";
		tor.auth.username = "923458299433";
		tor.auth.password = "LTZCOS";
		
		
		MockHttpServletRequest mockRequest = createRequest(BASE_URL+"/send", RequestMethod.GET);
		mockRequest.addParameter("to", "923343872951");
		mockRequest.addParameter("text", "THRIVE TEST TEXT");
		mockRequest.addParameter("mask", "IRD-PSZ");
		handlerAdapter.handle(mockRequest , mockResponse, tor);

		Map<String, Object> mapResp = new Gson().fromJson(mockResponse.getContentAsString(), new TypeToken<HashMap<String, Object>>() {}.getType());

		System.out.println(mapResp);
		
		assertThat(mapResp, Matchers.<String, Object>hasEntry("SUCCESS", true));
		assertThat(mapResp, hasKey("ERROR_MESSAGE"));
		assertThat(mapResp.get("ERROR_MESSAGE").toString().toLowerCase(), allOf(
				containsString("authent"), containsString("unhandled"), containsString("error")));
	}
	
	private MockHttpServletRequest createRequest(String URL, RequestMethod method) {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		// mockRequest.setContentType(MediaType.APPLICATION_JSON.toString());
		mockRequest.setMethod(method.name());
		mockRequest.setRequestURI(URL);
		mockRequest.setAttribute(HandlerMapping.class.getName()+".introspectTypeLevelMapping", true);
		return mockRequest;
	}
	public static void main(String[] args) {
		System.out.println(HttpUtil.post("https://telenorcsms.com.pk:27677/corporate_sms2/api/sendsms.jsp", 
				"to=923343872951&text=TEST THRIVE DIRECT TEXT", "", "923458299433", "LTZCOS").body());
	}
}
