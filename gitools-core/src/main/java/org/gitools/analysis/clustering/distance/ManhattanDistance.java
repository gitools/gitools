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

            if (p1==null || p2==null) {
                continue;
            }

            sum += FastMath.abs(p1 - p2);
        }
        return sum;

    }
}
