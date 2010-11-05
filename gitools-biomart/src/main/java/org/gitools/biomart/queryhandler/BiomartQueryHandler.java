package org.gitools.biomart.queryhandler;

public interface BiomartQueryHandler {

	void begin() throws Exception;

	void line(String[] fields) throws Exception;

	void end();
}
