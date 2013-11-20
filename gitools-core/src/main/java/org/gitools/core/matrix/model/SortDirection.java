package org.gitools.core.matrix.model;

import org.apache.commons.collections.comparators.ComparableComparator;

/**
* Created with IntelliJ IDEA.
* User: jordi
* Date: 18/11/13
* Time: 12:48
* To change this template use File | Settings | File Templates.
*/
public enum SortDirection {
    ASCENDING("Ascending", 1),
    DESCENDING("Descending", -1);

    private final String title;
    private final int factor;

    SortDirection(String title, int factor) {
        this.title = title;
        this.factor = factor;
    }

    public int getFactor() {
        return factor;
    }

    public int compare(Object o1, Object o2) {
        if(o1 == o2) { return 0; }
        if(o1 == null) { return ( factor ); }
        if(o2 == null) { return ( -1 * factor ); }
        return ComparableComparator.getInstance().compare(o1, o2) * factor;
    }

    @Override
    public String toString() {
        return title;
    }
}
