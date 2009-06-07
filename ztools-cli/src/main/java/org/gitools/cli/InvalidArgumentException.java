package org.gitools.cli;

public class InvalidArgumentException extends Exception {

	private static final long serialVersionUID = 5877801897717716451L;

	public InvalidArgumentException(String msg) {
		super(msg);
	}
	
	public InvalidArgumentException(Throwable cause) {
		super(cause);
	}
	
	public InvalidArgumentException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
