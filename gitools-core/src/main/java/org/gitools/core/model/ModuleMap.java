/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@XmlAccessorType(XmlAccessType.NONE)
public class ModuleMap extends Resource {

    private static final long serialVersionUID = 6463084331984782264L;

    private String[] moduleNames;
    private String[] moduleDescriptions;
    private String[] itemNames;

    private int[][] itemIndices;

    private int[][] moduleTreeIndices;

    private final Map<String, Integer> moduleIndexMap = new HashMap<>();

    public ModuleMap() {
        this.moduleNames = new String[0];
        this.moduleDescriptions = new String[0];
        this.itemNames = new String[0];
        this.itemIndices = new int[0][];
        this.moduleTreeIndices = new int[0][];
    }

    public ModuleMap(String[] moduleNames, String[] itemNames) {

    }

    public ModuleMap(String moduleName, String[] itemNames) {
        setModuleNames(new String[]{moduleName});
        setItemNames(itemNames);
        int[] indices = new int[itemNames.length];
        for (int i = 0; i < indices.length; i++)
            indices[i] = i;
        setAllItemIndices(new int[][]{indices});
    }

    public ModuleMap(Map<String, Set<String>> map) {
        this(map, new HashMap<String, String>());
    }

    public ModuleMap(Map<String, Set<String>> map, Map<String, String> desc) {
        this(map, desc, new HashMap<String, Set<String>>());
    }

    public ModuleMap(Map<String, Set<String>> map, Map<String, String> desc, Map<String, Set<String>> tree) {

        int modCount = map.keySet().size();

        String[] mname = map.keySet().toArray(new String[modCount]);
        String[] mdesc = new String[modCount];
        int[][] mapIndices = new int[modCount][];
        int[][] treeIndices = new int[modCount][];

        Map<String, Integer> itemMap = new HashMap<>();
        Map<String, Integer> modMap = new HashMap<>();

        int i = 0;
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            mname[i] = entry.getKey();
            modMap.put(mname[i], i);

            mdesc[i] = desc.get(mname[i]);
            if (mdesc[i] == null) {
                mdesc[i] = "";
            }

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
            mapIndices[i] = ii;
            i++;
        }

        final Set<String> emptyChildren = new HashSet<>(0);

        for (int j = 0; j < modCount; j++) {
            Set<String> children = tree.get(mname[j]);
            if (children == null) {
                children = emptyChildren;
            }

            int[] mi = new int[children.size()];

            int k = 0;
            for (String child : children) {
                Integer idx = modMap.get(child);
                if (idx != null) {
                    mi[k++] = idx;
                }
            }

            if (k < mi.length) {
                int[] tmp = new int[k];
                System.arraycopy(mi, 0, tmp, 0, k);
                mi = tmp;
            }

            treeIndices[j] = mi;
        }

        String[] inames = new String[itemMap.keySet().size()];
        for (Map.Entry<String, Integer> entry : itemMap.entrySet())
            inames[entry.getValue()] = entry.getKey();

        setModuleNames(mname);
        setModuleDescriptions(mdesc);
        setItemNames(inames);
        this.itemIndices = mapIndices;
        this.moduleTreeIndices = treeIndices;
    }

    public String[] getModuleNames() {
        return moduleNames;
    }

    private final void setModuleNames(String[] moduleNames) {
        this.moduleNames = moduleNames;

        moduleIndexMap.clear();
        for (int i = 0; i < moduleNames.length; i++)
            moduleIndexMap.put(moduleNames[i], i);

        this.itemIndices = new int[moduleNames.length][];
    }

    private final void setModuleDescriptions(String[] descriptions) {
        this.moduleDescriptions = descriptions;
    }

    public int getModuleCount() {
        return moduleNames.length;
    }

    public String getModuleName(int index) {
        return moduleNames[index];
    }

    public String getModuleDescription(int index) {
        return moduleDescriptions[index];
    }

    public String[] getItemNames() {
        return itemNames;
    }

    public final void setItemNames(String[] itemNames) {
        this.itemNames = itemNames;
    }

    public String getItemName(int index) {
        return itemNames[index];
    }

    public int[] getItemIndices(int moduleIndex) {
        return itemIndices[moduleIndex];
    }

    public final int[][] getAllItemIndices() {
        return itemIndices;
    }

    public final void setAllItemIndices(int[][] itemIndices) {
        this.itemIndices = itemIndices;
    }

    private void setModuleTreeIndices(int[][] moduleTreeIndices) {
        this.moduleTreeIndices = moduleTreeIndices;
    }

    public final ModuleMap remap(String[] names) {
        return remap(names, 1, Integer.MAX_VALUE);
    }

    public final ModuleMap remap(String[] names, int minSize, int maxSize) {

        // prepare a item name to index map for input names
        Map<String, Integer> nameIndices = new HashMap<>();
        for (int i = 0; i < names.length; i++)
            nameIndices.put(names[i], i);

        // prepare new indices for item names
        int[] indexMap = new int[itemNames.length];
        for (int i = 0; i < itemNames.length; i++) {
            Integer index = nameIndices.get(itemNames[i]);
            if (index == null) {
                index = -1;
            }
            indexMap[i] = index;
        }

        // remap indices
        List<String> modNames = new ArrayList<>();
        List<int[]> modIndices = new ArrayList<>();

        int[] remapedIndices;
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
                for (int remapedIndex : remapedIndices)
                    if (remapedIndex != -1) {
                        newIndices[k++] = remapedIndex;
                    }
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
        mmap.setModuleTreeIndices(moduleTreeIndices);
        return mmap;
    }

    public int[] getItemIndices(String modName) {
        Integer modIndex = moduleIndexMap.get(modName);
        if (modIndex == null) {
            return null;
        }

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

}
