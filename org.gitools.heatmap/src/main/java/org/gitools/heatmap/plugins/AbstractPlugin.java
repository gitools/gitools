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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractPlugin extends Model implements Serializable, IPlugin {

    private static final String PLUGIN_ACTIVE = "plugin_active";

    @XmlElement
    private String name;

    @XmlElement
    private String version;

    @XmlElement
    private boolean active = true;

    public AbstractPlugin(String name) {
        this.name = name;
        this.name = getVersion();
    }


    public AbstractPlugin() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        boolean old = active;
        this.active = active;
        firePropertyChange(PLUGIN_ACTIVE, old, active);

    }

    @Override
    public boolean isCompatibleVersion(String version) {

        SemanticVersion v = new SemanticVersion(version);
        SemanticVersion old = new SemanticVersion(getOldestCompatibleVersion());

        return !old.isNewerThan(v);

    }

    @Override
    public String toString() {
        return name;
    }

    private class SemanticVersion {

        int major;
        int minor;
        int bug;

        public SemanticVersion(String version) {
            String[] parts = version.split("\\.");
            major = Integer.valueOf(parts[0]);
            minor = Integer.valueOf(parts[1]);
            bug = Integer.valueOf(parts[2]);
        }

        public int getMajor() {
            return major;
        }

        public int getMinor() {
            return minor;
        }

        public int getBug() {
            return bug;
        }

        public boolean isNewerThan(SemanticVersion otherVersion) {

            if (this.major > otherVersion.getMajor()) {
                return true;
            } else if (this.minor > otherVersion.getMinor()) {
                return true;
            } else if (this.bug > otherVersion.getBug()) {
                return true;
            }

            return false;
        }
    }
}
