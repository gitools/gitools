package es.imim.bg.csv;

@Deprecated
public interface CSVProcessor {
	public boolean start() throws CSVException;
	public boolean end() throws CSVException;
	
	public boolean lineStart(int row) throws CSVException;
	public boolean lineEnd(int row) throws CSVException;
	
	public boolean field(String field, int row, int col) throws CSVException;
}
