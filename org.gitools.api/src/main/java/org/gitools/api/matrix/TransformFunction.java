package org.gitools.api.matrix;



public abstract class TransformFunction  extends AbstractMatrixFunction<Double, Double>{

    protected String name;
    protected String description;
    protected IMatrixPosition position;

    public TransformFunction(String name) {
        this(name, "");
    }

    public TransformFunction(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public IMatrixPosition getPosition() {
        return position;
    }


    @Override
    public String toString() {
        return getName();
    }

}
