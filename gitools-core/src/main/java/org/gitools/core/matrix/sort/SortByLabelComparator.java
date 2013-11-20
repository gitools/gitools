package org.gitools.core.matrix.sort;


import com.google.common.base.Function;
import org.gitools.core.matrix.model.SortDirection;

import java.util.Comparator;

public class SortByLabelComparator implements Comparator<String> {

    private SortDirection direction;
    private Function<String, String> transformFunction;
    private boolean asNumeric;

    public SortByLabelComparator(SortDirection direction, Function<String, String> transformFunction, boolean asNumeric) {
        this.direction = direction;
        this.transformFunction =transformFunction;
        this.asNumeric = asNumeric;
    }

    @Override
    public int compare(String o1, String o2) {

        String v1 = transformFunction.apply(o1);
        String v2 = transformFunction.apply(o2);

        if (asNumeric) {
            return direction.compare(Double.valueOf(v1), Double.valueOf(v2));
        }

        return direction.compare(v1, v2);
    }
}
