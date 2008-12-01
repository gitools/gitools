package es.imim.bg.ztools.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(
		propOrder = {
				"name", 
				"startTime", 
				"elapsedTime",
				"toolConfig"
				/*"dataMatrix", 
				"moduleSet"*/,
				"results"})
				
public class Analysis {

	protected String name;
	protected Date startTime;
	protected long elapsedTime;
	
	protected DataMatrix dataMatrix;
	protected ModuleSet moduleSet;
	protected ResultsMatrix resultsMatrix;
	
	protected ToolConfig toolConfig;
	
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
	
	@XmlTransient
	public DataMatrix getDataMatrix() {
		return dataMatrix;
	}
	
	public void setDataMatrix(DataMatrix dataMatrix) {
		this.dataMatrix = dataMatrix;
	}
	
	@XmlTransient
	public ModuleSet getModuleSet() {
		return moduleSet;
	}
	
	public void setModuleSet(ModuleSet moduleSet) {
		this.moduleSet = moduleSet;
	}
	
	public ResultsMatrix getResults() {
		return resultsMatrix;
	}
	
	public void setResults(ResultsMatrix resultsMatrix) {
		this.resultsMatrix = resultsMatrix;
	}
	
	public ToolConfig getToolConfig() {
		return toolConfig;
	}
	
	public void setToolConfig(ToolConfig toolConfig) {
		this.toolConfig = toolConfig;
	}
}
