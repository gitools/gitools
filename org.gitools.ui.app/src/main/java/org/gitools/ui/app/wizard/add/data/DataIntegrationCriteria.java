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
package org.gitools.ui.app.wizard.add.data;

import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.operators.Operator;

public class DataIntegrationCriteria {

    private String attributeName;
    private int attributeIndex;
    private CutoffCmp comparator;
    private double cutoffValue;
    private Operator operator;

    public DataIntegrationCriteria(String attributeName, int attributeIndex, CutoffCmp comparator, double cutoffValue, Operator operator) {
        this.attributeName = attributeName;
        this.attributeIndex = attributeIndex;
        this.comparator = comparator;
        this.cutoffValue = cutoffValue;
        this.operator = operator;
    }

    public DataIntegrationCriteria(String attributeName, int attributeIndex, CutoffCmp comparator, double cutoffValue, String operator) {
        this.attributeName = attributeName;
        this.attributeIndex = attributeIndex;
        this.comparator = comparator;
        this.cutoffValue = cutoffValue;
        this.operator = Operator.getFromName(operator);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public int getAttributeIndex() {
        return attributeIndex;
    }

    public void setAttributeIndex(int attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    public CutoffCmp getComparator() {
        return comparator;
    }

    public void setComparator(CutoffCmp comparator) {
        this.comparator = comparator;
    }

    public double getCutoffValue() {
        return this.cutoffValue;
    }

    public void setCutoffValue(double cutoffValue) {
        this.cutoffValue = cutoffValue;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }


    @Override
    public String toString() {
        return attributeName.toString() + " " + comparator.toString() + " " + cutoffValue + " " + operator.getLongName();
    }
}
