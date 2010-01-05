package org.gitools.cli;

public class RequiredArgumentException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public RequiredArgumentException(String msg) {
		super(msg);
	}

}
