/*
 *  Copyright 2010 cperez.
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

package org.gitools.analysis;

import java.util.Properties;

public class AbstractMethod implements Method {

	protected String id;
	protected String name;
	protected String desc;
	protected Class<?> resultClass;
	protected Properties properties;

	public AbstractMethod(String id, String name, String desc,
			Class<?> resultClass, Properties properties) {

		this.id = id;
		this.name = name;
		this.desc = desc;
		this.resultClass = resultClass;
		this.properties = properties;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return desc;
	}

	protected void setDescription(String desc) {
		this.desc = desc;
	}

	@Override
	public Class<?> getResultClass() {
		return resultClass;
	}

	public void setResultClass(Class<?> resultClass) {
		this.resultClass = resultClass;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}


}
