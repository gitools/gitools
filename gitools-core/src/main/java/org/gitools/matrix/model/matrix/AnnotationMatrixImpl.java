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
package org.gitools.matrix.model.matrix;

import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AnnotationMatrixImpl extends StringMatrix implements IAnnotations
{

    private static final long serialVersionUID = 2941738380629859631L;

    private Map<String, Integer> rowMap;
    private Map<String, Integer> colMap;

    public AnnotationMatrixImpl()
    {
        super();
    }

    public AnnotationMatrixImpl(String title, ObjectMatrix1D rows, ObjectMatrix1D columns, ObjectMatrix2D cells)
    {

        super(title, rows, columns, cells);

        updateRowsMap();
        updateColumnsMap();
    }

    @Override
    public void setRows(ObjectMatrix1D rows)
    {
        super.setRows(rows);
        updateRowsMap();
    }

    @Override
    public void setColumns(ObjectMatrix1D columns)
    {
        super.setColumns(columns);
        updateColumnsMap();
    }

    @Override
    public int internalRowIndex(String id)
    {
        int index = -1;
        Integer idx = rowMap.get(id);
        if (idx != null)
        {
            index = idx.intValue();
        }
        return index;
    }

    public int getIndex(String id)
    {
        int index = -1;
        Integer idx = colMap.get(id);
        if (idx != null)
        {
            index = idx.intValue();
        }
        return index;
    }

    @NotNull
    public List<Annotation> getAnnotations(String label)
    {
        List<Annotation> ann = new ArrayList<Annotation>();
        int index = internalRowIndex(label);
        if (index >= 0)
        {
            int numAnn = getColumns().size();
            for (int i = 0; i < numAnn; i++)
            {
                String value = getCellAsString(index, i);
                if (value != null)
                {
                    String key = getLabel(i);
                    ann.add(new Annotation(key, value));
                }
            }
        }
        return ann;
    }

    @Override
    public boolean hasIdentifier(String identifier)
    {
        return internalRowIndex(identifier) != -1;
    }

    @Override
    public Collection<String> getLabel()
    {
        return colMap.keySet();
    }

    @Override
    public String getAnnotation(String identifier, String annotationLabel)
    {
        int column = getIndex(annotationLabel);

        if (column == -1)
        {
            return null;
        }

        int row = internalRowIndex(identifier);

        if (row == -1)
        {
            return null;
        }

        return getCellAsString(row, column);
    }

    private void updateRowsMap()
    {
        if (rowMap == null)
        {
            rowMap = new HashMap<String, Integer>();
        }

        rowMap.clear();

        for (int i = 0; i < rows.size(); i++)
            rowMap.put(rows.getQuick(i).toString(), i);
    }

    private void updateColumnsMap()
    {
        if (colMap == null)
        {
            colMap = new HashMap<String, Integer>();
        }

        colMap.clear();

        for (int i = 0; i < columns.size(); i++)
            colMap.put(columns.getQuick(i).toString(), i);
    }
}
