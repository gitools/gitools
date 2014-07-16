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


public class MutualExclusiveResult {

    public static final String COOC_PVALUE = "coocPvalue";
    public static final String MUTEX_PVALUE = "mutexPvalue";
    public static final String Z_SCORE = "z-score";
    private int N;
    private double mutexPvalue;
    private double coocPvalue;
    private int coverage;
    private int signal;
    private int nPermMutex;
    private int nPermCooc;
    private double signalCoverageRatio;
    private double expectedMean;
    private double expectedVar;
    private double zscore;

    public MutualExclusiveResult(int N, double zscore, double mutexPvalue, double coocPvalue,
                                 int coverage, int signal,
                                 double expectedMean, double expectedVar,
                                 int nPermMutex, int nPermCooc) {
        this.N = N;
        this.mutexPvalue = mutexPvalue;
        this.coocPvalue = coocPvalue;
        this.coverage = coverage;
        this.signal = signal;
        this.nPermMutex = nPermMutex;
        this.nPermCooc = nPermCooc;
        this.signalCoverageRatio = (double) signal / (double) coverage;
        this.expectedMean = expectedMean;
        this.expectedVar = expectedVar;
        this.zscore = zscore;
    }

    public MutualExclusiveResult() {
        this.N = 0;
        this.mutexPvalue = 0.0;
        this.coocPvalue = 0.0;
    }

    @LayerDef(id = "N", name = "N", description = "Number of elements")
    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    @LayerDef(id = MUTEX_PVALUE, name = "Mut-Ex p-value", description = "Mutual exclusive p-value",
            groups = {SimpleResult.RESULTS_GROUP,LayerDef.ALL_DATA_GROUP})
    public double getMutexPvalue() {
        return mutexPvalue;
    }

    public void setMutexPvalue(double mutexPvalue) {
        this.mutexPvalue = mutexPvalue;
    }

    @LayerDef(id = COOC_PVALUE, name = "Co-oc p-value", description = "Co-occurrence p-value",
    groups = {SimpleResult.RESULTS_GROUP,LayerDef.ALL_DATA_GROUP})
    public double getCoocPvalue() {
        return coocPvalue;
    }

    public void setCoocPvalue(double coocPvalue) {
        this.coocPvalue = coocPvalue;
    }

    @LayerDef(id = "coverage", name = "Observed coverage", description = "Observed coverage (items with signal)")
    public int getCoverage() {
        return coverage;
    }

    public void setCoverage(int coverage) {
        this.coverage = coverage;
    }

    @LayerDef(id = "signal", name = "Observed signal", description = "Observed signal in data (positive events)")
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

    @LayerDef(id = Z_SCORE, name = "Z Score", description = "Z Score",
            groups = {SimpleResult.RESULTS_GROUP,LayerDef.ALL_DATA_GROUP})
    public double getZscore() {
        return zscore;
    }

    public void setZscore(double zscore) {
        this.zscore = zscore;
    }

    @LayerDef(id = "n-perm-mutex", name = "N perm mut-ex ", description = "Permutations with signal >= observed")
    public int getnPermMutex() {
        return nPermMutex;
    }

    public void setnPermMutex(int nPermMutex) {
        this.nPermMutex = nPermMutex;
    }

    @LayerDef(id = "n-perm-cooc", name = "N perm co-oc ", description = "Permutations with signal <= observed")
    public int getnPermCooc() {
        return nPermCooc;
    }

    public void setnPermCooc(int nPermCooc) {
        this.nPermCooc = nPermCooc;
    }
}
