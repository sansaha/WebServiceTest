package com.lexmark.ws.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.DataProvider;

public class LexWSDataProvider {
	
	private static Properties testMetaDataProperties = new Properties();
	
	static{
		try {
			testMetaDataProperties.load(new FileInputStream("data/test-metadata.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@DataProvider
	public static Object[][] getAllWebServices(){
		
		String webServices = testMetaDataProperties.getProperty("TEST_WEB_SERVICE");
		
		List<LexWebService> webServiceList = new ArrayList<LexWebService>();
		
		if(webServices != null){
			String[] webServiceArray = webServices.split(",");
			for(String webSvc : webServiceArray){
				Properties serviceMetaDataProperties = new Properties();
				try {
					serviceMetaDataProperties.load(new FileInputStream("data/"+webSvc+"/service-metadata.properties"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				String httpMethod = serviceMetaDataProperties.getProperty("HTTP_METHOD_TYPE");
				String reqURL = serviceMetaDataProperties.getProperty("REQUEST_URL");
				
				Map<String,String> headerInfoMap = new HashMap<String,String>();
				String headerInfo = serviceMetaDataProperties.getProperty("HEADER_INFO_PAIRS");
				if(headerInfo != null && !headerInfo.trim().equals("")){
					String[] headerInfoArray = headerInfo.split(",");
					for(String headerInfoPair : headerInfoArray){
						String[] headerKeyValue = headerInfoPair.split("\\^");
						headerInfoMap.put(headerKeyValue[0], headerKeyValue[1]);
					}
				}
				
				Map<String,String> urlInfoMap = new HashMap<String,String>();
				String urlPlaceholderInfos = serviceMetaDataProperties.getProperty("URL_PLACEHOLDERS");
				if(urlPlaceholderInfos != null && !urlPlaceholderInfos.trim().equals("")){
					String[] urlPlaceholderInfoArray = urlPlaceholderInfos.split(",");
					for(String urlPlaceholderInfo : urlPlaceholderInfoArray){
						String[] keyValue = urlPlaceholderInfo.split("\\^");
						urlInfoMap.put(keyValue[0], keyValue[1]);
					}
				}
				
				
				String requestResponsePairCombinations = serviceMetaDataProperties.getProperty("REQUEST_RESPONSE_PAIRS");
				if(requestResponsePairCombinations != null){
					String[] reqRespCombinationArray = requestResponsePairCombinations.split(",");
					for(String reqRespComb : reqRespCombinationArray){
						String[] reqRespArray = reqRespComb.split("\\^");
						
						String requestFilePath = "data/"+webSvc+"/"+reqRespArray[0]+".txt";
						String responseFilePath = "data/"+webSvc+"/"+reqRespArray[1]+".txt";
						
						try {
							String reqString = getFileContentAsString(requestFilePath);
							String responseString = getFileContentAsString(responseFilePath);
							LexWebService lexWebService = new LexWebService();
							
							lexWebService.setWebServiceRequest(reqString);
							lexWebService.setExpectedWebServiceResponse(responseString);
							
							lexWebService.setHttpMethodEnum(HttpMethodEnum.valueOf(httpMethod));
							
							lexWebService.setHeaderParameters(headerInfoMap);
							lexWebService.setRequestURL(reqURL);
							
							if(!urlInfoMap.isEmpty()){
								lexWebService.setUrlPlaceholders(urlInfoMap);
							}
							
							webServiceList.add(lexWebService);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
					}
				}
				
			}
		}
		
		//Load the two dimensional array with the locale list results
		Object [][] wsArray = new Object[webServiceList.size()][1];
			int index = 0;
			for(LexWebService lexWebService:webServiceList){
				wsArray[index++][0] = lexWebService;
			}
				
		return wsArray;
	}
	
	private static String getFileContentAsString(String path) throws IOException{
		String content = null;
		if(Files.isReadable(Paths.get(path))){
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			content = new String(encoded);
		}
		
		return content;
	}
	
	
	
	

}
