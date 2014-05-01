/*
 * #%L
 * org.gitools.heatmap
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.heatmap.plugins;


import com.jgoodies.binding.beans.Model;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Dependent
public class Plugins extends Model {

    @XmlTransient
    @Inject
    @Any
    private Instance<IPlugin> pluginIterator;

    @XmlTransient
    private static final String PROPERTY_CONTENTS = "pluginscontent";

    @XmlElement(name = "plugin")
    private List<AbstractPlugin> plugins;

    @XmlTransient
    private Map<String, Integer> nameMap;

    public Plugins() {
        this.plugins = new ArrayList<>();
    }

    @PostConstruct
    public void init() {

        for (IPlugin plugin : pluginIterator) {
            if (plugin instanceof AbstractPlugin)
                register((AbstractPlugin) plugin);
        }
    }

    public void register(AbstractPlugin plugin) {
        for (AbstractPlugin existing : plugins) {
            if (existing.getName().equals(plugin.getName())) {
                plugins.remove(existing);
                break;
            }
        }
        plugins.add(plugin);
        Collections.sort(plugins, new Comparator<AbstractPlugin>() {
            @Override
            public int compare(AbstractPlugin o1, AbstractPlugin o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });
        firePropertyChange(PROPERTY_CONTENTS, null, this);
        updateMap();

    }

    private void updateMap() {
        if (nameMap == null) {
            nameMap = new HashMap<>();
        }
        nameMap.clear();
        Integer counter = 0;
        for (AbstractPlugin p : plugins) {
            nameMap.put(p.getName(), counter++);
        }
    }

    public AbstractPlugin get(String name) {
        if (nameMap == null) {
            updateMap();
        }
        if (!nameMap.containsKey(name)) {
            return null;
        }
        return plugins.get(nameMap.get(name));
    }

    public List<AbstractPlugin> getAll() {
        return plugins;
    }

    private boolean nameOccupied(String name) {
        for (AbstractPlugin p : plugins) {
            if (p.getName().toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


    public <T extends IPlugin> List<T> filter(Class<T> pluginClass) {
        List<T> filtered = new ArrayList<>();
        for (AbstractPlugin p : this.plugins) {
            if (pluginClass.isAssignableFrom(p.getClass())) {
                filtered.add((T) p);
            }
        }
        return filtered;
    }
}
