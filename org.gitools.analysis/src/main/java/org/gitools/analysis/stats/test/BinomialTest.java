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
package org.gitools.analysis.stats.test;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.gitools.analysis.stats.test.results.BinomialResult;
import org.gitools.analysis.stats.test.results.CommonResult;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;

public class BinomialTest extends AbstractEnrichmentTest {

    private static final int exactSizeLimit = 100000;

    public enum AproximationMode {
        onlyExact, onlyNormal, onlyPoisson, automatic
    }

    private abstract class BinomialAproximation {
        public abstract CommonResult getResult(int observed, int n, double p, double expectedMean, double expectedStdev, double expectedVar);
    }

    private final AproximationMode aproxMode;

    private double p;

    private BinomialAproximation aprox;

    public BinomialTest(AproximationMode aproxMode) {
        super("binomial", BinomialResult.class);

        this.aproxMode = aproxMode;
        switch (aproxMode) {
            case onlyExact:
                this.aprox = new BinomialAproximation() {

                    @Override
                    public CommonResult getResult(int observed, int n, double p, double expectedMean, double expectedStdev, double expectedVar) {

                        return resultWithExact(observed, n, p, expectedMean, expectedStdev);
                    }
                };
                break;
            case onlyNormal:
                this.aprox = new BinomialAproximation() {

                    @Override
                    public CommonResult getResult(int observed, int n, double p, double expectedMean, double expectedStdev, double expectedVar) {

                        return resultWithNormal(observed, n, p, expectedMean, expectedStdev);
                    }
                };
                break;
            case onlyPoisson:
                this.aprox = new BinomialAproximation() {

                    @Override
                    public CommonResult getResult(int observed, int n, double p, double expectedMean, double expectedStdev, double expectedVar) {

                        return resultWithPoisson(observed, n, p, expectedMean, expectedStdev);
                    }
                };
                break;
            case automatic:
                this.aprox = new BinomialAproximation() {

                    @Override
                    public CommonResult getResult(int observed, int n, double p, double expectedMean, double expectedStdev, double expectedVar) {

                        if (n <= exactSizeLimit) {
                            return resultWithExact(observed, n, p, expectedMean, expectedStdev);
                        } else if (n >= 150 && expectedVar >= 0.9 * expectedMean) {
                            return resultWithPoisson(observed, n, p, expectedMean, expectedStdev);
                        } else if ((n * p >= 5) && (n * (1 - p) >= 5)) {
                            return resultWithNormal(observed, n, p, expectedMean, expectedStdev);
                        } else {
                            return resultWithExact(observed, n, p, expectedMean, expectedStdev);
                        }
                    }
                };
                break;
        }
    }

    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append("binomial");
        switch (aproxMode) {
            case automatic:
                break;
            case onlyExact:
                sb.append("-exact");
                break;
            case onlyNormal:
                sb.append("-normal");
                break;
            case onlyPoisson:
                sb.append("-poisson");
                break;
        }
        return sb.toString();
    }

    @Override
    public void processPopulation(Iterable<Double> population) {

        double size = 0;
        double observed = 0;

        for (Double value : population) {
            if (value == null) {
                continue;
            }

            if (value == 1.0) {
                observed++;
            }

            size++;
        }

        p = observed / size;
    }

    @Override
    public CommonResult processTest(Iterable<Double> values) {

        int observed = 0;
        int n = 0;

        for (Double value : filter(values, notNull())) {
            if (value == 1.0) {
                observed++;
            }
            n++;
        }

        double expectedMean = n * p;
        double expectedVar = n * p * (1.0 - p);
        double expectedStdev = Math.sqrt(expectedVar);

        return aprox.getResult(observed, n, p, expectedMean, expectedStdev, expectedVar);
    }

    private static CommonResult resultWithExact(int observed, int n, double p, double expectedMean, double expectedStdev) {

        double leftPvalue;
        double rightPvalue;
        double twoTailPvalue;

        //FIXME: May be it's better to return null ???
        if (n == 0) {
            leftPvalue = rightPvalue = twoTailPvalue = 1.0;
        } else {

            BinomialDistribution distribution = new BinomialDistribution(n, p);

            leftPvalue = distribution.cumulativeProbability(observed);
            rightPvalue = observed > 0 ? distribution.cumulativeProbability(observed - 1, n) : 1.0;
            twoTailPvalue = leftPvalue + rightPvalue;
            twoTailPvalue = twoTailPvalue > 1.0 ? 1.0 : twoTailPvalue;
        }

        return new BinomialResult(BinomialResult.Distribution.BINOMIAL, n, leftPvalue, rightPvalue, twoTailPvalue, observed, expectedMean, expectedStdev, p);
    }

    private static NormalDistribution NORMAL = new NormalDistribution();

    private static CommonResult resultWithNormal(int observed, int n, double p, double expectedMean, double expectedStdev) {

        double zscore;
        double leftPvalue;
        double rightPvalue;
        double twoTailPvalue;

        // Calculate zscore and pvalues
        zscore = (observed - expectedMean) / expectedStdev;

        leftPvalue = NORMAL.cumulativeProbability(zscore);
        rightPvalue = 1.0 - leftPvalue;
        twoTailPvalue = (zscore <= 0 ? leftPvalue : rightPvalue) * 2;
        twoTailPvalue = twoTailPvalue > 1.0 ? 1.0 : twoTailPvalue;

        return new BinomialResult(BinomialResult.Distribution.NORMAL, n, leftPvalue, rightPvalue, twoTailPvalue, observed, expectedMean, expectedStdev, p);
    }

    private static CommonResult resultWithPoisson(int observed, int n, double p, double expectedMean, double expectedStdev) {

        double leftPvalue;
        double rightPvalue;
        double twoTailPvalue;

        try {
            PoissonDistribution poisson = new PoissonDistribution(expectedMean);

            leftPvalue = poisson.cumulativeProbability(observed);
            rightPvalue = observed > 0 ? poisson.cumulativeProbability(observed - 1, n) : 1.0;

            twoTailPvalue = (observed <= expectedMean ? leftPvalue : rightPvalue) * 2; //FIXME: Review
            twoTailPvalue = twoTailPvalue > 1.0 ? 1.0 : twoTailPvalue;
        } catch (ArithmeticException e) {
            leftPvalue = rightPvalue = twoTailPvalue = Double.NaN;
        }

        return new BinomialResult(BinomialResult.Distribution.POISSON, n, leftPvalue, rightPvalue, twoTailPvalue, observed, expectedMean, expectedStdev, p);
    }

}
