package com.lexmark.ws;

import javax.xml.ws.Endpoint;

public class ServicePublishTest {

	public static void main(String[] args) {
		Endpoint.publish("http://localhost:7070/CalculatorService", new CalculatorServiceImpl());
	}
}
