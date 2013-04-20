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
package org.gitools.analysis.correlation.methods;

import org.gitools.analysis.AbstractMethod;
import org.gitools.analysis.MethodException;
import org.gitools.analysis.correlation.CorrelationMethod;
import org.gitools.analysis.correlation.CorrelationResult;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class PearsonCorrelationMethod extends AbstractMethod implements CorrelationMethod {

    public static final String ID = "pearson";

    public PearsonCorrelationMethod(Properties properties) {
        super(ID, "Pearson's correlation", "Pearson's product-moment correlation", CorrelationResult.class, properties);
    }

    @NotNull
    @Override
    public CorrelationResult correlation(double[] x, double[] y, int[] indices, int indicesLength) throws MethodException {
        CorrelationResult result = new CorrelationResult();

        double sumxy = 0;
        double sumx = 0;
        double sumx2 = 0;
        double sumy = 0;
        double sumy2 = 0;
        double n = indicesLength;

        for (int k = 0; k < indicesLength; k++) {
            int i = indices[k];
            double xi = x[i];
            double yi = y[i];
            sumxy += xi * yi;
            sumx += xi;
            sumx2 += xi * xi;
            sumy += yi;
            sumy2 += yi * yi;
        }

        double r = (sumxy - (sumx * sumy / n)) / Math.sqrt((sumx2 - (sumx * sumx / n)) * (sumy2 - (sumy * sumy / n)));

        double se = Math.sqrt((1 - (r * r)) / (n - 2));

        result.setN(indicesLength);
        result.setScore(r);
        result.setStandardError(se);

        return result;
    }
}
