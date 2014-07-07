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
import org.gitools.api.plugins.IPlugin;
import org.gitools.resource.SemanticVersion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractPlugin extends Model implements Serializable, IPlugin {

    @XmlTransient
    private static final String PLUGIN_ENABLED = "plugin_active";

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String version;


    public AbstractPlugin(String id) {
        this.id = id;
        this.version = getVersion();
    }


    public AbstractPlugin() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isEnabled() {
        //TODO incorporate in Settings
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        //TODO incorporate in Settings
        //boolean old = enabled;
        //this.enabled = enabled;
        //firePropertyChange(PLUGIN_ENABLED, enabled, enabled);
        throw new RuntimeException("Method to be implemented");
    }

    @Override
    public boolean isCompatibleVersion(String version) {

        SemanticVersion v = new SemanticVersion(version);
        SemanticVersion old = new SemanticVersion(getOldestCompatibleVersion());

        return !old.isNewerThan(v);

    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public Class getPluginClass() {
        return this.getClass();
    }

    @Override
    public IPlugin createNewInstance() {
        try {
            return this.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
