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
package org.gitools.core.matrix.filter;

import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.operators.Operator;

public class DataIntegrationCriteria extends ValueFilterCriteria {

    private Operator operator;

    public DataIntegrationCriteria(IMatrixLayer layer, CutoffCmp comparator, double value, Operator operator) {
        super(layer, comparator, value);
        this.operator = operator;
    }


    public String getAttributeName() {
        return layer.getName();
    }


    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }


    @Override
    public String toString() {
        return operator.getLongName() + " " + getAttributeName().toString() + " " + comparator.toString() + " " + value;
    }
}
