package es.imim.bg.ztools.zcalc.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import es.imim.bg.progressmonitor.ProgressMonitor;

public class ZCalcInput {
	
	// Defaults
	
	public static final char defaultSep = '\t';
	public static final char defaultQuote = '"';
	
	public static final int defaultMinGroupSize = 20;
	public static final int defaultMaxGroupSize = Integer.MAX_VALUE;
	
	// Input 
	
	protected String dataFile;
	protected char dataSep = defaultSep;
	protected char dataQuote = defaultQuote;
	
	protected String groupsFile;
	protected char groupsSep = defaultSep;
	protected char groupsQuote = defaultQuote;

	protected String worksetFile;
	protected char worksetSep = defaultSep;
	protected char worksetQuote = defaultQuote;

	protected int minGroupSize = defaultMinGroupSize;
	protected int maxGroupSize = defaultMaxGroupSize;
	
	// Output
	
	protected String[] propNames;
	protected String[] itemNames;
	
	protected String dataName;
	protected DoubleMatrix2D data;
	
	protected String[] groupNames;
	protected int[][] groupItemIndices;
	
	public ZCalcInput(
			String dataFile, char dataSep, char dataQuote,
			String groupsFile, char groupsSep, char groupsQuote,
			String worksetFile, char worksetSep, char worksetQuote,
			int minGroupSize, int maxGroupSize) {
		
		this.dataFile = dataFile;
		this.dataSep = dataSep;
		this.dataQuote = dataQuote;
		this.groupsFile = groupsFile;
		this.groupsSep = groupsSep;
		this.groupsQuote = groupsQuote;
		this.worksetFile = worksetFile;
		this.worksetSep = worksetSep;
		this.worksetQuote = worksetQuote;
		this.minGroupSize = minGroupSize;
		this.maxGroupSize = maxGroupSize;
	}

	public ZCalcInput(
			String dataFile,
			String groupsFile,
			String worksetFile,
			int minGroupSize, int maxGroupSize) {
		
		this(
			dataFile, defaultSep, defaultQuote,
			groupsFile, defaultSep, defaultQuote,
			worksetFile, defaultSep, defaultQuote,
			minGroupSize, maxGroupSize);
	}
	
	public void load(ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, DataFormatException {
		
		//TODO Reader worksetReader = openReader(worksetFile);
		
		monitor.begin("Reading condition and item names ...", 1);
		
		Reader dataReader = openReader(dataFile);
		CSVStrategy dataCsvStrategy = new CSVStrategy(dataSep, dataQuote, '#', true, true, true);
		CSVParser dataParser = new CSVParser(dataReader, dataCsvStrategy);
		
		final List<Integer> dataPropIndices = new ArrayList<Integer>();
		final List<String> dataPropNames = new ArrayList<String>();
		final Map<String, Integer> itemIndexMap = new TreeMap<String, Integer>();
		
		dataName = readDataInfo(dataParser, dataPropNames, dataPropIndices, itemIndexMap);
		
		int numProps = dataPropNames.size();
		
		monitor.end();
		
		monitor.begin("Reading modules ...", 1);
		
		// read group item indices
		
		Reader groupsReader = openReader(groupsFile);
		CSVStrategy groupsCsvStrategy = new CSVStrategy(groupsSep, groupsQuote, '#', true, true, true);
		CSVParser groupsParser = new CSVParser(groupsReader, groupsCsvStrategy);
		
		final Map<String, SortedSet<Integer>> groupItemsMap = 
			new HashMap<String, SortedSet<Integer>>();
		
		readGroupMappings(groupsParser, itemIndexMap, groupItemsMap);
		
		dataReader.close();
		groupsParser = null;
		
		// copy group names and group item indices to arrays
		
		final Set<Entry<String, SortedSet<Integer>>> entries = groupItemsMap.entrySet();
		final String[] tmpGroupNames = new String[entries.size()];
		final int[][] tmpGroupItemIndices = new int[entries.size()][];
		int index = 0;
		for (Entry<String, SortedSet<Integer>> entry : entries) {
			SortedSet<Integer> indices = entry.getValue();
			tmpGroupNames[index] = entry.getKey();
			int[] ia = tmpGroupItemIndices[index] = new int[indices.size()];
			int i = 0;
			for (Integer idx : indices )
				ia[i++] = idx;
			index++;
		}
		
		// sort groups by number of items
		
		int numGroups = entries.size();
		final Integer[] groupOrder = new Integer[numGroups];
		for (int i = 0; i < groupOrder.length; i++)
			groupOrder[i] = i;
		
		Arrays.sort(groupOrder, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				int l1 = tmpGroupItemIndices[o1].length;
				int l2 = tmpGroupItemIndices[o2].length;
				return l2 - l1;
			}
		});
		
		// filter by number of items
		
