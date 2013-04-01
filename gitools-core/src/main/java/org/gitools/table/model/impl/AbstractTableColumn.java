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

import org.gitools.table.model.Table;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

@XmlSeeAlso({
        AnnotationTableColumn.class,
        MatrixCellTableColumn.class,
        MatrixPropertyTableColumn.class})

@XmlAccessorType(XmlAccessType.FIELD)
public class AbstractTableColumn
{
    // internal column,
    // reference of a Table or Matrix column
    protected int column;

    @XmlTransient
    protected Table table;

    public AbstractTableColumn()
    {
        super();
    }

    public AbstractTableColumn(int column, Table table)
    {
        this.column = column;
        this.table = table;
    }

    public void setColumn(int column)
    {
        this.column = column;
    }

    public int getColumn()
    {
        return column;
    }

    public Table getTable()
    {
        return table;
    }

    public void setTable(Table table)
    {
        this.table = table;
    }
}
