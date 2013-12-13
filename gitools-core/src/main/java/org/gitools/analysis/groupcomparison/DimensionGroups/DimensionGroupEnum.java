package org.gitools.analysis.groupcomparison.DimensionGroups;


public enum DimensionGroupEnum {
    Free("Group freely (e.g. by Id)"),
    Annotation("Group by annotation"),
    Value("Group by value");

    private String string;

    DimensionGroupEnum(String name) {
        string = name;
    }

    @Override
    public String toString() {
        return string;
    }
}
