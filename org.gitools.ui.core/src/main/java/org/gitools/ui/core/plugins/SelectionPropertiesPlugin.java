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
package org.gitools.ui.core.plugins;

import org.gitools.api.plugins.PluginAccess;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.plugins.AbstractPlugin;
import org.gitools.ui.core.components.boxes.Box;
import org.gitools.ui.core.components.boxes.SelectionBox;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@ApplicationScoped
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "selection-properties")
public class SelectionPropertiesPlugin extends AbstractPlugin implements IBoxPlugin {

    //TODO module for plugin
    @XmlTransient
    public static final String NAME = "selection-properties";

    public SelectionPropertiesPlugin() {
        super(NAME);
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getOldestCompatibleVersion() {
        return "1.0.0";
    }

    @Override
    public PluginAccess getPluginAccess() {
        return ACCESSES;
    }


    @Override
    public Box[] getBoxes(Heatmap heatmap) {
        return new Box[]{new SelectionBox("Selection details", null, heatmap)};
    }
}
