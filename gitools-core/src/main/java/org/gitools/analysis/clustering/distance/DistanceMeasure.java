package org.gitools.analysis.clustering.distance;

public interface DistanceMeasure {

    Double compute(Iterable<Double> a, Iterable<Double> b);

}
