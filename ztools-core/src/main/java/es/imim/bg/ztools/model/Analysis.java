package es.imim.bg.ztools.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(
		propOrder = {
				"name", 
				"startTime", 
				"elapsedTime",
				"toolConfig",
				"dataTableRef", 
				"moduleMapRef",
				"results"})
				
public class Analysis extends ComposedArtifact{

	

	protected String name;
	protected Date startTime;
	protected long elapsedTime;
	
	protected DataMatrix dataTable;
	protected ModuleMap moduleMap;
	protected ResultsMatrix resultsMatrix;
	
	protected ToolConfig toolConfig;
	
	public Analysis(){
		
	}
	
	public Analysis(String id, String artifactType) {
		super(id, artifactType);
	}
	
	public Analysis(String id, String artifactType, String title) {
		super(id, artifactType, title);
	}
	
	public Analysis(String id, String artifactType, String title, String description) {
		super(id, artifactType, title, description);
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
	public DataMatrix getDataTable() {
		return dataTable;
	}
	
	public void setDataTable(DataMatrix dataTable) {
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
	public ResultsMatrix getResults() {
		return resultsMatrix;
	}
	
	public void setResults(ResultsMatrix resultsMatrix) {
		this.resultsMatrix = resultsMatrix;
	}
}
