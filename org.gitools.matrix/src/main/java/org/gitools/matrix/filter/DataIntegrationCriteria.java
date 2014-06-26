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
package org.gitools.matrix.filter;

import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.operators.Operator;
import org.gitools.utils.xml.adapter.OperatorXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataIntegrationCriteria extends ValueFilterFunction {

    @XmlJavaTypeAdapter(OperatorXmlAdapter.class)
    private Operator operator;

    public DataIntegrationCriteria() {
        //JAXB requirement
    }

    public DataIntegrationCriteria(IMatrixLayer layer, CutoffCmp comparator, Double value, Double nullConversion, Operator operator) {
        super(layer, comparator, value, nullConversion);
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }


    @Override
    public String toString() {
        String empty = nullConversion == null ? "" : " (empty: " + nullConversion + ")";
        return operator.getLongName() + " " + layerId + " " + comparator.toString() + " " + cutoffValue + empty;
    }
}
