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


import org.gitools.api.plugins.IPlugin;
import org.gitools.api.plugins.IPluginManager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PluginManager implements IPluginManager {


    @Inject
    @Any
    private Instance<IPlugin> pluginIterator;
    private List<IPlugin> plugins;

    public PluginManager() {
    }

    @PostConstruct
    public void init() {
        plugins = new ArrayList<>();
        for (IPlugin plugin : pluginIterator) {
            plugins.add(plugin);
        }
    }

    @Override
    public List<IPlugin> getPlugins() {
        return plugins;
    }
}
