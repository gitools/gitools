package es.imim.bg.csv;

@Deprecated
public class CSVProcessorAdapter implements CSVProcessor {

	public boolean start() throws CSVException { return true; }
	public boolean end() throws CSVException { return true; }

	public boolean lineStart(int row) throws CSVException { return true; }
	public boolean lineEnd(int row) throws CSVException { return true; }
	
	public boolean field(String field, int row, int col) throws CSVException { return true; }

}
