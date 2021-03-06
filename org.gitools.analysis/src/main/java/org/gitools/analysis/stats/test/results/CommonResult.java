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
package org.gitools.analysis.stats.test.results;

import org.gitools.matrix.model.matrix.element.LayerDef;

import java.io.Serializable;

public abstract class CommonResult extends SimpleResult implements Serializable {

    private int N;
    private double leftPvalue;
    private double rightPvalue;
    private double corrLeftPvalue;
    private double corrRightPvalue;

    public CommonResult(int N, double leftPvalue, double rightPvalue, double twoTailPvalue) {
        super(N, twoTailPvalue);

        this.leftPvalue = leftPvalue;
        this.N = N;
        this.rightPvalue = rightPvalue;
    }


    @LayerDef(id = "left-p-value",
            name = "Left P-Value",
            description = "P-Value for alternative hipothesis 'lower than'",
            groups = {SimpleResult.RESULTS_GROUP, LayerDef.ALL_DATA_GROUP})
    public double getLeftPvalue() {
        return leftPvalue;
    }

    public void setLeftPvalue(double leftPvalue) {
        this.leftPvalue = leftPvalue;
    }

    @LayerDef(id = "right-p-value",
            name = "Right P-Value",
            description = "P-Value for alternative hipothesis 'greater than'",
            groups = {SimpleResult.RESULTS_GROUP, LayerDef.ALL_DATA_GROUP})
    public double getRightPvalue() {
        return rightPvalue;
    }

    public void setRightPvalue(double rightPvalue) {
        this.rightPvalue = rightPvalue;
    }

    @LayerDef(id = "corrected-left-p-value",
            name = "Corrected left P-Value",
            description = "Corrected P-Value for alternative hipothesis 'lower than'",
            groups = {SimpleResult.CORRECTED_RESULTS_GROUP, LayerDef.ALL_DATA_GROUP})
    public double getCorrLeftPvalue() {
        return corrLeftPvalue;
    }

    public void setCorrLeftPvalue(double corrLeftPvalue) {
        this.corrLeftPvalue = corrLeftPvalue;
    }

    @LayerDef(id = "corrected-right-p-value",
            name = "Corrected right P-Value",
            description = "Corrected P-Value for alternative hipothesis 'greater than'",
            groups = {SimpleResult.CORRECTED_RESULTS_GROUP, LayerDef.ALL_DATA_GROUP})
    public double getCorrRightPvalue() {
        return corrRightPvalue;
    }

    public void setCorrRightPvalue(double corrRightPvalue) {
        this.corrRightPvalue = corrRightPvalue;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "N=" + N +
                ", leftPvalue=" + leftPvalue +
                ", rightPvalue=" + rightPvalue +
                ", twoTailPvalue=" + getTwoTailPvalue() +
                ", corrLeftPvalue=" + corrLeftPvalue +
                ", corrRightPvalue=" + corrRightPvalue +
                ", corrTwoTailPvalue=" + getCorrTwoTailPvalue() +
                '}';
    }
}
