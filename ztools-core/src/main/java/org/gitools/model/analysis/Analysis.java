package org.gitools.model.analysis;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.Artifact;
import org.gitools.model.ModuleMap;
import org.gitools.model.ToolConfig;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;
import org.gitools.model.xml.adapter.ModuleMapXmlAdapter;

@XmlSeeAlso({EnrichmentAnalysis.class})
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType( propOrder={"startTime", "elapsedTime", "dataTableRef", "results" } )
public class Analysis extends Artifact {

	private static final long serialVersionUID = 44219853082656184L;

	protected Date startTime;	
	protected long elapsedTime;
	
	/** Original matrix, before ToolConfig applied **/
	
	
	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	protected DoubleMatrix dataTable;
	
	@XmlJavaTypeAdapter(ModuleMapXmlAdapter.class)
	protected ModuleMap moduleMap;
	
	
	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	protected ObjectMatrix resultsMatrix;

	@XmlTransient
	protected ToolConfig toolConfig;

	public Analysis() {
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public ToolConfig getToolConfig() {
		return toolConfig;
	}

	public void setToolConfig(ToolConfig toolConfig) {
		this.toolConfig = toolConfig;
	}

	public DoubleMatrix getDataTable() {
		return dataTable;
	}

	public void setDataTable(DoubleMatrix dataTable) {
		this.dataTable = dataTable;
	}

	public String getDataTableRef() {
		return dataTable.getName();
	}

	public ModuleMap getModuleMap() {
		return moduleMap;
	}

	public void setModuleSet(ModuleMap moduleMap) {
		this.moduleMap = moduleMap;
	}

	public String getModuleMapRef() {
		return moduleMap.getTitle();
	}

	public ObjectMatrix getResults() {
		return resultsMatrix;
	}

	public void setResults(ObjectMatrix resultsMatrix) {
		this.resultsMatrix = resultsMatrix;
	}
}
