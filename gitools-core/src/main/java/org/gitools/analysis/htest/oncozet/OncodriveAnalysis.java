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
	protected ModuleMap columnsMap;

	/** Minimum columns size */
	protected int minColumnsSize;

	/** Maximum columns size */
	protected int maxColumnsSize;

	public OncodriveAnalysis() {
	}

	public ModuleMap getColumnsMap() {
		return columnsMap;
	}

	public void setColumnsMap(ModuleMap map) {
		this.columnsMap = map;
	}

	public int getMinColumnsSize() {
		return minColumnsSize;
	}

	public void setMinColumnsSize(int minSize) {
		this.minColumnsSize = minSize;
	}

	public int getMaxColumnsSize() {
		return maxColumnsSize;
	}

	public void setMaxColumnsSize(int maxSize) {
		this.maxColumnsSize = maxSize;
	}
}
