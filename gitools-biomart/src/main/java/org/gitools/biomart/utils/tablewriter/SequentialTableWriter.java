package org.gitools.biomart.utils.tablewriter;

public interface SequentialTableWriter {

	void open() throws Exception;

	void write(String[] rowFields) throws Exception;

	void close();
}