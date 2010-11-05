package edu.upf.bg.tools.exception;

public class ToolException extends Exception {

	private static final long serialVersionUID = 2458110805961757125L;

	public ToolException() {
		super();
	}
	
	public ToolException(String msg) {
		super(msg);
	}
	
	public ToolException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ToolException(Throwable cause) {
		super(cause);
	}
}
