package org.gitools.heatmap.plugin;

public class MutualExclusivePlugin extends AbstractPlugin implements IBoxPlugin {


    public MutualExclusivePlugin(String name) {
        super(name);

    }

    @Override
    public PluginAccess getPluginAccess() {
        return IBoxPlugin.ACCESSES;
    }


}
