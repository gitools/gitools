/*
 * #%L
 * gitools-core
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
package org.gitools.table.model.impl;

import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.table.model.ITableColumn;
import org.gitools.table.model.Table;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixCellTableColumn
        extends AbstractTableColumn
        implements ITableColumn, Serializable
{

    private static final long serialVersionUID = -5968245911627777748L;

    public MatrixCellTableColumn()
    {
    }

    public MatrixCellTableColumn(int column, Table table)
    {
        super(column, table);

    }

    @Override
    public IElementAdapter getAdapter()
    {
        return table.getMatrix().getCellAdapter();
    }

    @Override
    public String getHeader()
    {
        Object object = table.getMatrix().getColumnLabel(column);
        return object == null ? "null" : object.toString();
    }

    @Override
    public int getRowCount()
    {
        return table.getMatrix().getRowCount();
    }

    @Override
    public Object getValue(int row)
    {
        return table.getMatrix().getCell(row, column);

    }
}
