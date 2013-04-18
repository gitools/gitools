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
package org.gitools.stats.test;

import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.stat.Probability;
import org.gitools.stats.calc.OnesCountStatistic;
import org.gitools.stats.calc.Statistic;
import org.gitools.stats.test.results.BinomialResult;
import org.gitools.stats.test.results.CommonResult;
import org.jetbrains.annotations.NotNull;

/**
 * @noinspection ALL
 */
public class BinomialTest extends AbstractTest {

    private static final int exactSizeLimit = 100000;

    public enum AproximationMode {
        onlyExact, onlyNormal, onlyPoisson, automatic
    }

    private abstract class BinomialAproximation {
        @NotNull
        public abstract CommonResult getResult(int observed, int n, double p, double expectedMean, double expectedStdev, double expectedVar);
    }

    private final Statistic statCalc;

    private final AproximationMode aproxMode;

    private double p;

    private BinomialAproximation aprox;

    public BinomialTest(@NotNull AproximationMode aproxMode) {
        this.statCalc = new OnesCountStatistic();
        this.aproxMode = aproxMode;

        switch (aproxMode) {
            case onlyExact:
                this.aprox = new BinomialAproximation() {
                    @NotNull
                    @Override
                    public CommonResult getResult(int observed, int n, double p, double expectedMean, double expectedStdev, double expectedVar) {

                        return resultWithExact(observed, n, p, expectedMean, expectedStdev);
                    }
                };
                break;
            case onlyNormal:
                this.aprox = new BinomialAproximation() {
                    @NotNull
                    @Override
                    public CommonResult getResult(int observed, int n, double p, double expectedMean, double expectedStdev, double expectedVar) {

                        return resultWithNormal(observed, n, p, expectedMean, expectedStdev);
                    }
                };
                break;
            case onlyPoisson:
                this.aprox = new BinomialAproximation() {
                    @NotNull
                    @Override
                    public CommonResult getResult(int observed, int n, double p, double expectedMean, double expectedStdev, double expectedVar) {

                        return resultWithPoisson(observed, n, p, expectedMean, expectedStdev);
                    }
                };
                break;
            case automatic:
                this.aprox = new BinomialAproximation() {
                    @NotNull
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

    @NotNull
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

	/*@Override
    public String[] getResultNames() {
		return new BinomialResult().getNames();
	}*/

    @NotNull
    @Override
    public Class<? extends CommonResult> getResultClass() {
        return BinomialResult.class;
    }

    @Override
    public void processPopulation(String name, @NotNull DoubleMatrix1D population) {
        p = statCalc.calc(population) / population.size();
    }

    @NotNull
    @Override
    public CommonResult processTest(String condName, @NotNull DoubleMatrix1D condItems, String groupName, int[] groupItemIndices) {

        // Create a view with group values (excluding NaN's)

        final DoubleMatrix1D groupItems = condItems.viewSelection(groupItemIndices).viewSelection(notNaNProc);

        // Calculate observed statistic

        int observed = (int) statCalc.calc(groupItems);

        // Calculate expected mean and standard deviation

        int n = groupItems.size();

        double expectedMean = n * p;
        double expectedVar = n * p * (1.0 - p);
        double expectedStdev = Math.sqrt(expectedVar);

        return aprox.getResult(observed, n, p, expectedMean, expectedStdev, expectedVar);
    }

    @NotNull
    final CommonResult resultWithExact(int observed, int n, double p, double expectedMean, double expectedStdev) {

        double leftPvalue;
        double rightPvalue;
        double twoTailPvalue;

        //FIXME: May be it's better to return null ???
        if (n == 0) {
            leftPvalue = rightPvalue = twoTailPvalue = 1.0;
        } else {
            leftPvalue = Probability.binomial(observed, n, p);
            rightPvalue = observed > 0 ? Probability.binomialComplemented(observed - 1, n, p) : 1.0;

            twoTailPvalue = leftPvalue + rightPvalue;
            twoTailPvalue = twoTailPvalue > 1.0 ? 1.0 : twoTailPvalue;
        }

        return new BinomialResult(BinomialResult.Distribution.BINOMIAL, n, leftPvalue, rightPvalue, twoTailPvalue, observed, expectedMean, expectedStdev, p);
    }

    @NotNull
    final CommonResult resultWithNormal(int observed, int n, double p, double expectedMean, double expectedStdev) {

        double zscore;
        double leftPvalue;
        double rightPvalue;
        double twoTailPvalue;

        // Calculate zscore and pvalues

        zscore = (observed - expectedMean) / expectedStdev;

        leftPvalue = Probability.normal(zscore);
        rightPvalue = 1.0 - leftPvalue;
        twoTailPvalue = (zscore <= 0 ? leftPvalue : rightPvalue) * 2;
        twoTailPvalue = twoTailPvalue > 1.0 ? 1.0 : twoTailPvalue;

        return new BinomialResult(BinomialResult.Distribution.NORMAL, n, leftPvalue, rightPvalue, twoTailPvalue, observed, expectedMean, expectedStdev, p);
    }

    @NotNull
    final CommonResult resultWithPoisson(int observed, int n, double p, double expectedMean, double expectedStdev) {

        double leftPvalue;
        double rightPvalue;
        double twoTailPvalue;

        // Calculate pvalues

        try {
            leftPvalue = Probability.poisson(observed, expectedMean);
            rightPvalue = observed > 0 ? Probability.poissonComplemented(observed - 1, expectedMean) : 1.0;

            twoTailPvalue = (observed <= expectedMean ? leftPvalue : rightPvalue) * 2; //FIXME: Review
            twoTailPvalue = twoTailPvalue > 1.0 ? 1.0 : twoTailPvalue;
        } catch (ArithmeticException e) {
            leftPvalue = rightPvalue = twoTailPvalue = Double.NaN;
        }

        return new BinomialResult(BinomialResult.Distribution.POISSON, n, leftPvalue, rightPvalue, twoTailPvalue, observed, expectedMean, expectedStdev, p);
    }

	/*private double filterPvalue(double pvalue) {
        if (pvalue < 0.0)
			pvalue = 0.0;
		else if (pvalue > 1.0)
			pvalue = 1.0;
		return pvalue;
	}*/
}
