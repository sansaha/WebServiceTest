package com.lexmark.ws;

import javax.jws.WebService;

@WebService
public class CalculatorServiceImpl implements CalculatorService {

	@Override
	public int add(int input1, int input2) {
		return input1+input2;
	}

	@Override
	public int subtract(int input1, int input2) {
		return input1-input2;
	}

	@Override
	public int multiply(int input1, int input2) {
		return input1*input2;
	}

	@Override
	public int divide(int input1, int input2) {
		return input1/input2;
	}

}
