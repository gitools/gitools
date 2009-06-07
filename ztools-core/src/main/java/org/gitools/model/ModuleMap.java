package org.gitools.model;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "name", "description"/*
											 * , "moduleNames", "itemNames",
											 * "itemIndices", "itemsOrder"
											 */})
public class ModuleMap extends Artifact {

	protected String name;
	protected String description;

	protected String[] moduleNames;
	protected String[] itemNames;

	protected int[][] itemIndices;

	protected int[] itemsOrder;

	public ModuleMap() {

	}

	public ModuleMap(String id, String artifactType) {
		super(id, artifactType);
	}

	public ModuleMap(String id, String artifactType, String title) {
		super(id, artifactType, title);
	}
	
	public ModuleMap(String id, String artifactType, String title, String description) {
		super(id, artifactType, title, description);
	}
	
	public ModuleMap(String[] moduleNames,
			String[] itemNames, int[][] itemIndices, int[] itemsOrder) {

		super();
		this.moduleNames = moduleNames;
		this.itemNames = itemNames;
		this.itemIndices = itemIndices;
		this.itemsOrder = itemsOrder;
	}
	
	public ModuleMap(String id, String artifactType, String[] moduleNames,
			String[] itemNames, int[][] itemIndices, int[] itemsOrder) {

		super(id, artifactType);
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
