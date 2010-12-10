/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

public class ModuleMapFactory {

	/** Create a module map
	 *
	 * @param itemNames item names
	 * @param moduleItemsMap map between module names and item names indices
	 * @param includeNonMappedItems maintain background without changes
	 * @param minModuleSize minimim module size
	 * @param maxModuleSize maximum module size
	 * @param itemsOrder Order in which rows should be loaded, it should have the same size as itemNames
	 * @return module map
	 */
	public static ModuleMap create(
			String[] itemNames,
			Map<String, SortedSet<Integer>> moduleItemsMap,
			boolean includeNonMappedItems,
			int minModuleSize, int maxModuleSize,
			int[] itemsOrder) {

		// create a map between the item names and its row position

		Map<String, Integer> itemNameToRowMapping = new TreeMap<String, Integer>();
		for (int i = 0; i < itemNames.length; i++)
			itemNameToRowMapping.put(itemNames[i], i);

		// itemHasMapping[idx] = true means that itemNames[idx] has references from at least one module
		final boolean[] itemHasMappings = new boolean[itemNames.length];

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
			@Override
			public int compare(Integer o1, Integer o2) {
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

		numModules = end - start;
		String[] moduleNames = new String[numModules];
		int[][] moduleItemIndices = new int[numModules][];

		// Prepare map between original item index
		// and data row where will be stored,
		// sorted according to group size.
		// Get group names and update group item indices.

		//int[] itemsOrder = new int[itemNames.length];
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
				orderedItemNames[itemsOrder[i]] = itemNames[i];

		ModuleMap mmap = new ModuleMap();

		mmap.setModuleNames(moduleNames);
		mmap.setItemNames(orderedItemNames);
		mmap.setAllItemIndices(moduleItemIndices);

		return mmap;
	}
}
