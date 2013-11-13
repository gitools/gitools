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


import java.util.*;

public class HashModuleMap extends Resource implements IModuleMap {

    private List<String> modules;
    private List<String> items;
    private Map<String, List<String>> map;

    private transient Map<String, int[]> mapIndices;

    public HashModuleMap() {
        this.modules = new ArrayList<>();
        this.items = new ArrayList<>();
        this.map = new HashMap<>();
    }

    @Override
    public Collection<String> getModules() {
        return modules;
    }

    @Override
    public Collection<String> getItems() {
        return items;
    }

    @Override
    public Collection<String> getMappingItems(String module) {
        return map.get(module);
    }

    @Override
    public int[] getItemIndices(String modName) {

        int[] result = getMapIndices().get(modName);

        if (result == null) {
            return new int[0];
        }

        return result;
    }

    public HashModuleMap addMapping(String module, String... items) {
        return addMapping(module, Arrays.asList(items));
    }

    public HashModuleMap addMapping(String module, Iterable<String> items) {

        if (!this.modules.contains(module)) {
            this.modules.add(module);
        }

        for (String item : items) {
            if (!this.items.contains(item)) {
                this.items.add(item);
            }

            if (!this.map.containsKey(module)) {
                this.map.put(module, new ArrayList<String>());
            }

            if (!this.map.get(module).contains(item)) {
                this.map.get(module).add(item);
            }
        }

        this.mapIndices = null;

        return this;
    }

    private Map<String, int[]> getMapIndices() {

        if (mapIndices == null) {
            mapIndices = new HashMap<>();

            for (String module : modules) {

                List<String> mappings = map.get(module);
                int[] mappingIndices = new int[mappings.size()];

                for (int i=0; i < mappings.size(); i++) {
                    mappingIndices[i] = items.indexOf(mappings.get(i));
                }

                mapIndices.put(module, mappingIndices);
            }
        }

        return mapIndices;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(modules.size()).append(" modules, ");
        sb.append(items.size()).append(" items");
        return sb.toString();
    }
}
