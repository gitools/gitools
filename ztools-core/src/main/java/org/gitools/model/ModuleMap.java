package org.gitools.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ModuleMap extends Artifact {

	private static final long serialVersionUID = 6463084331984782264L;

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
