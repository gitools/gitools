package org.gitools.matrix.model;

public interface IMatrixViewDimension extends IMatrixDimension
{
    int[] getVisible();

    void setVisible(int[] indices);

    public void move(Direction direction, int[] indices);

    void hide(int[] indices);

    int[] getSelected();

    void setSelected(int[] indices);

    boolean isSelected(int index);

    void selectAll();

    void invertSelection();

    void clearSelection();

    int getSelectionLead();

    void setSelectionLead(int index);


}
