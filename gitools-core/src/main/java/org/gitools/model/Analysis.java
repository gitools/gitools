package org.gitools.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Analysis extends Artifact {

	private static final long serialVersionUID = 44219853082656184L;

	/** Analysis start time */
	protected Date startTime;	

	/** Analysis elapsed time in nano-seconds */
	protected long elapsedTime;

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

	/*@Deprecated
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
	}*/
}
