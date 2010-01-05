package org.gitools.model;

import java.util.Properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.ModuleMap;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;
import org.gitools.model.xml.adapter.ModuleMapXmlAdapter;

@XmlRootElement(name = "enrichmentAnalysis")
@XmlAccessorType(XmlAccessType.FIELD)
public class EnrichmentAnalysis extends Analysis {

	private static final long serialVersionUID = -7476200948081644842L;
	
	/** Data filtering cutoff **/
	protected double cutoff;
	
	/** Data filtering comparison **/
	protected String comparator;

	/** Data */
	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	protected DoubleMatrix dataTable;
	
	/** Modules */
	@XmlJavaTypeAdapter(ModuleMapXmlAdapter.class)
	protected ModuleMap moduleMap;

	/** Test name */
	protected String testName;
	
	/** Test configuration */
	protected Properties testConfig;
	
	/** Results */
	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	protected ObjectMatrix resultsMatrix;
	
	public EnrichmentAnalysis() {
	}

	public double getCutoff() {
		return cutoff;
	}

	public void setCutoff(double cutoff) {
		this.cutoff = cutoff;
	}

	public String getComparator() {
		return comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	public DoubleMatrix getDataTable() {
		return dataTable;
	}

	public void setDataTable(DoubleMatrix dataTable) {
		this.dataTable = dataTable;
	}

	public ModuleMap getModuleMap() {
		return moduleMap;
	}

	public void setModuleMap(ModuleMap moduleMap) {
		this.moduleMap = moduleMap;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Properties getTestConfig() {
		return testConfig;
	}

	public void setTestConfig(Properties testConfig) {
		this.testConfig = testConfig;
	}

	public ObjectMatrix getResultsMatrix() {
		return resultsMatrix;
	}

	public void setResultsMatrix(ObjectMatrix resultsMatrix) {
		this.resultsMatrix = resultsMatrix;
	}
}
