package org.gitools.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import cern.colt.bitvector.BitMatrix;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.NONE)	
public class ModuleMap extends Artifact {

	private static final long serialVersionUID = 6463084331984782264L;

	protected String[] moduleNames;
	protected String[] itemNames;

	protected int[][] itemIndices;

	protected int[] itemsOrder;
	
	protected Map<String, Integer> moduleIndexMap = new HashMap<String, Integer>();
	protected Map<String, Integer> itemIndexMap = new HashMap<String, Integer>();
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

		setModuleNames(moduleNames);
		setItemNames(itemNames);

		this.itemIndices = itemIndices;
		this.itemsOrder = itemsOrder;
	}
	
	public String[] getModuleNames() {
		return moduleNames;
	}
	
	public void setModuleNames(String[] moduleNames) {
		this.moduleNames = moduleNames;

		for (int i = 0; i < moduleNames.length; i++)
			moduleIndexMap.put(moduleNames[i], i);
	}
	
	public int getModuleCount() {
		return moduleNames.length;
	}
	
	public String getModuleName(int index) {
		return moduleNames[index];
	}
	
	public void setModuleName(int index, String name) {
		moduleNames[index] = name;

		moduleIndexMap.clear();
		for (int i = 0; i < moduleNames.length; i++)
			moduleIndexMap.put(moduleNames[i], i);
	}
	
	public String[] getItemNames() {
		return itemNames;
	}
	
	public void setItemNames(String[] itemNames) {
		this.itemNames = itemNames;

		for (int i = 0; i < itemNames.length; i++)
			itemIndexMap.put(itemNames[i], i);
	}
	
	public int getItemCount() {
		return itemNames.length;
	}
	
	public String getItemName(int index) {
		return itemNames[index];
	}
	
	public void setItemName(int index, String name) {
		itemNames[index] = name;

		itemIndexMap.clear();
		for (int i = 0; i < itemNames.length; i++)
			itemIndexMap.put(itemNames[i], i);
	}
	
	public int[] getItemIndices(int moduleIndex) {
		return itemIndices[moduleIndex];
	}
	
	public void setItemIndices(int moduleIndex, int[] indices) {
		itemIndices[moduleIndex] = indices;
	}
	
	public final int[][] getAllItemIndices() {
		return itemIndices;
	}

	public final void setAllItemIndices(int[][] itemIndices) {
		this.itemIndices = itemIndices;
	}

	public final ModuleMap remap(String[] names, int minSize, int maxSize) {

		// prepare a item name to index map for input names
		Map<String, Integer> nameIndices = new HashMap<String, Integer>();
		for (int i = 0; i < names.length; i++)
			nameIndices.put(names[i], i);

		// prepare new indices for item names
		int[] indexMap = new int[itemNames.length];
		for (int i = 0; i < itemNames.length; i++) {
			Integer index = nameIndices.get(itemNames[i]);
			if (index == null)
				index = -1;
			indexMap[i] = index;
		}

		// remap indices
		List<String> modNames = new ArrayList<String>();
		List<int[]> modIndices = new ArrayList<int[]>();

		int[] remapedIndices = null;
		for (int i = 0; i < itemIndices.length; i++) {
			int[] indices = itemIndices[i];
			remapedIndices = new int[indices.length];

			int numItems = 0;
			for (int j = 0; j < indices.length; j++) {
				int newIndex = indexMap[indices[j]];
				remapedIndices[j] = newIndex;
				numItems += newIndex >= 0 ? 1 : 0;
			}

			boolean inRange = numItems >= minSize && numItems <= maxSize;

			if (numItems != remapedIndices.length && inRange) {
				int[] newIndices = new int[numItems];
				int k = 0;
				for (int j = 0; j < remapedIndices.length; j++)
					if (remapedIndices[j] != -1)
						newIndices[k++] = remapedIndices[j];
				remapedIndices = newIndices;
			}

			if (inRange) {
				modNames.add(moduleNames[i]);
				modIndices.add(remapedIndices);
			}
		}

		ModuleMap mmap = new ModuleMap();
		mmap.setItemNames(names);
		mmap.setModuleNames(modNames.toArray(new String[modNames.size()]));
		mmap.setAllItemIndices(modIndices.toArray(new int[modIndices.size()][]));
		return mmap;
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

	/*@Deprecated
	public int[] getItemsOrder() {
		return itemsOrder;
	}

	@Deprecated
	public void setItemsOrder(int[] itemsOrder) {
		this.itemsOrder = itemsOrder;
	}*/

	public int[] getItemIndices(String modName) {
		Integer modIndex = moduleIndexMap.get(modName);
		if (modIndex == null)
			return null;

		return itemIndices[modIndex];
	}
}
