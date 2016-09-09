package org.irdresearch.smstarseel.telenor;

import org.irdresearch.smstarseel.rest.telenor.TelenorContext;
import org.irdresearch.smstarseel.rest.util.Utils;
import org.junit.Assert;
import org.junit.Test;

public class TelenorContextTest {

	@Test
	public void shouldCreateAndParseCorrectReferenceNumberAndMessageId() {
		Long messageId = 30012102122L;
		String referenceNumber = TelenorContext.createReferenceNumber(messageId);
		Assert.assertEquals("Telenor:30012102122", referenceNumber);
		
		String refNumber = "Telenor:30012102122";
		Assert.assertEquals("30012102122", TelenorContext.getMessageId(refNumber));
	}
}
