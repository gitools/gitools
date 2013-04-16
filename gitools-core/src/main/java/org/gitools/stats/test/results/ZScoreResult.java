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
package org.gitools.stats.test.results;

import org.gitools.matrix.model.matrix.element.AttributeDef;

public class ZScoreResult extends CommonResult
{

    private double observed;
    private double expectedMean;
    private double expectedStdev;
    private double zscore;

    public ZScoreResult()
    {
        super(0, 0.0, 0.0, 0.0);
        observed = expectedMean = expectedStdev = 0;
        zscore = 0;
    }

    public ZScoreResult(int n, double leftPvalue, double rightPvalue, double twoTailPvalue, double observed, double expectedMean, double expectedStdev, double zscore)
    {

        super(n, leftPvalue, rightPvalue, twoTailPvalue);

        this.observed = observed;
        this.expectedMean = expectedMean;
        this.expectedStdev = expectedStdev;
        this.zscore = zscore;
    }

    @AttributeDef(id = "observed", name = "Observed value", description = "Value observed")
    public double getObserved()
    {
        return observed;
    }

    public void setObserved(double observed)
    {
        this.observed = observed;
    }

    @AttributeDef(id = "expected-mean", name = "Expected mean", description = "Value mean expected by chance")
    public double getExpectedMean()
    {
        return expectedMean;
    }

    public void setExpectedMean(double expectedMean)
    {
        this.expectedMean = expectedMean;
    }

    @AttributeDef(id = "expected-stdev", name = "Expected stddev", description = "Value standard deviation expected by chance")
    public double getExpectedStdev()
    {
        return expectedStdev;
    }

    public void setExpectedStdev(double expectedStdev)
    {
        this.expectedStdev = expectedStdev;
    }

    @AttributeDef(id = "z-score", name = "Z Score", description = "Normal distribution Z Score")
    public double getZscore()
    {
        return zscore;
    }

    public void setZscore(double zscore)
    {
        this.zscore = zscore;
    }
}
