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
package org.gitools.core.analysis.correlation.methods;

import org.gitools.core.analysis.AbstractMethod;
import org.gitools.core.analysis.MethodException;
import org.gitools.core.analysis.correlation.CorrelationMethod;
import org.gitools.core.analysis.correlation.CorrelationResult;

import java.util.Iterator;
import java.util.Properties;

public class PearsonCorrelationMethod extends AbstractMethod implements CorrelationMethod {

    public static final String ID = "pearson";

    public PearsonCorrelationMethod(Properties properties) {
        super(ID, "Pearson's correlation", "Pearson's product-moment correlation", CorrelationResult.class, properties);
    }

    @Override
    public CorrelationResult correlation(Iterable<Double> x, Iterable<Double> y, Double valueNaN) throws MethodException {

        Iterator<Double> xIterator = x.iterator();
        Iterator<Double> yIterator = y.iterator();

        int n = 0;
        double sumXY = 0, sumX = 0, sumX2 = 0, sumY = 0, sumY2 = 0;


        while (xIterator.hasNext() && yIterator.hasNext()) {
            Double xi = xIterator.next();
            Double yi = yIterator.next();


            if (xi == null) {
                xi = valueNaN;
            }

            if (yi == null) {
                yi = valueNaN;
            }

            if (xi == null || yi == null || Double.isNaN(xi) || Double.isNaN(yi)) {
                continue;
            }

            n++;
            sumXY += xi * yi;
            sumX += xi;
            sumX2 += xi * xi;
            sumY += yi;
            sumY2 += yi * yi;
        }

        double score = (sumXY - (sumX * sumY / n)) / Math.sqrt((sumX2 - (sumX * sumX / n)) * (sumY2 - (sumY * sumY / n)));
        double standardError = Math.sqrt((1 - (score * score)) / (n - 2));

        return new CorrelationResult(n, score, standardError);
    }
}
