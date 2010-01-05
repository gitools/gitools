package org.gitools.biomart.tablewriter;

public interface SequentialTableWriter {

	void open() throws Exception;

	void write(String[] rowFields) throws Exception;

	void close();
}
