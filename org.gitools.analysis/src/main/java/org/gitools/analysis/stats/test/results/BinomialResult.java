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

public class BinomialResult extends CommonResult {

    public enum Distribution {
        BINOMIAL, NORMAL, POISSON
    }

    private Distribution distribution;
    private int observed;
    private double expectedMean;
    private double expectedStdev;
    private double probability;

    public BinomialResult() {
        super(0, 0.0, 0.0, 0.0);
        observed = 0;
        expectedMean = expectedStdev = 0;
        probability = 0;
    }

    public BinomialResult(Distribution aprox, int n, double leftPvalue, double rightPvalue, double twoTailPvalue, int observed, double expectedMean, double expectedStdev, double p) {

        super(n, leftPvalue, rightPvalue, twoTailPvalue);

        this.distribution = aprox;
        this.observed = observed;
        this.expectedMean = expectedMean;
        this.expectedStdev = expectedStdev;
        this.probability = p;
    }

    @LayerDef(id = "observed", name = "Observed events", description = "Number of positive events observed")
    public int getObserved() {
        return observed;
    }

    public void setObserved(int observed) {
        this.observed = observed;
    }

    @LayerDef(id = "expected-mean", name = "Expected mean", description = "Number of positive events expected by chance")
    public double getExpectedMean() {
        return expectedMean;
    }

    public void setExpectedMean(double expectedMean) {
        this.expectedMean = expectedMean;
    }

    @LayerDef(id = "expected-stdev", name = "Expected stddev", description = "Standard deviation of the number of positive events expected by chance")
    public double getExpectedStdev() {
        return expectedStdev;
    }

    public void setExpectedStdev(double expectedStdev) {
        this.expectedStdev = expectedStdev;
    }

    @LayerDef(id = "distribution", name = "Distribution", description = "Wich distribution has been used to do calculations (Binomial exact, Normal or Poisson)")
    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }

    @LayerDef(id = "probability", name = "Probability", description = "Population probability of a positive event")
    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
