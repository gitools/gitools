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

package edu.upf.bg.tools;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.upf.bg.tools.ToolDescriptor;

@XmlRootElement(name="tools")
public class ToolSet {
	
	protected List<ToolDescriptor> toolDescriptors =
		new ArrayList<ToolDescriptor>();
	
	public ToolSet() {
	}
	
	@XmlElement(name="tool")
	public List<ToolDescriptor> getToolDescriptors() {
		return toolDescriptors;
	}
	
	public void setToolDescriptors(List<ToolDescriptor> toolDescriptors) {
		this.toolDescriptors = toolDescriptors;
	}
	
	public void add(ToolSet toolSet) {
		toolDescriptors.addAll(toolSet.getToolDescriptors());
	}
	
	public void add(ToolDescriptor desc) {
		toolDescriptors.add(desc);
	}
}
