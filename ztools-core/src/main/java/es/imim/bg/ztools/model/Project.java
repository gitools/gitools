package es.imim.bg.ztools.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(
		propOrder = {
				"summary", 
				"notes", 
				"dataTables", 
				"moduleMaps", 
				"analysis"})

@XmlRootElement
public class Project {
	
	protected String summary;
	protected String notes;
	
	protected List<DataMatrix> dataTables = new ArrayList<DataMatrix>();
	protected List<ModuleMap> moduleMaps = new ArrayList<ModuleMap>();
	protected List<Analysis> analysis = new ArrayList<Analysis>();
	

	public Project() {
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String sumary) {
		this.summary = sumary;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@XmlElement(name = "DataTable")
	public List<DataMatrix> getDataTables() {
		return dataTables;
	}
	
	public void setDataTables(List<DataMatrix> dataMatrices) {
		this.dataTables = dataMatrices;
	}
	
	@XmlElement(name = "ModuleMap")
	public List<ModuleMap> getModuleMaps() {
		return moduleMaps;
	}
	
	public void setModuleMaps(List<ModuleMap> moduleMaps) {
		this.moduleMaps = moduleMaps;
	}
	
	@XmlElement(name = "Analysis")
	public List<Analysis> getAnalysis() {
		return analysis;
	}
	
	public void setAnalysis(List<Analysis> analysis) {
		this.analysis = analysis;
	}	
}
