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
package org.gitools.analysis.clustering.distance;


import org.apache.commons.math3.util.FastMath;

import java.util.Iterator;

public class ManhattanDistance implements DistanceMeasure {

    @Override
    public Double compute(Iterable<Double> a, Iterable<Double> b) {

        Iterator<Double> ai = a.iterator();
        Iterator<Double> bi = b.iterator();

        double sum = 0.0;
        while (ai.hasNext() && bi.hasNext()) {

            Double p1 = ai.next();
            Double p2 = bi.next();

            if (p1 == null || p2 == null) {
                continue;
            }

            sum += FastMath.abs(p1 - p2);
        }
        return sum;

    }
}
