package org.gitools.matrix.model;

public interface IMatrixDimension
{
    /**
     * Total number of items in this dimension
     *
     * @return the dimension count
     */
    int size();

    /**
     * Gets the identifier label at the given position
     *
     * @param index the index
     * @return the label
     */
    String getLabel(int index);

    /**
     * Gets the index position of this identifier
     *
     * @param label the label
     * @return the index
     */
    int getIndex(String label);
}
