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
package org.gitools.core.model.decorator;

import com.jgoodies.binding.beans.Model;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.model.decorator.impl.*;
import org.gitools.utils.colorscale.IColorScale;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({BinaryDecorator.class, LinearDecorator.class, PValueDecorator.class, ZScoreDecorator.class, CorrelationDecorator.class, CategoricalDecorator.class})
public abstract class Decorator<C extends IColorScale> extends Model {

    public static final String PROPERTY_SHOW_VALUE = "showValue";

    @XmlAttribute
    private String name;

    @XmlElement(name = "show-value")
    private boolean showValue = false;

    public Decorator() {
        super();
    }

    public abstract void decorate(Decoration decoration, IMatrix matrix, int row, int column, int layer);

    public abstract C getScale();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowValue() {
        return showValue;
    }

    public void setShowValue(boolean showValue) {
        boolean oldValue = this.showValue;
        this.showValue = showValue;
        firePropertyChange(PROPERTY_SHOW_VALUE, oldValue, showValue);
    }

    protected static double toDouble(Object value) {
        return MatrixUtils.doubleValue(value);
    }
}
