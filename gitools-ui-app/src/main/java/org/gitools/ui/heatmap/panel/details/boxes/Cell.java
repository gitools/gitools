/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
     * @param row    the row
     * @param column the column
     * @param values the values
     */
    public Cell(@NotNull PropertyItem row, @NotNull PropertyItem column, @NotNull Collection<PropertyItem> values)
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
    public Collection<PropertyItem> getValues()
    {
        return values;
    }

}
