package org.gitools.model.REVIEW;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import org.gitools.model.ResourceContainer;
import org.gitools.model.ModuleMap;
import org.gitools.model.ToolConfig;

@Deprecated // use Analysis and its children (EnrichmentAnalysis, ...)
public class AnalysisArt extends ResourceContainer {

	protected String name;
	protected Date startTime;
	protected long elapsedTime;

	protected TableArtifact dataTable;
	protected ModuleMap moduleMap;
	protected TableArtifact resultsMatrix;

	protected ToolConfig toolConfig;

	public AnalysisArt() {

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

	public TableArtifact getDataTable() {
		return dataTable;
	}

	public void setDataTable(TableArtifact dataTable) {
		this.dataTable = dataTable;
	}

	public TableArtifact getResultsMatrix() {
		return resultsMatrix;
	}

	public void setResultsMatrix(TableArtifact resultsMatrix) {
		this.resultsMatrix = resultsMatrix;
	}

	public ModuleMap getModuleMap() {
		return moduleMap;
	}

	public void setModuleMap(ModuleMap moduleMap) {
		this.moduleMap = moduleMap;

	}
}
