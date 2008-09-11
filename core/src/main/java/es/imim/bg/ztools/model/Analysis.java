package es.imim.bg.ztools.model;

import java.util.Date;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectMatrix2D;
import es.imim.bg.ztools.test.factory.TestFactory;

public abstract class Analysis {

	protected String name;
	protected Date startTime;
	protected long elapsedTime;
	
	protected String[] condNames;
	protected String[] itemNames;
	
	protected DoubleMatrix2D data;
	
	protected String[] groupNames;
	protected int[][] groupItemIndices;
	
	protected TestFactory testFactory;
	
	protected String[] resultNames;
	protected ObjectMatrix2D results;
	
	public String getName() {
		return name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
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
	
	public TestFactory getTestFactory() {
		return testFactory;
	}
	
	public String[] getResultNames() {
		return resultNames;
	}
	
	public ObjectMatrix2D getResults() {
		return results;
	}
	
}
