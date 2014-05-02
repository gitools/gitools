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
import org.gitools.api.ApplicationContext;
import org.gitools.api.plugins.IPlugin;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(AbstractPlugin.class)
public class Plugins extends Model {


    @XmlTransient
    private static final String PROPERTY_CONTENTS = "pluginscontent";

    //@XmlElement(name = "plugin")
    //@XmlAnyElement
    //@XmlElementWrapper not neccesary
    @XmlElementRef()
    private List<AbstractPlugin> plugins;

    @XmlTransient
    private Map<String, Integer> nameMap;

    public Plugins() {
        this.plugins = new ArrayList<>();
        for (IPlugin plugin : ApplicationContext.getPluginManger().getPlugins()) {
            if (plugin.isEnabled()) {
                register(plugin.createNewInstance());
            }
        }
    }

    public List<AbstractPlugin> getPlugins() {
        return plugins;
    }

    public <T extends IPlugin> List<T> filter(Class<T> pluginClass) {
        List<T> filtered = new ArrayList<>();
        for (IPlugin p : this.plugins) {
            if (pluginClass.isAssignableFrom(p.getPluginClass())) {
                filtered.add((T) p);
            }
        }
        return filtered;
    }

    public void register(IPlugin plugin) {
        for (IPlugin existing : plugins) {
            if (existing.getName().equals(plugin.getName())) {
                plugins.remove(existing);
                break;
            }
        }
        plugins.add((AbstractPlugin) plugin);
        Collections.sort(plugins, new Comparator<IPlugin>() {
            @Override
            public int compare(IPlugin o1, IPlugin o2) {
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
        for (IPlugin p : plugins) {
            nameMap.put(p.getName(), counter++);
        }
    }

    public IPlugin get(String name) {
        if (nameMap == null) {
            updateMap();
        }
        if (!nameMap.containsKey(name)) {
            return null;
        }
        return plugins.get(nameMap.get(name));
    }
}
