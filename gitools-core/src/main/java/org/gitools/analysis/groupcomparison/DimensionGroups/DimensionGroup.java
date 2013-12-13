package org.gitools.analysis.groupcomparison.DimensionGroups;

import org.gitools.api.matrix.position.IMatrixPredicate;


public abstract class DimensionGroup {

    protected String name = "";
    protected DimensionGroupEnum groupType;
    protected IMatrixPredicate predicate;


    abstract public String getProperty();

    public IMatrixPredicate getPredicate() {
        return predicate;
    }

    public DimensionGroup(String name, IMatrixPredicate predicate, DimensionGroupEnum groupType) {
        this.name = name;
        this.predicate = predicate;
        this.groupType = groupType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
