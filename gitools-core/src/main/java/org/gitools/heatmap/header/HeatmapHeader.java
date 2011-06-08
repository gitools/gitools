/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package org.gitools.heatmap.header;

import org.gitools.heatmap.HeatmapDim;
import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import java.awt.Color;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.model.AbstractModel;

public abstract class HeatmapHeader extends AbstractModel {

	public static final String TITLE_CHANGED = "title";
	public static final String SIZE_CHANGED = "size";
	public static final String VISIBLE_CHANGED = "visible";
	public static final String BG_COLOR_CHANGED = "bgColor";

	/** The title of the cluster set */
	protected String title;

	/** The height/width of the color band */
	protected int size;

	/** Wether the cluster set is visible */
	protected boolean visible;

	/* Background color*/
	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color backgroundColor;

	public HeatmapHeader(HeatmapDim dim) {
		this.dim = dim;
		this.title = "";
		this.size = 100;
		this.visible = true;
		this.backgroundColor = Color.WHITE;
	}

	@XmlTransient
	protected HeatmapDim dim;

	public HeatmapDim getHeatmapDim() {
		return dim;
	}

	public void setHeatmapDim(HeatmapDim dim) {
		this.dim = dim;
	}

	/** The title */
	public String getTitle() {
		return title;
	}

	/** The title */
	public void setTitle(String title) {
		String old = this.title;
		this.title = title;
		firePropertyChange(TITLE_CHANGED, old, title);
	}

	/** The height/width */
	public int getSize() {
		return size;
	}

	/** The height/width */
	public void setSize(int size) {
		int old = this.size;
		this.size = size;
		firePropertyChange(SIZE_CHANGED, old, size);
	}

	/** Wether the header is visible */
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		boolean old = this.visible;
		this.visible = visible;
		firePropertyChange(VISIBLE_CHANGED, old, visible);
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color color) {
		Color old = this.backgroundColor;
		this.backgroundColor = color;
		firePropertyChange(BG_COLOR_CHANGED, old, color);
	}
}
