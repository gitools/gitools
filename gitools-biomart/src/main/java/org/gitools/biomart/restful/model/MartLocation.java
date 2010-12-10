/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.biomart.restful.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "MartURLLocation")
@XmlAccessorType(XmlAccessType.FIELD)
public class MartLocation {

	@XmlAttribute
	protected String database;

	@XmlAttribute(name = "default")
	protected int isdefault;

	@XmlAttribute
	protected String displayName;

	@XmlAttribute
	protected String host;

	@XmlAttribute
	protected String includeDatasets;

	@XmlAttribute
	protected String martUser;

	@XmlAttribute
	protected String name;

	@XmlAttribute
	protected String path;

	@XmlAttribute
	protected String port;

	@XmlAttribute
	protected String serverVirtualSchema;

	@XmlAttribute
	protected int visible;

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

	public MartLocation(String database, int isdefault,
			String displayName, String host,
			String includeDatasets, String martUser,
			String name, String path, String port,
			String serverVirtualSchema, int visible) {

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
