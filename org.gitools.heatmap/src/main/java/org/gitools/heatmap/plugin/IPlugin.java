package org.gitools.heatmap.plugin;


public interface IPlugin {

    public String getName();

    public boolean isActive();

    public void setActive(boolean active);

    public PluginAccess getPluginAccess();
}
