package edu.upf.bg.tools.exception;

public class ToolExitException extends ToolException {

	private static final long serialVersionUID = -1922487556805827074L;

	private int returnCode;
	
	public ToolExitException(int returnCode) {
		this.returnCode = returnCode;
	}
	
	public final int getReturnCode() {
		return returnCode;
	}
}
