/*
 * #%L
 * org.gitools.matrix
 * %%
 * Copyright (C) 2013 - 2016 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.matrix.transform;

import org.gitools.api.ApplicationContext;
import org.gitools.api.analysis.IAggregator;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.transform.parameters.AggregatorParameter;
import org.gitools.utils.aggregation.MeanAggregator;
import org.gitools.utils.aggregation.MedianAggregator;
import org.gitools.utils.aggregation.StdDevAggregator;


public class ZScoreFunction extends ConfigurableTransformFunction {

    public static final String AGGREGATION_PARAM = "Aggregation";
    AggregatorParameter aggregatorParameter;

    private IProgressMonitor monitor;
    private Double estimator;
    private Double stdDev;


    public ZScoreFunction() {
        super("Fold-Change");
    }


    @Override
    public Double apply(Double value, IMatrixPosition position) {

            if (estimator == null || stdDev == null || value == null) {
                return null;
            } else {
                return (value - estimator) / stdDev;
            }

    }


    @Override
    public void onBeforeIterate(IMatrixIterable<Double> parentIterable) {
        super.onBeforeIterate(parentIterable);
        monitor = ApplicationContext.getProgressMonitor().subtask();


        IMatrix matrix = parentIterable.getPosition().getMatrix();

        IMatrixLayer aggLayer = new MatrixLayer("agg", Double.class);

        estimator = aggregatorParameter.getParameterValue().aggregate(parentIterable);
        stdDev = StdDevAggregator.INSTANCE.aggregate(parentIterable);

        monitor.end();
    }



    @Override
    public ZScoreFunction createNew() {
        return new ZScoreFunction();
    }

    @Override
    protected void createDefaultParameters() {
        aggregatorParameter = new AggregatorParameter();
        aggregatorParameter.setDescription("Select if the fold change should be calculated with the row/column median or mean");
        IAggregator[] iAggregators = {
                MedianAggregator.INSTANCE,
                MeanAggregator.INSTANCE
        };
        aggregatorParameter.setChoices(iAggregators);
        addParameter(AGGREGATION_PARAM, aggregatorParameter);

    }

    public String getName() {
        return "Z-score";
    }

    public String getDescription() {
        return "The Z-score reflects the relationship to the estimator of all values";
    }

}
