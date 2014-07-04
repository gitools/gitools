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
package org.gitools.heatmap;

import org.gitools.api.analysis.IAggregator;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.heatmap.decorator.Decorator;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.heatmap.decorator.impl.*;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.utils.aggregation.LogSumAggregator;
import org.gitools.utils.aggregation.MeanAggregator;
import org.gitools.utils.aggregation.NonZeroCountAggregator;
import org.gitools.utils.aggregation.SumAggregator;
import org.gitools.utils.events.EventUtils;
import org.gitools.utils.formatter.HeatmapTextFormatter;
import org.gitools.utils.formatter.ITextFormatter;
import org.gitools.utils.formatter.ScientificHeatmapTextFormatter;
import org.gitools.utils.xml.adapter.FontXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapLayer extends MatrixLayer implements IMatrixLayer {

    public static final String PROPERTY_DECORATOR = "decorator";
    public static final String PROPERTY_SHORT_FORMATTER = "shortFormatter";
    public static final String PROPERTY_LONG_FORMATTER = "longFormatter";
    public static final String PROPERTY_FONT = "cellFont";

    private transient ITextFormatter shortFormatter;
    private transient ITextFormatter longFormatter;

    @XmlJavaTypeAdapter(FontXmlAdapter.class)
    protected Font font;

    private Decorator decorator;

    public HeatmapLayer() {
        super();

        if (this.font == null) {
            setFont(new Font(Font.MONOSPACED, Font.PLAIN, 9));
        }
    }

    public HeatmapLayer(String id, Class<?> valueClass, Decorator decorator) {
        super(id, valueClass);

        this.decorator = decorator;
        this.font = new Font(Font.MONOSPACED, Font.PLAIN, 9);
    }

    public HeatmapLayer(IMatrixLayer layer) {
        super(layer.getId(), layer.getValueClass(), layer.getName(), layer.getDescription());
        this.setAggregator(layer.getAggregator());
        this.setSortDirection(layer.getSortDirection());

        if (this.decorator == null) {
            this.decorator = new LinearDecorator();
        }
        this.font = new Font(Font.MONOSPACED, Font.PLAIN, 9);

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

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        Font old = this.font;
        this.font = font;
        firePropertyChange(PROPERTY_FONT, old, font);
    }

    @XmlTransient
    public ITextFormatter getShortFormatter() {

        if (shortFormatter == null) {
            return HeatmapTextFormatter.TWO_DECIMALS;
        }

        return shortFormatter;
    }

    public void setShortFormatter(ITextFormatter shortFormatter) {
        this.shortFormatter = shortFormatter;
        firePropertyChange(PROPERTY_SHORT_FORMATTER, null, shortFormatter);
    }

    @XmlTransient
    public ITextFormatter getLongFormatter() {

        if (longFormatter == null) {
            return ScientificHeatmapTextFormatter.INSTANCE;
        }
        return longFormatter;
    }

    public void setLongFormatter(ITextFormatter longFormatter) {
        this.longFormatter = longFormatter;
        firePropertyChange(PROPERTY_LONG_FORMATTER, null, longFormatter);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public IAggregator getAggregator() {
        if (this.aggregator == null) {
            return defaultAggregator(decorator);
        }

        return aggregator;
    }

    private IAggregator defaultAggregator(Decorator decorator) {
        if (decorator instanceof LinearDecorator ||
                decorator instanceof ZScoreDecorator) {
            return MeanAggregator.INSTANCE;
        } else if (decorator instanceof CategoricalDecorator ||
                decorator instanceof BinaryDecorator) {
            return NonZeroCountAggregator.INSTANCE;
        } else if (decorator instanceof PValueDecorator) {
            return LogSumAggregator.INSTANCE;
        }
        return SumAggregator.INSTANCE;
    }

    public void populateDetails(List<DetailsDecoration> details, IMatrix matrix, String row, String column, int layerIndex, boolean isSelected) {
        DetailsDecoration decoration = new DetailsDecoration(getName(), getDescription(), getDescriptionUrl(), null, getValueUrl());
        decoration.setReference(this);
        decoration.setIndex(layerIndex);
        if (isSelected) {
            decoration.setSelected(true);
        }
        if (row != null && column != null) {
            boolean previousShowValue = getDecorator().isShowValue();
            getDecorator().setShowValue(true, true);
            getDecorator().decorate(decoration, getLongFormatter(), matrix, matrix.getLayers().get(layerIndex), row, column);
            getDecorator().setShowValue(previousShowValue, true);
        }
        details.add(decoration);
    }

}
