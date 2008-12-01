package es.imim.bg.ztools.model;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(
		propOrder = {
				"name",
				"description"/*,
				"moduleNames", 
				"itemNames", 
				"itemIndices",
				"itemsOrder"*/})
				
public class ModuleSet {

	protected String name;
	protected String description;
	
	protected String[] moduleNames;
	protected String[] itemNames;
	
	protected int[][] itemIndices;
	
	protected int[] itemsOrder;

	public ModuleSet() {
	}
	
	public ModuleSet(
			String[] moduleNames, String[] itemNames, int[][] itemIndices, int[] itemsOrder) {
		
		this.moduleNames = moduleNames;
		this.itemNames = itemNames;
		this.itemIndices = itemIndices;
		this.itemsOrder = itemsOrder;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@XmlTransient
	public final String[] getModuleNames() {
		return moduleNames;
	}

	public final void setModuleNames(String[] moduleNames) {
		this.moduleNames = moduleNames;
	}

	@XmlTransient
	public final String[] getItemNames() {
		return itemNames;
	}

	public final void setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
	}

	@XmlTransient
	public final int[][] getItemIndices() {
		return itemIndices;
	}

	public final void setItemIndices(int[][] itemIndices) {
		this.itemIndices = itemIndices;
	}
	
	@XmlTransient
	public int[] getItemsOrder() {
		return itemsOrder;
	}
	
	public void setItemsOrder(int[] itemsOrder) {
		this.itemsOrder = itemsOrder;
	}
}
