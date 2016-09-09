package org.irdresearch.smstarseel.telenor;

import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.irdresearch.smstarseel.rest.telenor.Authenticate;
import org.irdresearch.smstarseel.rest.util.HttpUtil;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AuthenticatorTest {

	private Authenticate as;
	
	@Before
	public void setup() {
		as = new Authenticate();
	}
	
	@Test
	public void shouldThrowExceptionIfMsisdnIsEmpty() throws JSONException {
		Map<String, Object> r = as.authenticate("", "", null);
		Assert.assertTrue((boolean) r.get("ERROR"));
		Assert.assertTrue(r.get("ERROR_MESSAGE").toString().toLowerCase().contains("msisdn"));
	}
	
	@Test
	public void shouldThrowExceptionIfPasswordIsEmpty() throws JSONException {
		Map<String, Object> r = as.authenticate("test", "", null);
		Assert.assertTrue((boolean) r.get("ERROR"));
		Assert.assertTrue(r.get("ERROR_MESSAGE").toString().toLowerCase().contains("password"));	
	}
	
	@Test
	public void shouldThrowExceptionIfTelenorUrlIsNotConfigured() throws JSONException {
		as.authenticatorUrl = "";
		Map<String, Object> r = as.authenticate("tst", "pw", null);
		Assert.assertTrue((boolean) r.get("ERROR"));
		Assert.assertTrue(r.get("ERROR_MESSAGE").toString().toLowerCase().contains("telenor.url.authenticator"));
	}
	
	@Test
	public void shouldPassIfEverythingIsWorking() throws JSONException {
		as.authenticatorUrl = "https://telenorcsms.com.pk:27677/corporate_sms2/api/auth.jsp";
		String msisdn = "923458299433";
		String password = "LTZCOS";
		Map<String, Object> r = as.authenticate(msisdn, password, null);
		Assert.assertTrue((boolean) r.get("SUCCESS"));
		Assert.assertTrue(r.get("data") != null);
		Assert.assertTrue(r.get("response").toString().equalsIgnoreCase("ok"));
	}
	
	@Test
	public void shouldFailIfAuthenticationCredentailsAreIncorrect() throws JSONException {
		as.authenticatorUrl = "https://telenorcsms.com.pk:27677/corporate_sms2/api/auth.jsp";
		String msisdn = "923458299433";
		String password = "LTECOS";
		Map<String, Object> r = as.authenticate(msisdn, password, null);
		Assert.assertTrue((boolean) r.get("ERROR"));
		Assert.assertTrue(r.get("data") != null);
		Assert.assertTrue(r.get("response").toString().equalsIgnoreCase("error"));
	}
	
	public void shouldPingServerIfKeepAliveIsTrue() {
		//TODO
	}
	
}
