package com.prac_icsd2.exception;

public class EntityAlreadyExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7948931874569683856L;
	public EntityAlreadyExistException() {
		
	}
	public EntityAlreadyExistException(String msg) {
		super(msg+" "+"Entity Already Exists exception fired");
	}
}
