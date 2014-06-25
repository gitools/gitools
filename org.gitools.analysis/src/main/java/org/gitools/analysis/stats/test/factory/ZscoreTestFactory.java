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
package org.gitools.analysis.stats.test.factory;

import org.gitools.analysis.ToolConfig;
import org.gitools.analysis.stats.calc.MeanStatistic;
import org.gitools.analysis.stats.calc.MedianStatistic;
import org.gitools.analysis.stats.calc.Statistic;
import org.gitools.analysis.stats.test.EnrichmentTest;
import org.gitools.analysis.stats.test.ZscoreTest;

import java.util.Map;


public final class ZscoreTestFactory extends TestFactory {

    public static final String NUM_SAMPLES_PROPERTY = "samples";
    public static final String ESTIMATOR_PROPERTY = "estimator";

    public static final String MEAN_ESTIMATOR = "mean";
    public static final String MEDIAN_ESTIMATOR = "median";

    public static final int DEFAULT_NUM_SAMPLES = 10000;

    private int numSamples;
    private final Statistic statCalc;

    public ZscoreTestFactory(ToolConfig config) {
        super(config);

        Map<String, String> props = config.getConfiguration();

        final String estimatorName = props.get(ESTIMATOR_PROPERTY);
        if (MEAN_ESTIMATOR.equalsIgnoreCase(estimatorName)) {
            this.statCalc = new MeanStatistic();
        } else if (MEDIAN_ESTIMATOR.equalsIgnoreCase(estimatorName)) {
            this.statCalc = new MedianStatistic();
        } else {
            this.statCalc = new MeanStatistic();
        }

        try {
            this.numSamples = Integer.parseInt(props.get(NUM_SAMPLES_PROPERTY));
        } catch (NumberFormatException e) {
            this.numSamples = DEFAULT_NUM_SAMPLES;
        }
    }


    @Override
    public EnrichmentTest create() {
        return new ZscoreTest(numSamples, statCalc);
    }

}
