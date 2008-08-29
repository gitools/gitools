package es.imim.bg.ztools.zcalc.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;

import es.imim.bg.progressmonitor.ProgressMonitor;

public class DataFile {

	protected char separator;
	protected char quote;

	// Output
	
	protected String[] condNames;
	protected String[] itemNames;
	
	protected String dataName;
	protected DoubleMatrix2D data;
	
	public DataFile() {
		this.separator = '\t';
		this.quote = '"';
	}
	
	public void load(ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, DataFormatException {
	}
	
	public void loadInfo(String dataFile, ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, DataFormatException {

		//TODO Reader worksetReader = openReader(worksetFile);
		
		monitor.begin("Reading names ...", 1);
		
		Reader dataReader = openReader(dataFile);
		
		CSVStrategy dataCsvStrategy = new CSVStrategy(separator, quote, '#', true, true, true);
		CSVParser dataParser = new CSVParser(dataReader, dataCsvStrategy);
		
		final List<Integer> dataPropIndices = new ArrayList<Integer>();
		final List<String> dataPropNames = new ArrayList<String>();
		final Map<String, Integer> itemIndexMap = new TreeMap<String, Integer>();
		
		dataName = readDataInfo(dataParser, dataPropNames, dataPropIndices, itemIndexMap);
		
		int numProps = dataPropNames.size();
		
		dataReader.close();
		
		monitor.end();
		
		monitor.begin("Reading data ...", 1);
		
		monitor.info(numProps + " columns");
		monitor.info(numItems + " items");
		
		// Load data

		dataReader = openReader(dataFile);
		dataParser = new CSVParser(dataReader, dataCsvStrategy);
		dataParser.getLine();
		
		condNames = new String[numProps];
		
		itemNames = new String[numItems];
		data = DoubleFactory2D.dense.make(numItems, numProps);
		
		loadData(dataParser, 
				dataPropNames, dataPropIndices, condNames, 
				itemDataRow, itemNames, data);

		dataReader.close();
		
		monitor.end();
	}

	private String readDataInfo(
			CSVParser dataParser, 
			List<String> dataPropNames, 
			List<Integer> dataPropIndices, 
			Map<String, Integer> itemIndexMap) throws IOException, DataFormatException {
		
		// read header
		
		final String[] header = dataParser.getLine();
		if (header.length < 2)
			throw new DataFormatException("At least 2 columns expected.");
		
		// read datafile name and column names
		
		String dataName = header[0];
		
		for (int i = 1; i < header.length; i++) {
			// if (header[i] in selected properties) {
			dataPropNames.add(header[i]);
			dataPropIndices.add(i - 1);
			// }
		}
		
		String[] rowData;
		
		// read item names
		
		int index = 0;
		while ((rowData = dataParser.getLine()) != null)
			itemIndexMap.put(rowData[0], index++);
		
		return dataName;
	}
	
	private Reader openReader(String fileName) throws FileNotFoundException, IOException {
		if (fileName == null)
			return null;
		
		if (fileName.endsWith(".gz"))
			return
				new InputStreamReader(
					new GZIPInputStream(
							new FileInputStream(
									new File(fileName))));
		else
			return 
				new BufferedReader(
					new FileReader(
							new File(fileName)));
	}
}
