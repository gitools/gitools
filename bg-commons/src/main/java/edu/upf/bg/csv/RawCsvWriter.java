package edu.upf.bg.csv;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class RawCsvWriter {

	private PrintWriter out;
	private char separator;
	private char quote;
	
	public RawCsvWriter(Writer writer, char separator, char quote) {
		this.out = new PrintWriter(writer);
		this.separator = separator;
		this.quote = quote;
	}
	
	public void writeNewLine() {
		out.println();
	}
	
	public void writeValue(String value) {
		out.print(value);
	}
	
	public void writeQuotedValue(String value) {
		out.print(quote);
		out.print(value);
		out.print(quote);
	}
	
	public void writeProperty(String name, String value) {
		out.print(name);
		out.print(separator);
		writeQuotedValue(value);
		out.println();
	}

	public void writePropertyList(String name, String[] values) {
		out.print(name);
		if (values.length > 0) {
			for (String value : values) {
				out.print(separator);
				writeQuotedValue(value);
			}
		}
		out.println();
	}
	
	public void writePropertyList(String name, List<String> values) {
		out.print(name);
		if (values.size() > 0) {
			for (String value : values) {
				out.print(separator);
				writeQuotedValue(value);
			}
		}
		out.println();
	}

	public void writeSeparator() {
		out.print(separator);
	}

	public void close() {
		out.close();
	}
}
