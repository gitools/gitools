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

package org.gitools.heatmap.header;

import edu.upf.bg.color.generator.ColorGenerator;
import edu.upf.bg.color.generator.ColorGeneratorFactory;
import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import edu.upf.bg.xml.adapter.FontXmlAdapter;
import org.gitools.clustering.ClusteringResults;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HeatmapDataHeatmapHeader extends HeatmapHeader {

	public static final String THICKNESS_CHANGED = "thickness";
	public static final String MARGIN_CHANGED = "margin";
	public static final String LABEL_VISIBLE_CHANGED = "labelVisible";
	public static final String LABEL_FONT_CHANGED = "labelFont";
	public static final String LABEL_ROTATED_CHANGED = "labelRotated";
    private static final String HEADER_HEATMAP_CHANGED = "headerHeatmap";


    /* The thickness of the color band */
	protected int thickness;

	/* Color band margin */
	protected int margin;

	/** Whether to show labels of each cluster */
	protected boolean labelVisible;

	/** The font to use for labels */
	@XmlJavaTypeAdapter(FontXmlAdapter.class)
	protected Font font;

	/** If false the label is painted along the color band,
	 * otherwise the label is perpendicular to the color band */
	protected boolean labelRotated;

	/** Label foreground color */
	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color labelColor;

    private Heatmap headerHeatmap;

    public HeatmapDataHeatmapHeader(HeatmapDim hdim) {
		super(hdim);
		
		size = 20;
		thickness = 14;
		margin = 1;

		labelVisible = false;
		font = new Font(Font.MONOSPACED, Font.PLAIN, 9);
		labelRotated = false;
		labelColor = Color.BLACK;

	}

	/* The thickness of the color band */
	public int getThickness() {
		return thickness;
	}

	/* The thickness of the color band */
	public void setThickness(int thickness) {
		int old = this.thickness;
		this.thickness = thickness;
		firePropertyChange(THICKNESS_CHANGED, old, thickness);
	}

	/* Color band margin */
	public int getMargin() {
		return margin;
	}

	/* Color band margin */
	public void setMargin(int margin) {
		int old = this.margin;
		this.margin = margin;
		firePropertyChange(MARGIN_CHANGED, old, margin);
	}

	/** Whether to show labels of each value */
	public void setLabelVisible(boolean labelVisible) {
		boolean old = this.labelVisible;
		this.labelVisible = labelVisible;
		firePropertyChange(LABEL_VISIBLE_CHANGED, old, labelVisible);
	}

	/** The font to use for labels */
	public Font getLabelFont() {
		return font;
	}

	/** The font to use for labels */
	public void setLabelFont(Font font) {
		Font old = this.font;
		this.font = font;
		firePropertyChange(LABEL_FONT_CHANGED, old, font);
	}

	/** If false the label is painted along the color band,
	 * otherwise the label is perpendicular to the color band */
	public boolean isLabelRotated() {
		return labelRotated;
	}

	/** If false the label is painted along the color band,
	 * otherwise the label is perpendicular to the color band */
	public void setLabelRotated(boolean labelRotated) {
		boolean old = this.labelRotated;
		this.labelRotated = labelRotated;
		firePropertyChange(LABEL_ROTATED_CHANGED, old, labelRotated);
	}

    public void setHeaderHeatmap (Heatmap headerHeatmap) {
        Heatmap old = this.headerHeatmap;
        this.headerHeatmap = headerHeatmap;
        firePropertyChange(HEADER_HEATMAP_CHANGED,old,headerHeatmap);
    }
    
    public Heatmap getHeaderHeatmap () {
        return this.headerHeatmap;
    }

    @Override
    public String getTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("Data: ");
        sb.append(headerHeatmap.getTitle());
        return sb.toString();
    }

}
