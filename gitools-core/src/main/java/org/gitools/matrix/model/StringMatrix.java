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
package org.gitools.matrix.model;

import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import org.gitools.matrix.model.element.StringElementAdapter;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @noinspection ALL
 */ //TODO remove JAXB support
@XmlAccessorType(XmlAccessType.NONE)

public class StringMatrix extends ObjectMatrix
{

    private static final long serialVersionUID = 5061265701379494159L;

    StringMatrix()
    {
        super();

        setCellAdapter(new StringElementAdapter());
    }

    StringMatrix(String title, ObjectMatrix1D rows, ObjectMatrix1D columns, ObjectMatrix2D cells)
    {

        super(title, rows, columns, cells, new StringElementAdapter());
    }

    @Override
    public void makeCells()
    {
        cells = ObjectFactory2D.dense.make(rows.size(), columns.size());
    }

    @XmlTransient
    protected int internalColumnCount()
    {
        return cells.columns();
    }

    protected int internalRowCount()
    {
        return cells.rows();
    }

    @NotNull
    @Override
    public String getCell(int row, int column)
    {
        return (String) cells.get(row, column);
    }

    public void setCell(int row, int column, String cell)
    {
        cells.set(row, column, cell);
    }
}
