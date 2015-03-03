package com.lexmark.ws.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;


public class LexWebService {
	
	private HttpMethodEnum httpMethodEnum;
	
	private String webServiceRequest;
	
	private String expectedWebServiceResponse;
	
	private String actualWebServiceResponse;
	
	private LexWSCredential webServiceCredential;
	
	private String requestURL;
	
	private Map<String,String> headerParameters;
	
	private Map<String,String> urlPlaceholders;

	public String getWebServiceRequest() {
		return webServiceRequest;
	}

	public void setWebServiceRequest(String webServiceRequest) {
		this.webServiceRequest = webServiceRequest;
	}

	public String getExpectedWebServiceResponse() {
		return expectedWebServiceResponse;
	}

	public void setExpectedWebServiceResponse(
			String expectedWebServiceResponse) {
		this.expectedWebServiceResponse = expectedWebServiceResponse;
	}

	public String getActualWebServiceResponse() {
		return actualWebServiceResponse;
	}

	public void setActualWebServiceResponse(
			String actualWebServiceResponse) {
		this.actualWebServiceResponse = actualWebServiceResponse;
	}

	public LexWSCredential getWebServiceCredential() {
		return webServiceCredential;
	}

	public void setWebServiceCredential(LexWSCredential webServiceCredential) {
		this.webServiceCredential = webServiceCredential;
	}

	public HttpMethodEnum getHttpMethodEnum() {
		return httpMethodEnum;
	}

	public void setHttpMethodEnum(HttpMethodEnum httpMethodEnum) {
		this.httpMethodEnum = httpMethodEnum;
	}
		
	public void setHeaderParameters(Map<String, String> headerParameters) {
		this.headerParameters = headerParameters;
	}
	
	
	public void setUrlPlaceholders(Map<String, String> urlPlaceholders) {
		this.urlPlaceholders = urlPlaceholders;
	}
		
	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public int callWebService(){
		int responseCode = 0;
		validateInputData();
		
		String wsUrl = requestURL;
		
		if(this.urlPlaceholders != null && !this.urlPlaceholders.isEmpty()){
			Set<String> placeHolderNames = urlPlaceholders.keySet();
			for(String name:placeHolderNames){
				wsUrl = wsUrl.replace("{"+name+"}", urlPlaceholders.get(name));
			}
		}
		
		if(httpMethodEnum == HttpMethodEnum.GET && webServiceRequest != null && !webServiceRequest.trim().equals("")){
			wsUrl = wsUrl+"?"+webServiceRequest;
		}
		HttpURLConnection httpCon = null;
		
		 try {
			 System.out.println("Request URL::"+wsUrl);
			 URL url=new URL(wsUrl);
			 httpCon = (HttpURLConnection)url.openConnection();
			 httpCon.setAllowUserInteraction(false);
			 httpCon.setDoOutput(true);
			 //TODO need further investigation for do input
			 httpCon.setDoInput(true);
			 
			 Set<String> headerContents = this.headerParameters.keySet();
			 for(String headerName:headerContents){
				 httpCon.setRequestProperty(headerName, headerParameters.get(headerName));
			 }
			 
			 httpCon.setRequestMethod(httpMethodEnum.toString());
			 
			 if(httpMethodEnum != HttpMethodEnum.GET){
				 OutputStream postStream = httpCon.getOutputStream();
				 PrintWriter prnWriter = new PrintWriter(postStream);
				 prnWriter.println(webServiceRequest);
				 prnWriter.flush();
				 prnWriter.close();
			 }
			 
			 responseCode = httpCon.getResponseCode();
			 String responseText = httpCon.getResponseMessage();
			 System.out.println("Post code: " + responseCode);
			 System.out.println("Post response: " + responseText);
			 InputStreamReader inputStreamReader = new InputStreamReader(
			 httpCon.getInputStream() );
			 BufferedReader reader = new BufferedReader( inputStreamReader );
			 String inputLine;
			 StringBuffer responseMessage = new StringBuffer("");
			 while ((inputLine = reader.readLine()) != null)
			 responseMessage.append(inputLine);
			
			 inputStreamReader.close();
			 reader.close();
			 
			 actualWebServiceResponse = responseMessage.toString();
			 System.out.println("Web Service Reply: " + actualWebServiceResponse);
			 
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			
			if(httpCon != null){
				httpCon.disconnect();
			}
			
		}
		 
		 return responseCode;
	}
	
	public void validateInputData(){
		//TODO need implementation for input data validation
	}
	
	
	
	

}
