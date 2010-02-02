package org.gitools.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import cern.colt.bitvector.BitMatrix;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

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

	/** Filters out modules with less items than minModuleSize
	 * and with more items than maxModulesSize considering only
	 * the item names in itemNames.
	 *
	 * It will return the items indices that should be considered for the background.
	 */
	public int[] filter(
			int minModuleSize,
			int maxModuleSize,
			String[] bgItemNames,
			boolean includeNonMappedItems) {

		// create a map between the item names and its row position

		Map<String, Integer> itemNameToRowMapping = new TreeMap<String, Integer>();
		for (int i = 0; i < bgItemNames.length; i++)
			itemNameToRowMapping.put(bgItemNames[i], i);

		// create a map between module name and set of item indices
		final Map<String, SortedSet<Integer>> moduleItemsMap =
			new HashMap<String, SortedSet<Integer>>();

		for (int mi = 0; mi < moduleNames.length; mi++) {
			SortedSet<Integer> indices = new TreeSet<Integer>();
			moduleItemsMap.put(moduleNames[mi], indices);
			for (int ii = 0; ii < itemNames.length; ii++) {
				Integer index = itemNameToRowMapping.get(itemNames[ii]);
				if (index != null)
					indices.add(index);
			}
		}

		// itemHasMapping[idx] = true means that itemNames[idx] has references from at least one module
		final boolean[] itemHasMappings = new boolean[bgItemNames.length];

		// copy module names and module item indices to arrays

		final Set<Map.Entry<String, SortedSet<Integer>>> entries = moduleItemsMap.entrySet();
		final String[] tmpModuleNames = new String[entries.size()];
		final int[][] tmpModuleItemIndices = new int[entries.size()][];
		int index = 0;

		for (Map.Entry<String, SortedSet<Integer>> entry : entries) {
			SortedSet<Integer> indices = entry.getValue();
			tmpModuleNames[index] = entry.getKey();
			int[] ia = tmpModuleItemIndices[index] = new int[indices.size()];
			int i = 0;
			for (Integer idx : indices ) {
				ia[i++] = idx;
				itemHasMappings[idx] = true;
			}
			index++;
		}

		// sort groups by number of items

		int numModules = entries.size();
		final Integer[] moduleOrder = new Integer[numModules];
		for (int i = 0; i < moduleOrder.length; i++)
			moduleOrder[i] = i;

		Arrays.sort(moduleOrder, new Comparator<Integer>() {
			@Override public int compare(Integer o1, Integer o2) {
				int l1 = tmpModuleItemIndices[o1].length;
				int l2 = tmpModuleItemIndices[o2].length;
				return l2 - l1;
			}
		});

		// filter by number of items

		int start = 0;
		int end = numModules - 1;
		while (start < numModules
				&& tmpModuleItemIndices[moduleOrder[start]].length > maxModuleSize) {
			int order = moduleOrder[start];
			tmpModuleNames[order] = null;
			tmpModuleItemIndices[order] = null;
			start++;
		}
		while (end >= start
				&& tmpModuleItemIndices[moduleOrder[end]].length < minModuleSize) {
			int order = moduleOrder[end];
			tmpModuleNames[order] = null;
			tmpModuleItemIndices[order] = null;
			end--;
		}
		end++;

		final int fileNumModules = numModules;

		numModules = end - start;
		String[] moduleNames = new String[numModules];
		int[][] moduleItemIndices = new int[numModules][];

		// Prepare map between original item index
		// and data row where will be stored,
		// sorted according to group size.
		// Get group names and update group item indices.

		int [] itemsOrder = new int[bgItemNames.length];
		Arrays.fill(itemsOrder, -1);

		int numItems = 0;
		for (int i = start; i < end; i++) {
			int order = moduleOrder[i];

			moduleNames[i - start] = tmpModuleNames[order];
			moduleItemIndices[i - start] = tmpModuleItemIndices[order];

			final int[] indices = tmpModuleItemIndices[order];

			for (int j = 0; j < indices.length; j++) {
				int idx = indices[j];
				if (itemsOrder[idx] < 0)
					itemsOrder[idx] = numItems++;
				indices[j] = itemsOrder[idx];
			}
		}

		// Put the rest of the items at the end
		for (int i = 0; i < itemsOrder.length; i++)
			if (itemsOrder[i] < 0
					&& (includeNonMappedItems || itemHasMappings[i]))
				itemsOrder[i] = numItems++;

		// Create ordered list of item names

		final String[] orderedItemNames = new String[numItems];
		for (int i = 0; i < itemsOrder.length; i++)
			if (itemsOrder[i] >= 0)
				orderedItemNames[itemsOrder[i]] = bgItemNames[i];

		setModuleNames(moduleNames);
		setItemNames(orderedItemNames);
		//moduleMap.setNumMappedItems(numItems);
		setItemIndices(moduleItemIndices);
		
		return itemsOrder;
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
