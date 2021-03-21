package com.blz.CascadingDelete;

public class PayrollServiceException extends Exception{
	enum ExceptionType{
		CONNECTION_PROBLEM, RETRIEVAL_PROBLEM, UPDATE_PROBLEM, INSERTION_PROBLEM; 
	}
	
	ExceptionType type;
	PayrollServiceException(String message, ExceptionType type){
		super(message);
		this.type = type;
	}
}
