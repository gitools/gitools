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

public abstract class SimpleResult implements Serializable {

    public static final String CORRECTED_RESULTS_GROUP = "Corrected test results";
    public static final String RESULTS_GROUP = "Test results";
    private int N;
    private double twoTailPvalue;
    private double corrTwoTailPvalue;

    public SimpleResult(int N, double twoTailPvalue) {

        this.N = N;
        this.twoTailPvalue = twoTailPvalue;
    }

    @LayerDef(id = "N",
            name = "N",
            description = "Number of elements (sample size)",
            groups = {SimpleResult.RESULTS_GROUP, SimpleResult.CORRECTED_RESULTS_GROUP, LayerDef.COMPLETE_GROUP})
    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    @LayerDef(id = "two-tail-p-value",
            name = "Two tail P-Value",
            description = "P-Value for alternative hipothesis different than",
            groups = {RESULTS_GROUP, LayerDef.COMPLETE_GROUP})
    public double getTwoTailPvalue() {
        return twoTailPvalue;
    }

    public void setTwoTailPvalue(double twoTailPvalue) {
        this.twoTailPvalue = twoTailPvalue;
    }

    @LayerDef(id = "corrected-two-tail-p-value",
            name = "Corrected two tail P-Value",
            description = "Corrected P-Value for alternative hipothesis different than",
            groups = {CORRECTED_RESULTS_GROUP, LayerDef.COMPLETE_GROUP})
    public double getCorrTwoTailPvalue() {
        return corrTwoTailPvalue;
    }

    public void setCorrTwoTailPvalue(double corrTwoTailPvalue) {
        this.corrTwoTailPvalue = corrTwoTailPvalue;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "N=" + N +
                ", twoTailPvalue=" + twoTailPvalue +
                ", corrTwoTailPvalue=" + corrTwoTailPvalue +
                '}';
    }
}
