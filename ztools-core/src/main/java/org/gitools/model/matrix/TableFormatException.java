package org.gitools.model.matrix;

public class TableFormatException extends RuntimeException {

	public TableFormatException(Throwable cause) {
		super(cause);
	}
	
	public TableFormatException(String message) {
		super(message);
	}
	
	public TableFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
