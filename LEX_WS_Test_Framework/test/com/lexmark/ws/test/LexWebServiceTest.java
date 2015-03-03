package com.lexmark.ws.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.lexmark.ws.util.LexWSDataProvider;
import com.lexmark.ws.util.LexWebService;

public class LexWebServiceTest {
	
	@Test(dataProviderClass=LexWSDataProvider.class,dataProvider="getAllWebServices")
	public void validateWebServices(LexWebService lexWebService){
		int responseCode = lexWebService.callWebService();
		Assert.assertEquals(responseCode, 200);
		Assert.assertEquals(lexWebService.getActualWebServiceResponse(), lexWebService.getExpectedWebServiceResponse());
	}
	

}
