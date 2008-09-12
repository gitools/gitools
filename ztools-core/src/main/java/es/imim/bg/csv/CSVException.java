package es.imim.bg.csv;

@Deprecated
public class CSVException extends Exception {

	private static final long serialVersionUID = 8673467489532683989L;

	public static final String msgSeparatorExpected = "Separator expected.";
	public static final String msgEndOfFileExpected = "End of file expected.";

	private int line;
	private int col;
	private String csvMessage;
	
	public CSVException(int line, int col, String msg) {
		super("line " + (line + 1) + ", col " + (col + 1) + " : " + msg);
		this.line = line + 1;
		this.col = col + 1;
		this.csvMessage = msg;
	}
	
	public CSVException(int line, int col, String msg, Exception e) {
		super("line " + (line + 1) + ", col " + (col + 1) + " : " + msg, e);
		this.line = line + 1;
		this.col = col + 1;
		this.csvMessage = msg;
	}

	public CSVException() {
		this(0,0,"");
	}
	
	public int getLine() {
		return line;
	}
	
	public int getCol() {
		return col;
	}
	
	public String getCsvMessage() {
		return csvMessage;
	}
}
