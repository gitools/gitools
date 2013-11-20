package org.gitools.core.matrix.sort;

import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.IMatrixPosition;
import org.gitools.core.matrix.model.SortDirection;

import java.util.Comparator;

public class SingleSortByValueComparator implements Comparator<String> {

    private IMatrixPosition position;
    private IMatrixLayer<? extends Comparable> layer;
    private IMatrixDimension dimension;
    private SortDirection sortDirection;

    public SingleSortByValueComparator(IMatrixPosition position, IMatrixLayer<? extends Comparable> layer, IMatrixDimension dimension, SortDirection sortDirection) {
        this.position = position;
        this.layer = layer;
        this.dimension = dimension;
        this.sortDirection = sortDirection;
    }

    @Override
    public int compare(String o1, String o2) {

        Comparable v1 = position.getMatrix().get(layer, position.set(dimension, o1));
        Comparable v2 = position.getMatrix().get(layer, position.set(dimension, o2));

        if (v1==null && v2==null) {
            return 0;
        }

        if (v1==null) {
            return -1 * sortDirection.getFactor();
        }

        if (v2==null) {
            return sortDirection.getFactor();
        }

        return v1.compareTo(v2) * sortDirection.getFactor();
    }
}
