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

import edu.upf.bg.xml.adapter.FontXmlAdapter;
import org.gitools.heatmap.HeatmapDim;
import edu.upf.bg.xml.adapter.ColorXmlAdapter;

import java.awt.*;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.model.AbstractModel;

public abstract class HeatmapHeader extends AbstractModel {

	public static final String TITLE_CHANGED = "title";
	public static final String SIZE_CHANGED = "size";
	public static final String VISIBLE_CHANGED = "visible";
	public static final String BG_COLOR_CHANGED = "bgColor";
    public static final String MARGIN_CHANGED = "margin";
    public static final String LABEL_VISIBLE_CHANGED = "labelVisible";
    public static final String LABEL_FONT_CHANGED = "labelFont";
    public static final String LABEL_ROTATED_CHANGED = "labelRotated";
    public static final String LABEL_COLOR_CHANGED = "labelColor";
    public static final String LARGEST_LABEL_LENGTH_CHANGED = "largestLabelLength";

    /** The title of the cluster set */
	protected String title;

	/** The height/width of the color band */
	protected int size;

	/** Wether the cluster set is visible */
	protected boolean visible;

	/* Background color*/
	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color backgroundColor;

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

    /** Tells the drawer how long the largest label is with current
     * font settings
     */
    
    protected int largestLabelLength;

    /** Label foreground color */
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    protected Color labelColor;

    public HeatmapHeader(HeatmapDim dim) {
		this.dim = dim;
		this.title = "";
		this.size = 100;
		this.visible = true;
		this.backgroundColor = Color.WHITE;
        font = new Font(Font.MONOSPACED, Font.PLAIN, 9);
        margin = 1;
        labelRotated = false;
        labelVisible = false;
        labelColor = Color.BLACK;
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

    public boolean isLabelVisible() {
        return this.labelVisible;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    /** The font to use for labels */
    public Font getLabelFont() {
        return font;
    }

    /** The font to use for labels */
    public void setLabelFont(Font font) {
        Font old = this.font;
        this.font = font;
        updateLargestLabelLength(null);
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

    /** Label foreground color */
    public Color getLabelColor() {
        return labelColor;
    }

    /** Label foreground color */
    public void setLabelColor(Color labelColor) {
        Color old = this.labelColor;
        this.labelColor = labelColor;
        firePropertyChange(LABEL_COLOR_CHANGED, old, labelColor);
    }

    public int getLargestLabelLength() {
        return largestLabelLength;
    }

    public void setLargestLabelLength(int largestLabelLength) {
        int old = this.largestLabelLength;
        this.largestLabelLength = largestLabelLength;
        firePropertyChange(LARGEST_LABEL_LENGTH_CHANGED, old, largestLabelLength);
    }

    abstract protected void updateLargestLabelLength(Component component);

}