		int start = 0;
		int end = numGroups - 1;
		while (start < numGroups 
				&& tmpGroupItemIndices[groupOrder[start]].length > maxGroupSize) {
			int order = groupOrder[start];
			tmpGroupNames[order] = null;
			tmpGroupItemIndices[order] = null;
			start++;
		}
		while (end >= start 
				&& tmpGroupItemIndices[groupOrder[end]].length < minGroupSize) {
			int order = groupOrder[end];
			tmpGroupNames[order] = null;
			tmpGroupItemIndices[order] = null;
			end--;
		}
		end++;
		
		numGroups = end - start;
		groupNames = new String[numGroups];
		groupItemIndices = new int[numGroups][];
		
		// prepare map between original item index 
		// and data row where will be stored,
		// get group names and update group item indices
		
		final int[] itemDataRow = new int[itemIndexMap.size()];
		Arrays.fill(itemDataRow, -1);
		
		int numItems = 0;
		for (int i = start; i < end; i++) {
			int order = groupOrder[i];
			
			groupNames[i - start] = tmpGroupNames[order];
			groupItemIndices[i - start] = tmpGroupItemIndices[order];
			
			int[] indices = tmpGroupItemIndices[order];
			for (int j = 0; j < indices.length; j++) {
				int idx = indices[j];
				if (itemDataRow[idx] < 0)
					itemDataRow[idx] = numItems++;
				indices[j] = itemDataRow[idx];
			}
		}
		
		// Put not used items at the end
		
		for (int i = 0; i < itemDataRow.length; i++)
			if (itemDataRow[i] < 0)
				itemDataRow[i] = numItems++;
		
		monitor.info(numGroups + " groups");
		
		monitor.end();
		
		monitor.begin("Reading data ...", 1);
		
		monitor.info(numProps + " conditions");
		monitor.info(numItems + " items");
		
		// Load data

		dataReader = openReader(dataFile);
		dataParser = new CSVParser(dataReader, dataCsvStrategy);
		dataParser.getLine();
		
		propNames = new String[numProps];
		
		itemNames = new String[numItems];
		data = DoubleFactory2D.dense.make(numItems, numProps);
		
		loadData(dataParser, 
				dataPropNames, dataPropIndices, propNames, 
				itemDataRow, itemNames, data);

		dataReader.close();
		
		monitor.end();
	}
	
	protected String readDataInfo(
			CSVParser dataParser, 
			List<String> dataPropNames, 
			List<Integer> dataPropIndices, 
			Map<String, Integer> itemIndexMap) throws IOException, DataFormatException {
		
		// read header
		
		final String[] header = dataParser.getLine();
		if (header.length < 2)
			throw new DataFormatException("At least 2 columns expected.");
		
		// read data name and property names
		
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

	protected void readGroupMappings(
			CSVParser groupsParser,
			Map<String, Integer> itemIndexMap, 
			Map<String, SortedSet<Integer>> groupItemsMap) throws IOException, DataFormatException {
	
		String[] rowData;
		
		while ((rowData = groupsParser.getLine()) != null) {
			if (rowData.length < 2)
				throw new DataFormatException(
						"At least 2 columns expected at " 
						+ groupsParser.getLineNumber() 
						+ "(item name and group name).");
			
			String itemName = rowData[0];
			String groupName = rowData[1];
			
			Integer itemIndex = itemIndexMap.get(itemName);
			if (itemIndex != null) {
				SortedSet<Integer> itemIndices = groupItemsMap.get(groupName);
				if (itemIndices == null) {
					itemIndices = new TreeSet<Integer>();
					groupItemsMap.put(groupName, itemIndices);
				}
				itemIndices.add(itemIndex);
			}
		}
	}
	
	protected void loadData(
			CSVParser dataParser, 
			List<String> dataPropNames, 
			List<Integer> dataPropIndices, 
			String[] propNames, 
			int[] itemDataRow, 
			String[] itemNames, 
			DoubleMatrix2D data) throws IOException {
		
		String[] rowData;
		int row = 0;
		
		final int numProps = dataPropNames.size();
		
		while ((rowData = dataParser.getLine()) != null) {
			int rowidx = itemDataRow[row];
			if (rowidx >= 0) {
				itemNames[rowidx] = rowData[0];
				int col = 0;
				while (col < numProps && col < rowData.length - 1) {
					int colidx = dataPropIndices.get(col);
					double value = Double.NaN;
					try {
						value = Double.parseDouble(rowData[col + 1]);
					}
					catch (NumberFormatException e) {}
					data.setQuick(rowidx, colidx, value);
					col++;
				}
			}
			row++;
		}
		
		// get property names

		for (int i = 0; i < numProps; i++)
			propNames[dataPropIndices.get(i)] = dataPropNames.get(i);
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
	
	public String[] getPropNames() {
		return propNames;
	}
	
	public String[] getItemNames() {
		return itemNames;
	}
	
	public DoubleMatrix2D getData() {
		return data;
	}
	
	public String[] getGroupNames() {
		return groupNames;
	}
	
	public int[][] getGroupItemIndices() {
		return groupItemIndices;
	}
}
