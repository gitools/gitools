package org.gitools.persistence;

public class PersistenceException extends Exception {

	private static final long serialVersionUID = -3292740209879198812L;

	public PersistenceException(Throwable cause) {
		super(cause);
	}
	
	public PersistenceException(String message) {
		super(message);
	}
	
	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}
}
