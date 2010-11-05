package org.gitools.utils;

import org.apache.commons.csv.CSVStrategy;

public class CSVStrategies {

	public static final CSVStrategy TSV = 
		new CSVStrategy('\t', '"', '#', true, true, true);
	
	public static final CSVStrategy CSV = 
		new CSVStrategy(',', '"', '#', true, true, true);
}
