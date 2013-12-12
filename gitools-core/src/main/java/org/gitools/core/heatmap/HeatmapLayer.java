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
package org.gitools.core.heatmap;

import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.core.matrix.model.MatrixLayer;
import org.gitools.core.model.decorator.Decorator;
import org.gitools.core.model.decorator.DetailsDecoration;
import org.gitools.utils.events.EventUtils;
import org.gitools.utils.formatter.DetailsBoxFormatter;
import org.gitools.utils.formatter.HeatmapTextFormatter;
import org.gitools.utils.formatter.ITextFormatter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapLayer extends MatrixLayer implements IMatrixLayer {
    public static final String PROPERTY_DECORATOR = "decorator";

    private transient ITextFormatter shortFormatter;
    private transient ITextFormatter longFormatter;

    private Decorator decorator;

    public HeatmapLayer() {
        super();

        // JAXB requirement
    }

    public HeatmapLayer(String id, Class<?> valueClass, Decorator decorator) {
        super(id, valueClass);

        this.decorator = decorator;
    }

    public Decorator getDecorator() {
        return decorator;
    }

    public void setDecorator(Decorator decorator) {
        Decorator oldValue = this.decorator;
        this.decorator = decorator;

        firePropertyChange(PROPERTY_DECORATOR, oldValue, decorator);
        EventUtils.moveListeners(oldValue, decorator);

    }

    @XmlTransient
    public ITextFormatter getShortFormatter() {

        if (shortFormatter == null) {
            return HeatmapTextFormatter.INSTANCE;
        }

        return shortFormatter;
    }

    public void setShortFormatter(ITextFormatter shortFormatter) {
        this.shortFormatter = shortFormatter;
    }

    @XmlTransient
    public ITextFormatter getLongFormatter() {

        if (longFormatter == null) {
            return DetailsBoxFormatter.INSTANCE;
        }
        return longFormatter;
    }

    public void setLongFormatter(ITextFormatter longFormatter) {
        this.longFormatter = longFormatter;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void populateDetails(List<DetailsDecoration> details, IMatrix matrix, String row, String column, int layerIndex, boolean isSelected) {
        DetailsDecoration decoration = new DetailsDecoration(getName(), getDescription(), getDescriptionUrl(), null, getValueUrl());
        decoration.setIndex(layerIndex);
        decoration.setSelectable(true);
        if (isSelected) {
            decoration.setSelected(true);
        }
        if (row != null && column != null) {
            boolean previousShowValue = getDecorator().isShowValue();
            getDecorator().setShowValue(true);
            getDecorator().decorate(decoration, getLongFormatter(), matrix, matrix.getLayers().get(layerIndex), row, column);
            getDecorator().setShowValue(previousShowValue);
        }
        details.add(decoration);
    }
}
