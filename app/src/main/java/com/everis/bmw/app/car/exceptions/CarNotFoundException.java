package com.everis.bmw.app.car.exceptions;

public class CarNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CarNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public CarNotFoundException(String msg) {
		super(msg);
	}
	
	public CarNotFoundException(Throwable cause) {
		super(cause);
	}
	
	

}
