/*
 * #%L
 * org.gitools.analysis
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.analysis.clustering.distance;

import org.apache.commons.math3.util.FastMath;

import java.util.Iterator;

public class EuclideanDistance implements DistanceMeasure {

    private static EuclideanDistance INSTANCE = new EuclideanDistance();
    public static EuclideanDistance get() {
        return INSTANCE;
    }

    private EuclideanDistance() {
        super();
    }

    @Override
    public Double compute(Iterable<? extends Number> a, Iterable<? extends Number> b) {

        Iterator<? extends Number> ai = a.iterator();
        Iterator<? extends Number> bi = b.iterator();

        double sum = 0.0;
        while (ai.hasNext() && bi.hasNext()) {

            Number p1 = ai.next();
            Number p2 = bi.next();

            if (p1 == null || p2 == null) {
                continue;
            }

            final double dp = p1.doubleValue() - p2.doubleValue();
            sum += dp * dp;
        }

        return FastMath.sqrt(sum);
    }

    public String toString() {
        return "Euclidean distance";
    }

}
