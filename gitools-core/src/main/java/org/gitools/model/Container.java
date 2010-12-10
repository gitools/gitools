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

package org.gitools.model;

import org.gitools._DEPRECATED.model.ResourceReference;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Container extends Artifact {

	private static final long serialVersionUID = 9028366098418461333L;

	protected boolean limitedExploration;

	@XmlElementWrapper(name = "references")
	@XmlElement(name = "reference")
	protected List<ResourceReference> references = new ArrayList<ResourceReference>(0);

	public Container() {
	}

	public List<ResourceReference> getReferences() {
		return references;
	}

	public void setReferences(List<ResourceReference> references) {
		this.references = references;
	}

	public boolean isLimitedExploration() {
		return limitedExploration;
	}

	public void setLimitedExploration(boolean limitedExploration) {
		this.limitedExploration = limitedExploration;
	}

}
