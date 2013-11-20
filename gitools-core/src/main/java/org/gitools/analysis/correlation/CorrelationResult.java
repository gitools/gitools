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
package org.gitools.analysis.correlation;


import org.gitools.core.matrix.model.matrix.element.LayerDef;

public class CorrelationResult {

    private int n;
    private double score;
    private double standardError;

    public CorrelationResult() {
    }

    public CorrelationResult(int n, double score, double standardError) {
        this.n = n;
        this.score = score;
        this.standardError = standardError;
    }

    @LayerDef(id = "n", name = "Observations", description = "Number of observations")
    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    @LayerDef(id = "score", name = "Correlation", description = "Correlation score")
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @LayerDef(id = "se", name = "Standard Error", description = "Standard Error")
    public double getStandardError() {
        return standardError;
    }

    public void setStandardError(double standardError) {
        this.standardError = standardError;
    }
}
