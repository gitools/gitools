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


public class GroupComparisonResult extends CommonResult {

    private int N_group1;
    private int N_group2;
    private double U1;
    private double U2;
    private double pValueLogSum;

    public GroupComparisonResult() {
        super(0, 0.0, 0.0, 0.0);
    }

    public GroupComparisonResult(int N, int N_group1, int N_group2, double leftPvalue, double rightPvalue, double twoTailPvalue, double U1, double U2) {

        super(N, leftPvalue, rightPvalue, twoTailPvalue);
        this.N_group1 = N_group1;
        this.N_group2 = N_group2;
        this.U1 = U1;
        this.U2 = U2;
        pValueLogSum = Math.log10(leftPvalue) - Math.log10(rightPvalue);
    }

    @LayerDef(id = "N-group1", name = "N Group 1", description = "Number of elements in Group 1")
    public int getN_group1() {
        return N_group1;
    }

    public void setN_group1(int N_group1) {
        this.N_group1 = N_group1;
    }

    @LayerDef(id = "N-group2", name = "N Group 2", description = "Number of elements in Group 2")
    public int getN_group2() {
        return N_group2;
    }

    public void setN_group2(int N_group2) {
        this.N_group2 = N_group2;
    }


    @LayerDef(id = "p-value-log-sum", name = "P-Value Log Sum", description = "Score combining left and right p-values.")
    public double getpValueLogSum() {
        return pValueLogSum;
    }

    public void setpValueLogSum(double pValueLogSum) {
        this.pValueLogSum = pValueLogSum;
    }


    @LayerDef(id = "U1", name = "U1", description = "Mann–Whitney U1 statistic used to calculate one-sided alternative 'less'")
    public double getU1() {
        return U1;
    }

    public void setU1(double u1) {
        U1 = u1;
    }

    @LayerDef(id = "U2", name = "U2", description = "Mann–Whitney U2 statistic used to calculate one-sided alternative 'greater'")
    public double getU2() {
        return U2;
    }

    public void setU2(double u2) {
        U2 = u2;
    }
}
