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

import org.gitools.analysis.stats.FisherExactTest;
import org.gitools.analysis.stats.test.results.CommonResult;
import org.gitools.analysis.stats.test.results.FisherResult;

public class FisherTest extends AbstractTest {

    private int populationNotOnes, populationOnes;

    public FisherTest() {
        super("fisher", FisherResult.class);
    }

    @Override
    public void processPopulation(Iterable<Double> population) {

        populationNotOnes = populationOnes = 0;

        for (Double value : population) {
            if (value != null) {
                if (value == 1.0) {
                    populationOnes++;
                } else {
                    populationNotOnes++;
                }
            }
        }
    }

    @Override
    public CommonResult processTest(Iterable<Double> values) {

        int notOnes = 0, ones = 0;
        for (Double value : values) {
            if (value != null) {
                if (value == 1.0) {
                    ones++;
                } else {
                    notOnes++;
                }
            }
        }

        FisherExactTest fisher = new FisherExactTest(ones, notOnes, (populationOnes - ones), (populationNotOnes - notOnes));
        fisher.testContingencyTable();
        return new FisherResult(
                notOnes + ones,
                fisher.getLeftPValue(),
                fisher.getRightPValue(),
                fisher.getTwoTailPValue(),
                fisher.getA(),
                fisher.getB(),
                fisher.getC(),
                fisher.getD()
        );
    }

}
