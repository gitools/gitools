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

import org.gitools.heatmap.HeatmapDim;

import java.awt.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.gitools.matrix.model.AnnotationMatrix;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapTextLabelsHeader extends HeatmapHeader {

	private static final long serialVersionUID = -2580139666999968074L;
	
	public static final String LABEL_PATTERN_CHANGED = "labelPatternChanged";
	public static final String LINK_NAME_CHANGED = "linkNameChanged";
	public static final String LINK_PATTERN_CHANGED = "linkPatternChanged";

	public enum LabelSource {
		ID, ANNOTATION, PATTERN
	}

	protected LabelSource labelSource;
	protected String labelAnnotation;
	protected String labelPattern;
	
	@Deprecated protected String linkName;
	@Deprecated protected String linkPattern;

	public HeatmapTextLabelsHeader() {
		this(null);
	}

	public HeatmapTextLabelsHeader(HeatmapDim hdim) {
		super(hdim);

		size = 80;
		labelColor = Color.BLACK;
		backgroundColor = Color.WHITE;
		font = new Font(Font.MONOSPACED, Font.PLAIN, 9);
		AnnotationMatrix am = hdim != null ? hdim.getAnnotations() : null;
		if (am != null && am.getColumnCount() > 0) {
			labelSource = LabelSource.ANNOTATION;
			labelAnnotation = am.getColumnLabel(0);
		}
		else {
			labelSource = LabelSource.ID;
			labelAnnotation = "";
		}
		
		labelPattern = "${id}";
		linkName = "Google";
		linkPattern = "http://www.google.com/search?q=${url:id}";
	}

	@Override
	public String getTitle() {
		StringBuilder sb = new StringBuilder();
		sb.append("Text: ");
		switch (labelSource) {
			case ID: sb.append("ID"); break;
			case ANNOTATION: sb.append(labelAnnotation); break;
			case PATTERN: sb.append(labelPattern); break;
		}
		return sb.toString();
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		Font old = this.font;
		this.font = font;
		firePropertyChange(LABEL_FONT_CHANGED, old, font);
	}

    @Override
    protected void updateLargestLabelLength(Component component) {
        this.largestLabelLength = 0;
    }

    public LabelSource getLabelSource() {
		return labelSource;
	}

	public void setLabelSource(LabelSource labelSource) {
		this.labelSource = labelSource;
	}

	public String getLabelAnnotation() {
		return labelAnnotation;
	}

	public void setLabelAnnotation(String labelAnnotation) {
		this.labelAnnotation = labelAnnotation;
	}

	public String getLabelPattern() {
		return labelPattern;
	}

	public void setLabelPattern(String pattern) {
		String old = this.labelPattern;
		this.labelPattern = pattern;
		firePropertyChange(LABEL_PATTERN_CHANGED, old, pattern);
	}

	@Deprecated
	public String getLinkName() {
		return linkName;
	}

	@Deprecated
	public void setLinkName(String linkName) {
		String old = this.linkName;
		this.linkName = linkName;
		firePropertyChange(LINK_NAME_CHANGED, old, linkName);
	}

	@Deprecated
	public String getLinkPattern() {
		return linkPattern;
	}

	@Deprecated
	public void setLinkPattern(String linkPattern) {
		String old = this.linkPattern;
		this.linkPattern = linkPattern;
		firePropertyChange(LINK_PATTERN_CHANGED, old, linkPattern);
	}
}
