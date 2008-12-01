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
public class Investigation {
	
	protected String summary;
	protected String notes;
	
	protected List<DataMatrix> dataMatrices = new ArrayList<DataMatrix>();
	protected List<ModuleSet> moduleSets = new ArrayList<ModuleSet>();
	protected List<Analysis> analysis = new ArrayList<Analysis>();
	//protected List<Figure> figures;

	public Investigation() {
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
	public List<ModuleSet> getModuleSets() {
		return moduleSets;
	}
	
	public void setModuleSets(List<ModuleSet> moduleSets) {
		this.moduleSets = moduleSets;
	}
	
	public List<Analysis> getAnalysis() {
		return analysis;
	}
	
	public void setAnalysis(List<Analysis> analysis) {
		this.analysis = analysis;
	}	
}
