package org.gitools.model;

import java.util.Set;
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

	protected String organism;
	protected String moduleCategory;
	protected String itemCategory;

	protected String[] moduleNames;
	protected String[] moduleDescriptions;
	protected String[] itemNames;

	protected int[][] itemIndices;

	protected Map<String, Integer> moduleIndexMap = new HashMap<String, Integer>();
	protected Map<String, Integer> itemIndexMap = new HashMap<String, Integer>();
	
	public ModuleMap() {
		this.moduleNames = new String[0];
		this.moduleDescriptions = new String[0];
		this.itemNames = new String[0];
		this.itemIndices = new int[0][];
	}

	//@Deprecated
	public ModuleMap(String[] moduleNames, String[] itemNames,
			int[][] itemIndices/*, int[] itemsOrder*/) {

		setModuleNames(moduleNames);
		setItemNames(itemNames);

		this.itemIndices = itemIndices;
		//this.itemsOrder = itemsOrder;
	}

	public ModuleMap(String moduleName, String[] itemNames) {
		setModuleNames(new String[] { moduleName });
		setItemNames(itemNames);
		int[] indices = new int[itemNames.length];
		for (int i = 0; i < indices.length; i++)
			indices[i] = i;
		setAllItemIndices(new int[][] { indices });
	}

	public ModuleMap(Map<String, Set<String>> map) {
		this(map, new HashMap<String, String>());
	}

	public ModuleMap(Map<String, Set<String>> map, Map<String, String> desc) {
		int modCount = map.keySet().size();

		String[] mname = map.keySet().toArray(new String[modCount]);
		String[] mdesc = new String[modCount];
		int[][] indices = new int[modCount][];

		Map<String, Integer> itemMap = new HashMap<String, Integer>();

		int i = 0;
		for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
			mname[i] = entry.getKey();
			mdesc[i] = desc.get(mname[i]);
			if (mdesc[i] == null)
				mdesc[i] = "";

			int[] ii = new int[entry.getValue().size()];
			int j = 0;
			for (String iname : entry.getValue()) {
				Integer idx = itemMap.get(iname);
				if (idx == null) {
					idx = itemMap.size();
					itemMap.put(iname, itemMap.size());
				}
				ii[j++] = idx;
			}
			indices[i] = ii;
			i++;
		}

		String[] inames = new String[itemMap.keySet().size()];
		for (Map.Entry<String, Integer> entry : itemMap.entrySet())
			inames[entry.getValue()] = entry.getKey();

		setModuleNames(mname);
		setModuleDescriptions(mdesc);
		setItemNames(inames);
		this.itemIndices = indices;
	}

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public String getModuleCategory() {
		return moduleCategory;
	}

	public void setModuleCategory(String moduleCategory) {
		this.moduleCategory = moduleCategory;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String[] getModuleNames() {
		return moduleNames;
	}
	
	public final void setModuleNames(String[] moduleNames) {
		this.moduleNames = moduleNames;

		moduleIndexMap.clear();
		for (int i = 0; i < moduleNames.length; i++)
			moduleIndexMap.put(moduleNames[i], i);

		this.itemIndices = new int[moduleNames.length][];
	}

	public String[] getModuleDescriptions() {
		return moduleDescriptions;
	}

	public final void setModuleDescriptions(String[] descriptions) {
		this.moduleDescriptions = descriptions;
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

	public String getModuleDescription(int index) {
		return moduleDescriptions[index];
	}

	public void setModuleDescription(int index, String desc) {
		moduleDescriptions[index] = desc;
	}

	public int getModuleItemCount(int index) {
		return itemIndices[index].length;
	}
	
	public String[] getItemNames() {
		return itemNames;
	}
	
	public final void setItemNames(String[] itemNames) {
		this.itemNames = itemNames;

		itemIndexMap.clear();
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

	public final ModuleMap remap(String[] names) {
		return remap(names, 1, Integer.MAX_VALUE);
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

	public int getModuleIndex(String modName) {
		Integer modIndex = moduleIndexMap.get(modName);
		if (modIndex == null)
			return -1;
		else
			return modIndex;
	}

	public int[] getItemIndices(String modName) {
		Integer modIndex = moduleIndexMap.get(modName);
		if (modIndex == null)
			return null;

		return itemIndices[modIndex];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int numMods = moduleNames != null ? moduleNames.length : 0;
		sb.append(numMods).append(" modules, ");
		int numItems = itemNames != null ? itemNames.length : 0;
		sb.append(numItems).append(" items");
		return sb.toString();
	}

	public void printItemCount() {
		for (int i = 0; i < getModuleCount(); i++) {
			System.out.print(getModuleName(i) + "\t");
			System.out.println(getModuleItemCount(i));
		}
	}
}
