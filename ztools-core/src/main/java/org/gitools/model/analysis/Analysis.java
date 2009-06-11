package org.gitools.model.analysis;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.gitools.model.Artifact;
import org.gitools.model.ModuleMap;
import org.gitools.model.ToolConfig;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.model.matrix.ObjectMatrix;

/*@XmlType(propOrder = { "name", "startTime", "elapsedTime", "toolConfig",
		"dataTableRef", "moduleMapRef", "results" })*/

public class Analysis extends Artifact {

	private static final long serialVersionUID = 44219853082656184L;

	protected String name;
	protected Date startTime;
	protected long elapsedTime;
	
	protected DoubleMatrix dataTable;
	protected ModuleMap moduleMap;
	protected ObjectMatrix resultsMatrix;

	protected ToolConfig toolConfig;

	public Analysis() {

	}

	public Analysis(String id, String artifactType) {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@XmlTransient
	public DoubleMatrix getDataTable() {
		return dataTable;
	}

	public void setDataTable(DoubleMatrix dataTable) {
		this.dataTable = dataTable;
	}

	@XmlElement(name = "dataTableRef")
	public String getDataTableRef() {
		return dataTable.getName();
	}

	@XmlTransient
	public ModuleMap getModuleMap() {
		return moduleMap;
	}

	public void setModuleSet(ModuleMap moduleMap) {
		this.moduleMap = moduleMap;
	}

	@XmlElement(name = "moduleMapRef")
	public String getModuleMapRef() {
		return moduleMap.getName();
	}

	@XmlElement(name = "Results")
	public ObjectMatrix getResults() {
		return resultsMatrix;
	}

	public void setResults(ObjectMatrix resultsMatrix) {
		this.resultsMatrix = resultsMatrix;
	}
}
