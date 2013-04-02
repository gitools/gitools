package org.gitools.ui.heatmap.panel.details.boxes;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;

/**
 * Class to represent the content of one matrix cell
 */
public class Cell implements Serializable
{

    private PropertyItem row;
    private PropertyItem column;
    private Collection<PropertyItem> values;

    /**
     * Instantiates a new Cell.
     *
     * @param row the row
     * @param column the column
     * @param values the values
     */
    public Cell(@NotNull PropertyItem row,@NotNull PropertyItem column,@NotNull Collection<PropertyItem> values)
    {
        this.row = row;
        this.column = column;
        this.values = values;
    }

    /**
     * Gets column.
     *
     * @return the column
     */
    public PropertyItem getColumn()
    {
        return column;
    }

    /**
     * Gets row.
     *
     * @return the row
     */
    public PropertyItem getRow()
    {
        return row;
    }

    /**
     * Gets values.
     *
     * @return the values
     */
    public Collection<PropertyItem> getValues() {
        return values;
    }

}
