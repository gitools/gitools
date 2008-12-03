package es.imim.bg.ztools.cli;

public class CliToolException extends Exception {

	private static final long serialVersionUID = -2988569516373185054L;

	public CliToolException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public CliToolException(String msg) {
		super(msg);
	}
	
	public CliToolException(Throwable cause) {
		super(cause);
	}
}
