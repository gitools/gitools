/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.analysis._DEPRECATED.model.decorator.impl;

import org.gitools.analysis._DEPRECATED.model.decorator.Decorator;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.analysis._DEPRECATED.model.decorator.Decoration;
import org.gitools.utils.colorscale.impl.BinaryColorScale;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.formatter.ITextFormatter;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;
import org.gitools.utils.xml.adapter.CutoffCmpXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "binary")
public class BinaryDecorator extends Decorator<BinaryColorScale> {
    public static final String PROPERTY_COMPARATOR = "comparator";
    public static final String PROPERTY_CUTOFF = "cutoff";
    public static final String PROPERTY_COLOR = "color";
    public static final String PROPERTY_NON_SIGNIFICANT_COLOR = "nonSignificantColor";
    public static final String PROPERTY_EMPTY_COLOR = "emptyColor";

    @XmlTransient
    private BinaryColorScale scale;

    public BinaryDecorator() {
        super();

        this.scale = new BinaryColorScale();
    }


    public BinaryColorScale getScale() {
        return scale;
    }

    public void setScale(BinaryColorScale scale) {
        this.scale = scale;
    }

    @XmlElement(name = "comparator")
    @XmlJavaTypeAdapter(CutoffCmpXmlAdapter.class)
    public CutoffCmp getComparator() {
        return CutoffCmp.getFromName(getScale().getComparator());
    }

    public void setComparator(CutoffCmp cutoffCmp) {
        CutoffCmp old = getComparator();
        getScale().setComparator(cutoffCmp.getShortName());
        firePropertyChange(PROPERTY_COMPARATOR, old, cutoffCmp);
    }

    @XmlElement(name = "cutoff")
    public double getCutoff() {
        return getScale().getCutoff();
    }

    public void setCutoff(double cutoff) {
        double old = getScale().getCutoff();
        getScale().setCutoff(cutoff);
        firePropertyChange(PROPERTY_CUTOFF, old, cutoff);
    }

    @XmlElement(name = "true-color")
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    public Color getColor() {
        return getScale().getMaxColor();
    }

    public void setColor(Color color) {
        Color old = getScale().getMaxColor();
        getScale().setMaxColor(color);
        firePropertyChange(PROPERTY_COLOR, old, color);
    }

    @XmlElement(name = "false-color")
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    public Color getNonSignificantColor() {
        return getScale().getMinColor();
    }

    public void setNonSignificantColor(Color color) {
        Color old = getScale().getMinColor();
        this.getScale().setMinColor(color);
        firePropertyChange(PROPERTY_NON_SIGNIFICANT_COLOR, old, color);
    }

    @XmlElement(name = "empty-color")
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    public Color getEmptyColor() {
        return getScale().getEmptyColor();
    }

    public void setEmptyColor(Color color) {
        Color old = getScale().getEmptyColor();
        getScale().setEmptyColor(color);
        firePropertyChange(PROPERTY_EMPTY_COLOR, old, color);
    }

    @Override
    public void decorate(Decoration decoration, ITextFormatter textFormatter, IMatrix matrix, IMatrixLayer layer, String... identifiers) {

        Object value = matrix.get(layer, identifiers);
        double v = toDouble(value);

        if (Double.isNaN(v)) {
            decoration.setBgColor(getScale().getEmptyColor());
            return;
        }

        final Color c = getScale().valueColor(v);

        decoration.setBgColor(c);
        if (isShowValue()) {
            decoration.setValue(textFormatter.format(value));
        }

    }

}
