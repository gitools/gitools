/*
 * #%L
 * org.gitools.resource
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
package org.gitools.resource;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class SemanticVersion {

    @XmlTransient
    public static final String OLD_VERSION = "oldVersion";


    @XmlTransient
    boolean initialized = false;

    @XmlAttribute
    private String semantic;

    @XmlTransient
    private int major;

    @XmlTransient
    private int minor;

    @XmlTransient
    private int bug;

    @XmlTransient
    private String special = "";

    public SemanticVersion() {
    }

    public SemanticVersion(String version) {

        semantic = version;

        init();

    }

    private void init() {
        String version = semantic;

        initialized = true;

        if (version.equals(OLD_VERSION)) {
            return;
        }

        //SNAPSHOT or RC
        if (version.contains("-")) {
            int index = version.indexOf("-");
            special = version.substring(index + 1);
            version = version.substring(0, index);
        }

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

    public boolean isNewerThan(SemanticVersion other) {

        if (this.semantic.equals(OLD_VERSION)) {
            return false;
        }

        if (!initialized) {
            init();
        }

        if (this.major > other.getMajor()) {
            // this is a newer major release
            return true;

        } else if (this.major < other.getMajor()) {
            //other is newer
            return false;

        } else if (this.minor > other.getMinor()) {
            //this is newer minor release
            return true;

        } else if (this.minor < other.getMinor()) {
            //other is newer minor release
            return false;

        } else if (this.bug > other.getBug()) {
            //newer bug release
            return true;

        } else if (this.bug < other.getBug()) {
            //other is newer bug release
            return false;

        } else if (this.special.equals("") && !other.getSpecial().equals("")) {
            //user using development version - official release available
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return semantic;
    }

    public String getSpecial() {
        return special;
    }

}
