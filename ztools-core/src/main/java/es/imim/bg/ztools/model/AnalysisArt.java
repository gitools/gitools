package es.imim.bg.ztools.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;


@XmlType(propOrder = { "name", "startTime", "elapsedTime", "toolConfig", "dataTableRef",
	"moduleMapRef", "results" })
public class AnalysisArt extends ComposedArtifact {

    protected String name;
    protected Date startTime;
    protected long elapsedTime;

    protected TableArtifact dataTable;
    protected ModuleMap moduleMap;
    protected TableArtifact resultsMatrix;

    protected ToolConfig toolConfig;

    public AnalysisArt() {

    }

    public AnalysisArt(String id, String artifactType) {
	super(id, artifactType);
    }

    public AnalysisArt(String id, String artifactType, String title) {
	super(id, artifactType, title);
    }

    public AnalysisArt(String id, String artifactType, String title, String description) {
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
