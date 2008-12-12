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
				"dataMatrices", 
				"moduleSets", 
				"analysis"})

@XmlRootElement
public class Project {
	
	protected String summary;
	protected String notes;
	
	protected List<DataMatrix> dataMatrices = new ArrayList<DataMatrix>();
	protected List<ModuleMap> moduleMaps = new ArrayList<ModuleMap>();
	protected List<Analysis> analysis = new ArrayList<Analysis>();
	//protected List<Figure> figures;

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
	
	@XmlElement(name = "data")
	public List<DataMatrix> getDataMatrices() {
		return dataMatrices;
	}
	
	public void setDataMatrices(List<DataMatrix> dataMatrices) {
		this.dataMatrices = dataMatrices;
	}
	
	@XmlElement(name = "modules")
	public List<ModuleMap> getModuleSets() {
		return moduleMaps;
	}
	
	public void setModuleSets(List<ModuleMap> moduleMaps) {
		this.moduleMaps = moduleMaps;
	}
	
	public List<Analysis> getAnalysis() {
		return analysis;
	}
	
	public void setAnalysis(List<Analysis> analysis) {
		this.analysis = analysis;
	}	
}
