package es.imim.bg.ztools.zcalc.input;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import cern.colt.matrix.DoubleMatrix2D;

import es.imim.bg.progressmonitor.ProgressMonitor;

public interface ZCalcInput {

	void load(ProgressMonitor monitor) throws FileNotFoundException,
			IOException, DataFormatException;

	String[] getCondNames();
	
	String[] getItemNames();
	
	DoubleMatrix2D getData();
	
	String[] getGroupNames();
	
	int[][] getGroupItemIndices();
}