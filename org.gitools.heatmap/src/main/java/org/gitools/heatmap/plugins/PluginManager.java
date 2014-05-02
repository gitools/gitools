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
