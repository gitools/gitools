/*
 *  Copyright 2010 chris.
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

package org.gitools.analysis.htest.oncozet;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
public class OncodriveAnalysis extends HtestAnalysis implements Serializable {

	/** Modules */
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected ModuleMap moduleMap;

	/** Minimum columns size */
	protected int minModuleSize;

	/** Maximum columns size */
	protected int maxModuleSize;

	public OncodriveAnalysis() {
	}

	public ModuleMap getModuleMap() {
		return moduleMap;
	}

	public void setModuleMap(ModuleMap map) {
		this.moduleMap = map;
	}

	public int getMinModuleSize() {
		return minModuleSize;
	}

	public void setMinModuleSize(int minSize) {
		this.minModuleSize = minSize;
	}

	public int getMaxModuleSize() {
		return maxModuleSize;
	}

	public void setMaxModuleSize(int maxSize) {
		this.maxModuleSize = maxSize;
	}
}
