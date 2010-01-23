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
import org.gitools.model.xml.adapter.ModuleMapXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
public class OncozAnalysis extends HtestAnalysis implements Serializable {

	/** Modules */
	@XmlJavaTypeAdapter(ModuleMapXmlAdapter.class)
	protected ModuleMap setsMap;

	/** Minimum set size */
	protected int minSetSize;

	/** Maximum set size */
	protected int maxSetSize;

	public OncozAnalysis() {
	}

	public ModuleMap getSetsMap() {
		return setsMap;
	}

	public void setSetsMap(ModuleMap setsMap) {
		this.setsMap = setsMap;
	}

	public int getMinSetSize() {
		return minSetSize;
	}

	public void setMinSetSize(int minSetSize) {
		this.minSetSize = minSetSize;
	}

	public int getMaxSetSize() {
		return maxSetSize;
	}

	public void setMaxSetSize(int maxSetSize) {
		this.maxSetSize = maxSetSize;
	}
}
