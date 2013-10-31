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
import java.util.*;

@XmlAccessorType(XmlAccessType.NONE)
public class ArrayModuleMap extends Resource implements IModuleMap {

    private static final long serialVersionUID = 6463084331984782264L;

    private String[] moduleNames;
    private String[] moduleDescriptions;
    private String[] itemNames;

    private int[][] itemIndices;
    private final Map<String, Integer> moduleIndexMap = new HashMap<>();

    public ArrayModuleMap(String[] moduleNames, String[] itemNames, int[][] itemIndices) {
        this.moduleNames = moduleNames;
        this.moduleDescriptions = new String[moduleNames.length];
        this.itemNames = itemNames;
        this.itemIndices = itemIndices;
    }

    public ArrayModuleMap(Map<String, Set<String>> map, Map<String, String> desc, Map<String, Set<String>> tree) {

        int modCount = map.keySet().size();

        String[] mname = map.keySet().toArray(new String[modCount]);
        String[] mdesc = new String[modCount];
        int[][] mapIndices = new int[modCount][];

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

        }

        String[] inames = new String[itemMap.keySet().size()];
        for (Map.Entry<String, Integer> entry : itemMap.entrySet())
            inames[entry.getValue()] = entry.getKey();

        setModuleNames(mname);
        this.moduleDescriptions = mdesc;
        setItemNames(inames);
        this.itemIndices = mapIndices;
    }

    @Override
    public String[] getModuleNames() {
        return moduleNames;
    }

    @Override
    public Collection<String> getModules() {
        return Arrays.asList(moduleNames);
    }

    private void setModuleNames(String[] moduleNames) {
        this.moduleNames = moduleNames;

        moduleIndexMap.clear();
        for (int i = 0; i < moduleNames.length; i++)
            moduleIndexMap.put(moduleNames[i], i);

        this.itemIndices = new int[moduleNames.length][];
    }

    @Override
    public int getModuleCount() {
        return moduleNames.length;
    }

    @Override
    public String getModuleName(int index) {
        return moduleNames[index];
    }

    @Override
    public String getModuleDescription(int index) {
        return moduleDescriptions[index];
    }

    @Override
    public String[] getItemNames() {
        return itemNames;
    }

    @Override
    public Collection<String> getItems() {
        return Arrays.asList(itemNames);
    }

    @Override
    public Collection<String> getMappingItems(String module) {

        int[] mappingIndices = getItemIndices(module);

        List<String> mappingItems = new ArrayList<>(mappingIndices.length);
        for (int itemIndex : mappingIndices) {
            mappingItems.add(itemNames[itemIndex]);
        }

        return mappingItems;
    }

    private void setItemNames(String[] itemNames) {
        this.itemNames = itemNames;
    }

    @Override
    public String getItemName(int index) {
        return itemNames[index];
    }

    @Override
    public int[] getItemIndices(int moduleIndex) {
        return itemIndices[moduleIndex];
    }

    private void setAllItemIndices(int[][] itemIndices) {
        this.itemIndices = itemIndices;
    }


    @Override
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
