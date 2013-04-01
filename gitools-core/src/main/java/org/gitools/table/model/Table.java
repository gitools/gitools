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
package org.gitools.table.model;

import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.Matrix;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.Artifact;
import org.gitools.model.xml.TableColumnXmlAdapter;
import org.gitools.persistence.formats.analysis.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.table.model.impl.AbstractTableColumn;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Table extends Artifact implements ITable, Serializable
{

    private int rowCount = 0;

    @XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
    private IMatrix matrix;

    @XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
    private AnnotationMatrix annotations;

    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    @XmlJavaTypeAdapter(TableColumnXmlAdapter.class)
    private List<ITableColumn> columns;

    public Table()
    {
        columns = new ArrayList<ITableColumn>();
    }

    public Table(Matrix matrix, AnnotationMatrix annotations)
    {
        rowCount = matrix.getRowCount();
        columns = new ArrayList<ITableColumn>();
        this.matrix = matrix;
        this.annotations = annotations;
    }

    public void addColumn(ITableColumn column)
    {
        columns.add(column);
    }

    @Override
    public IElementAdapter getCellColumnAdapter(int column)
    {
        return columns.get(column).getAdapter();
    }

    @Override
    public ITableColumn getColumn(int index)
    {
        return columns.get(index);
    }

    @Override
    public int getColumnCount()
    {
        return columns.size();
    }

    @Override
    public String getHeader(int column)
    {
        return columns.get(column).getHeader();
    }

    @Override
    public Object getValue(int row, int column)
    {
        return columns.get(column).getValue(row);
    }

    @Override
    public int getRowCount()
    {
        return rowCount;
    }

    public void removeColumn(int index)
    {
        columns.remove(index);

    }

    public void setAnnotations(AnnotationMatrix annotations)
    {
        this.annotations = annotations;
    }

    public AnnotationMatrix getAnnotations()
    {
        return annotations;
    }

    public void setMatrix(Matrix matrix)
    {
        this.matrix = matrix;
    }

    public IMatrix getMatrix()
    {
        return matrix;
    }

    public void setRowCount(int rowCount)
    {
        this.rowCount = rowCount;
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        for (int i = 0; i < columns.size(); i++)
        {
            AbstractTableColumn col = (AbstractTableColumn) columns.get(i);
            col.setTable(this);
        }
    }
}
