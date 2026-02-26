package com.prac_icsd2.exception;

public class EntityAlreadyExistException extends RuntimeException {

	public EntityAlreadyExistException() {
		
	}
	public EntityAlreadyExistException(String msg) {
		super(msg+" "+"Entity Already Exists exception fired");
	}
}
