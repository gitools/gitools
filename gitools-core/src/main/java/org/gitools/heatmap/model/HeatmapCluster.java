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

package org.gitools.heatmap.model;

import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import java.awt.Color;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class HeatmapCluster {

	protected String name;
	
	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color color;

	public Color getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setName(String name) {
		this.name = name;
	}


}
