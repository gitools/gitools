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
package org.gitools.plugins.mutex.analysis;

import org.gitools.analysis.stats.test.results.SimpleResult;
import org.gitools.matrix.model.matrix.element.LayerDef;


public class MutualExclusiveResult extends SimpleResult {

    private int coverage;
    private int signal;
    private double signalCoverageRatio;
    private double expectedMean;
    private double expectedVar;
    private double zscore;

    public MutualExclusiveResult(int N, double twoTailPvalue, int coverage, int signal, double expectedMean, double expectedVar) {
        super(N, twoTailPvalue);

        this.coverage = coverage;
        this.signal = signal;
        this.signalCoverageRatio = (double) signal / (double) coverage;
        this.expectedMean = expectedMean;
        this.expectedVar = expectedVar;
        this.zscore = (this.coverage-this.expectedMean) / Math.sqrt(this.expectedVar);
    }

    public MutualExclusiveResult() {
        super(0, 0.0);
    }

    @LayerDef(id = "coverage", name = "Observed coverage", description = "Observed coverage")
    public int getCoverage() {
        return coverage;
    }

    public void setCoverage(int coverage) {
        this.coverage = coverage;
    }

    @LayerDef(id = "signal", name = "Observed signal", description = "Observed signal")
    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    @LayerDef(id = "sig-cov-ratio", name = "Signal-coverage ratio", description = "Observed signal-coverage ratio")
    public double getSignalCoverageRatio() {
        return signalCoverageRatio;
    }

    public void setSignalCoverageRatio(double signalCoverageRatio) {
        this.signalCoverageRatio = signalCoverageRatio;
    }

    @LayerDef(id = "expected-mean", name = "Expected mean", description = "Value mean expected by chance")
    public double getExpectedMean() {
        return expectedMean;
    }

    public void setExpectedMean(double expectedMean) {
        this.expectedMean = expectedMean;
    }

    @LayerDef(id = "expected-var", name = "Expected variance", description = "Variance expected by chance")
    public double getExpectedVar() {
        return expectedVar;
    }

    public void setExpectedVar(double expectedVar) {
        this.expectedVar = expectedVar;
    }

    @LayerDef(id = "z-score", name = "Z Score", description = "Z Score")
    public double getZscore() {
        return zscore;
    }

    public void setZscore(double zscore) {
        this.zscore = zscore;
    }
}
