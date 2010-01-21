package org.gitools.model;

import org.gitools.model.enrichment.EnrichmentAnalysis;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;
import org.gitools.model.xml.adapter.ModuleMapXmlAdapter;

@XmlSeeAlso({EnrichmentAnalysis.class})
@XmlAccessorType(XmlAccessType.FIELD)
public class Analysis extends Artifact {

	private static final long serialVersionUID = 44219853082656184L;

	protected Date startTime;	
	protected long elapsedTime;

	@Deprecated
	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	protected DoubleMatrix dataTable;
	
	@Deprecated
	@XmlJavaTypeAdapter(ModuleMapXmlAdapter.class)
	protected ModuleMap moduleMap;
	
	@Deprecated
	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	protected ObjectMatrix resultsMatrix;

	@Deprecated
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

	@Deprecated
	public ToolConfig getToolConfig() {
		return toolConfig;
	}

	@Deprecated
	public void setToolConfig(ToolConfig toolConfig) {
		this.toolConfig = toolConfig;
	}

	@Deprecated
	public DoubleMatrix getDataTable() {
		return dataTable;
	}

	@Deprecated
	public void setDataTable(DoubleMatrix dataTable) {
		this.dataTable = dataTable;
	}

	@Deprecated
	public String getDataTableRef() {
		return dataTable.getName();
	}

	@Deprecated
	public ModuleMap getModuleMap() {
		return moduleMap;
	}

	@Deprecated
	public void setModuleMap(ModuleMap moduleMap) {
		this.moduleMap = moduleMap;
	}

	@Deprecated
	public String getModuleMapRef() {
		return moduleMap.getTitle();
	}

	@Deprecated
	public ObjectMatrix getResultsMatrix() {
		return resultsMatrix;
	}

	@Deprecated
	public void setResultsMatrix(ObjectMatrix resultsMatrix) {
		this.resultsMatrix = resultsMatrix;
	}
}
