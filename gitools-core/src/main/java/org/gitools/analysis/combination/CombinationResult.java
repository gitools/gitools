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
package org.gitools.analysis.combination;

import org.gitools.matrix.model.matrix.element.AttributeDef;


/**
 * @noinspection ALL
 */
public class CombinationResult {

    private int n;

    private double zscore;

    private double pvalue;

    @AttributeDef(id = "N", name = "N", description = "Number of pvalues combined")
    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    @AttributeDef(id = "z-score", name = "Z-Score", description = "Z-Score of the combination")
    public double getZscore() {
        return zscore;
    }

    public void setZscore(double zscore) {
        this.zscore = zscore;
    }

    @AttributeDef(id = "p-value", name = "P-Value", description = "Combined P-Value")
    public double getPvalue() {
        return pvalue;
    }

    public void setPvalue(double pvalue) {
        this.pvalue = pvalue;
    }
}
