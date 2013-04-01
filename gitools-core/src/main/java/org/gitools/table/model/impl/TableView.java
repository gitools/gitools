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
import org.gitools.model.xml.IndexArrayXmlAdapter;
import org.gitools.table.model.ITable;
import org.gitools.table.model.ITableColumn;
import org.gitools.table.model.Table;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"contents", "visibleRows", "visibleColumns"})
public class TableView implements ITable, Serializable
{

    private static final long serialVersionUID = -3231844654295236093L;

    @XmlElement(type = Table.class)
    ITable contents;

    @XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
    private int[] visibleRows;

    @XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
    private int[] visibleColumns;

    public TableView()
    {
        visibleRows = new int[0];
        visibleColumns = new int[0];
    }

    public TableView(ITable table)
    {
        this.contents = table;
        visibleRows = new int[0];
        visibleColumns = new int[0];
    }

    @Override
    public IElementAdapter getCellColumnAdapter(int column)
    {
        return contents.getCellColumnAdapter(column);
    }

    @Override
    public ITableColumn getColumn(int index)
    {
        return contents.getColumn(index);

    }

    @Override
    public int getColumnCount()
    {
        return contents.getColumnCount();
    }

    @Override
    public String getHeader(int column)
    {
        return contents.getHeader(column);
    }

    @Override
    public int getRowCount()
    {
        return contents.getRowCount();
    }

    @Override
    public Object getValue(int row, int column)
    {
        return contents.getValue(row, column);
    }

    // getters and setters

    public ITable getContents()
    {
        return contents;
    }

    public void setContents(ITable contents)
    {
        this.contents = contents;
    }

    public int[] getVisibleRows()
    {
        return visibleRows;
    }

    public void setVisibleRows(int[] visibleRows)
    {
        this.visibleRows = visibleRows;
    }

    public int[] getVisibleColumns()
    {
        return visibleColumns;
    }

    public void setVisibleColumns(int[] visibleColumns)
    {
        this.visibleColumns = visibleColumns;
    }

    // FIXME:
    //	this must be common to MatrixView and TableView

    // Marshal and unMarshall methods
    // Marshalling
    public void beforeMarshal(Marshaller u)
    {

        boolean naturalOrder = true;
        int rows = visibleRows.length;
        int columns = visibleColumns.length;
        int maxSize = rows > columns ? rows : columns;

        int i = 0;
        while (i < maxSize && naturalOrder)
        {
            if (i < columns)
            {
                naturalOrder = i == visibleColumns[i];
            }
            if (i < rows)
            {
                naturalOrder = (i == visibleRows[i] && naturalOrder);
            }
            i++;
        }

        if (naturalOrder)
        {
            visibleColumns = null;
            visibleRows = null;
        }
    }

    public void afterMarshal(Marshaller u)
    {

        if (visibleColumns == null && visibleRows == null)
        {

            int count = contents.getRowCount();
            int[] rows = new int[count];

            for (int i = 0; i < count; i++)
                rows[i] = i;
            setVisibleRows(rows);

            count = contents.getColumnCount();
            int[] columns = new int[count];

            for (int i = 0; i < count; i++)
                columns[i] = i;
            setVisibleColumns(columns);
        }

    }

    // UnMarshalling
    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        int count = 0;
        int[] rows;
        int[] columns;

        if (visibleRows.length == 0)
        {
            count = contents.getRowCount();
            rows = new int[count];

            for (int i = 0; i < count; i++)
                rows[i] = i;
            setVisibleRows(rows);
        }
        if (visibleColumns.length == 0)
        {
            count = contents.getColumnCount();
            columns = new int[count];

            for (int i = 0; i < count; i++)
                columns[i] = i;
            setVisibleColumns(columns);
        }
    }
}
