package es.imim.bg.ztools.cli;

public class ToolException extends Exception {

	private static final long serialVersionUID = -2988569516373185054L;

	public ToolException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ToolException(String msg) {
		super(msg);
	}
	
	public ToolException(Throwable cause) {
		super(cause);
	}
}
