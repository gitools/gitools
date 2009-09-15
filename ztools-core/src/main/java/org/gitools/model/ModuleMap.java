package org.gitools.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import cern.colt.bitvector.BitMatrix;

@XmlAccessorType(XmlAccessType.NONE)	
public class ModuleMap extends Artifact {

	private static final long serialVersionUID = 6463084331984782264L;

	protected String[] moduleNames;
	protected String[] itemNames;

	protected int[][] itemIndices;

	protected int[] itemsOrder;
	
	//protected Map<String, Integer> moduleIndexMap;
	//protected Map<String, Integer> itemIndexMap;
	//protected BitMatrix map;

	public ModuleMap() {
	}

	/*public ModuleMap(int numModules, int numItems) {
		moduleNames = new String[numModules];
		Arrays.fill(moduleNames, "");
		
		itemNames = new String[numItems];
		Arrays.fill(itemNames, "");
		
		map = new BitMatrix(numModules, numItems);
	}*/
	
	/*public ModuleMap(String[] moduleNames, String[] itemNames) {
		this.moduleNames = moduleNames;		
		this.itemNames = itemNames;
		
		map = new BitMatrix(moduleNames.length, itemNames.length);
	}*/
	
	@Deprecated
	public ModuleMap(String[] moduleNames, String[] itemNames,
			int[][] itemIndices, int[] itemsOrder) {

		this.moduleNames = moduleNames;
		this.itemNames = itemNames;
		this.itemIndices = itemIndices;
		this.itemsOrder = itemsOrder;
	}
	
	public String[] getModuleNames() {
		return moduleNames;
	}
	
	public void setModuleNames(String[] moduleNames) {
		this.moduleNames = moduleNames;
	}
	
	public int getModuleCount() {
		return moduleNames.length;
	}
	
	public String getModuleName(int index) {
		return moduleNames[index];
	}
	
	public void setModuleName(int index, String name) {
		moduleNames[index] = name;
	}
	
	public String[] getItemNames() {
		return itemNames;
	}
	
	public void setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
	}
	
	public int getItemCount() {
		return itemNames.length;
	}
	
	public String getItemName(int index) {
		return itemNames[index];
	}
	
	public void setItemName(int index, String name) {
		itemNames[index] = name;
	}
	
	public int[] getItemIndices(int moduleIndex) {
		return itemIndices[moduleIndex];
	}
	
	public void setItemIndices(int moduleIndex, int[] indices) {
		itemIndices[moduleIndex] = indices;
	}
	
	public final int[][] getItemIndices() {
		return itemIndices;
	}

	public final void setItemIndices(int[][] itemIndices) {
		this.itemIndices = itemIndices;
	}

	public BitMatrix getMap() {
		int mc = getModuleCount();
		int ic = getItemCount();
		
		BitMatrix map = new BitMatrix(mc, ic);
		
		map.xor(map);
		for (int mi = 0; mi < mc; mi++) {
			int[] indices = itemIndices[mi];
			int c = indices.length;
			for (int ii = 0; ii < c; ii++)
				map.putQuick(mi, indices[ii], true);
		}
		
		return map;
	}
	
	@Deprecated
	public int[] getItemsOrder() {
		return itemsOrder;
	}

	@Deprecated
	public void setItemsOrder(int[] itemsOrder) {
		this.itemsOrder = itemsOrder;
	}
}
