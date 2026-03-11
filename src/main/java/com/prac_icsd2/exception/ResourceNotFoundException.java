package com.prac_icsd2.exception;

public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8574700826566433658L;
	public ResourceNotFoundException()
	{
		
	}
	public ResourceNotFoundException(String msg)
	{
		super(msg+"resource not found exception fired");
	}
}
