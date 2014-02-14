/*
 * #%L
 * gitools-biomart
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.datasources.biomart.restful.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "MartURLLocation")
@XmlAccessorType(XmlAccessType.FIELD)
public class MartLocation {

    @XmlAttribute
    private String database;

    @XmlAttribute(name = "default")
    private int isdefault;

    @XmlAttribute
    private String displayName;

    @XmlAttribute
    private String host;

    @XmlAttribute
    private String includeDatasets;

    @XmlAttribute
    private String martUser;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String path;

    @XmlAttribute
    private String port;

    @XmlAttribute
    private String serverVirtualSchema;

    @XmlAttribute
    private int visible;

    public MartLocation() {
    }

    public MartLocation(Mart mart) {
        this.database = mart.getDatabase();
        this.isdefault = mart.getDefault();
        this.displayName = mart.getDisplayName();
        this.host = mart.getHost();
        this.includeDatasets = mart.getIncludeDatasets();
        this.martUser = mart.getMartUser();
        this.name = mart.getName();
        this.path = mart.getPath();
        this.port = mart.getPort();
        this.serverVirtualSchema = mart.getServerVirtualSchema();
        this.visible = mart.getVisible();
    }

    public MartLocation(String database, int isdefault, String displayName, String host, String includeDatasets, String martUser, String name, String path, String port, String serverVirtualSchema, int visible) {

        this.database = database;
        this.isdefault = isdefault;
        this.displayName = displayName;
        this.host = host;
        this.includeDatasets = includeDatasets;
        this.martUser = martUser;
        this.name = name;
        this.path = path;
        this.port = port;
        this.serverVirtualSchema = serverVirtualSchema;
        this.visible = visible;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIncludeDatasets() {
        return includeDatasets;
    }

    public void setIncludeDatasets(String includeDatasets) {
        this.includeDatasets = includeDatasets;
    }

    public int getDefault() {
        return isdefault;
    }

    public void setDefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public String getMartUser() {
        return martUser;
    }

    public void setMartUser(String martUser) {
        this.martUser = martUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServerVirtualSchema() {
        return serverVirtualSchema;
    }

    public void setServerVirtualSchema(String serverVirtualSchema) {
        this.serverVirtualSchema = serverVirtualSchema;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
}
