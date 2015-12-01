package org.gitools.api.matrix;



public abstract class TransformFunction  extends AbstractMatrixFunction<Double, Double>{
    //abstract IMatrixPosition getPosition();

    protected String name;
    protected String description;
    protected IMatrixPosition position;

    public TransformFunction(String name) {
        this.name = name;
        this.description = "";
    }

    public void init() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public IMatrixPosition getPosition() {
        return position;
    }


    @Override
    public String toString() {
        return name;
    }

}
