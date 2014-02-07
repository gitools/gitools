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
package org.gitools.matrix.modulemap;


import org.gitools.api.modulemap.IModuleMap;
import org.gitools.resource.Resource;

import java.util.*;

public class HashModuleMap extends Resource implements IModuleMap {

    private Set<String> modules;
    private Set<String> items;
    private Map<String, Set<String>> map;

    public HashModuleMap() {
        this.modules = new HashSet<>();
        this.items = new HashSet<>();
        this.map = new HashMap<>();
    }

    @Override
    public Set<String> getModules() {
        return modules;
    }

    @Override
    public Set<String> getItems() {
        return items;
    }

    @Override
    public Set<String> getMappingItems(String module) {
        return map.get(module);
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
                this.map.put(module, new HashSet<String>());
            }

            if (!this.map.get(module).contains(item)) {
                this.map.get(module).add(item);
            }
        }

        return this;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(modules.size()).append(" modules, ");
        sb.append(items.size()).append(" items");
        return sb.toString();
    }
}
