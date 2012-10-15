package org.gitools.analysis.groupcomparison;

import org.gitools.datafilters.BinaryCutoff;

/**
* Created by IntelliJ IDEA.
* User: mschroeder
* Date: 6/29/12
* Time: 4:18 PM
* To change this template use File | Settings | File Templates.
*/
public class ColumnGroup {

    protected String name = "";
    protected int[] columns = new int[0];
    protected BinaryCutoff binaryCutoff = null;
    protected int cutoffAttributeIndex = -1;

    public ColumnGroup(String string) {
        this.name = name;
    }

    public ColumnGroup (String name,
                            int[] columns,
                            BinaryCutoff binaryCutoff,
                            int cutoffAttributeIndex) {
        this.name = name;
        this.columns = columns;
        this.binaryCutoff = binaryCutoff;
        this.cutoffAttributeIndex = cutoffAttributeIndex;
    }

    public BinaryCutoff getBinaryCutoff() {
        return binaryCutoff;
    }

    public void setBinaryCutoff(BinaryCutoff binaryCutoff) {
        this.binaryCutoff = binaryCutoff;
    }

    public int getCutoffAttributeIndex() {
        return cutoffAttributeIndex;
    }

    public void setCutoffAttributeIndex(int cutoffAttributeIndex) {
        this.cutoffAttributeIndex = cutoffAttributeIndex;
    }

    public int[] getColumns() {
        return columns;
    }

    public void setColumns(int[] columns) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
