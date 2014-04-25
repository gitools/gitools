package org.gitools.heatmap.plugin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SelectionPropertiesPlugin extends AbstractPlugin implements IBoxPlugin {


    public SelectionPropertiesPlugin(String name) {
        super(name);
    }

    public SelectionPropertiesPlugin() {
    }

    @Override
    public PluginAccess getPluginAccess() {
        return IBoxPlugin.ACCESSES;
    }


}
