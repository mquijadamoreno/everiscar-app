package com.everis.bmw.app.exceptions;

public class CarStateNotValidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CarStateNotValidException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public CarStateNotValidException(String msg) {
		super(msg);
	}
	
	public CarStateNotValidException(Throwable cause) {
		super(cause);
	}
	
}
