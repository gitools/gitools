package org.gitools.matrix.model;

import com.jgoodies.binding.beans.Model;

public abstract class AbstractMatrixDimension extends Model implements IMatrixDimension {

    private int vectorPosition;
    private String id;

    protected AbstractMatrixDimension() {
        super();
    }

    public AbstractMatrixDimension(String id, int vectorPosition) {
        super();

        this.id = id;
        this.vectorPosition = vectorPosition;
    }

    @Override
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @Override
    public int getVectorPosition() {
        return vectorPosition;
    }

    protected void setVectorPosition(int vectorPosition) {
        this.vectorPosition = vectorPosition;
    }

    @Override
    public int getPosition(int[] matrixPosition) {
        return matrixPosition[vectorPosition];
    }

    @Override
    public void setPosition(int[] matrixPosition, int index) {
        matrixPosition[vectorPosition] = index;
    }

    @Override
    public int next(int[] matrixPosition) {
        int next = matrixPosition[vectorPosition]++;

        if (next == size()) {
            matrixPosition[vectorPosition] = 0;
            next = -1;
        } else {
            matrixPosition[vectorPosition] = next;
        }

        return next;
    }
}
