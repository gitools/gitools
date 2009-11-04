package org.gitools.model.table;

public class TableFormatException extends RuntimeException {

	private static final long serialVersionUID = 1350759217738437851L;

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
