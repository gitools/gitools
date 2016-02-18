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


import org.apache.commons.math3.analysis.function.Logit;
import org.gitools.api.matrix.*;
import org.gitools.matrix.transform.parameters.DoubleParameter;
import org.gitools.utils.aggregation.MaxAggregator;
import org.gitools.utils.aggregation.MinAggregator;

public class ScaleFunction extends ConfigurableTransformFunction {

    public static final String LOW = "Low";
    public static final String HIGH = "High";
    private Logit logit;
    private DoubleParameter lowParameter;
    private DoubleParameter highParameter;
    private double min;
    private double max;
    private IMatrixLayer<Double> resultLayer;

    public ScaleFunction(IMatrixLayer<Double> resultLayer) {
        super("Scale", "Returns the Logit transformation");
        this.resultLayer = resultLayer;
    }

    /*
    *        (low-high)(x - min)
    * f(x) = -----------------------  + a
    *               max - min
    */
    @Override
    public Double apply(Double value, IMatrixPosition position) {
        Double low = lowParameter.getParameterValue();
        Double high = highParameter.getParameterValue();

        if (value != null) {

            return (high-low) * (value - min) / (max - min) + low;
        }
        return null;
    }

    @Override
    public void onBeforeIterate(IMatrixIterable<Double> parentIterable) {
        IMatrix matrix = parentIterable.getPosition().getMatrix();


        IMatrixIterable<Double> matrixIterable = matrix.newPosition()
                .iterate(resultLayer, matrix.getRows(), matrix.getColumns());

        max = MaxAggregator.INSTANCE.aggregate(matrixIterable);
        min = MinAggregator.INSTANCE.aggregate(matrixIterable);

    }

    @Override
    public ScaleFunction createNew() {
        return new ScaleFunction(resultLayer);
    }

    @Override
    protected void createDefaultParameters() {
        lowParameter = new DoubleParameter();
        lowParameter.setParameterValue(0.0);
        lowParameter.setDescription("Low bound");
        addParameter(LOW, lowParameter);

        highParameter = new DoubleParameter();
        highParameter.setParameterValue(1.0);
        highParameter.setDescription("High bound");
        addParameter(HIGH, highParameter);

    }

    @Override
    public String getName() {
        return super.getName()  + " to [" + lowParameter.getParameterValue() + "," + highParameter.getParameterValue() +  "]";
    }
}
