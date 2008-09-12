package es.imim.bg.ztools.zcalc.input;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import cern.colt.matrix.DoubleMatrix2D;
import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.resources.DataFile;
import es.imim.bg.ztools.resources.ModulesFile;

public class FileZCalcInput implements ZCalcInput {
	
	// Defaults
	
	public static final int defaultMinGroupSize = 20;
	public static final int defaultMaxGroupSize = Integer.MAX_VALUE;
	
	// Input 
	
	protected String dataFileName;
	protected String groupsFileName;
	
	protected int minGroupSize = defaultMinGroupSize;
	protected int maxGroupSize = defaultMaxGroupSize;
	
	// Output
	
	protected String[] condNames;
	protected String[] itemNames;
	
	protected String dataName;
	protected DoubleMatrix2D data;
	
	protected String[] groupNames;
	protected int[][] groupItemIndices;
	
	public FileZCalcInput(
			String dataFile,
			String groupsFile,
			int minGroupSize, 
			int maxGroupSize) {
		
		this.dataFileName = dataFile;
		this.groupsFileName = groupsFile;
		this.minGroupSize = minGroupSize;
		this.maxGroupSize = maxGroupSize;
	}
	
	public void load(ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, DataFormatException {

		// Load metadata
		
		DataFile dataFile = new DataFile(dataFileName);
		dataFile.loadMetadata(monitor);
		
		// Load modules
		
		ModulesFile modules = new ModulesFile(groupsFileName);
		modules.load(
				minGroupSize,
				maxGroupSize,
				dataFile.getRowNames(),
				monitor);
		
		groupNames = modules.getModuleNames();
		groupItemIndices = modules.getModuleItemIndices();
		
		// Load data
		
		dataFile.loadData( 
				null, 
				modules.getItemsOrder(), 
				monitor);
		
		condNames = dataFile.getColumnNames();
		itemNames = dataFile.getRowNames();
		data = dataFile.getData();
	}
	
	public String[] getCondNames() {
		return condNames;
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
