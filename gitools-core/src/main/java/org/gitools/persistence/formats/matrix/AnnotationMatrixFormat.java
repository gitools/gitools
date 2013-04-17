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
package org.gitools.persistence.formats.matrix;

import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import org.gitools.matrix.model.matrix.AnnotationMatrixImpl;
import org.gitools.matrix.model.matrix.IAnnotations;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence.formats.AbstractResourceFormat;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AnnotationMatrixFormat extends AbstractResourceFormat<IAnnotations>
{


    public AnnotationMatrixFormat()
    {
        super(FileSuffixes.ANNOTATION_MATRIX, IAnnotations.class);
    }

    @NotNull
    @Override
    protected IAnnotations readResource(@NotNull IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException
    {

        AnnotationMatrixImpl matrix = new AnnotationMatrixImpl();

        try
        {
            InputStream in = resourceLocator.openInputStream();
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            // header
            String[] hdr = parser.readNext();
            int numColumns = hdr.length - 1;
            ObjectMatrix1D columns = ObjectFactory1D.dense.make(numColumns);
            for (int i = 0; i < numColumns; i++)
                columns.set(i, hdr[i + 1]);
            matrix.setColumns(columns);

            // body
            List<String> rawData = new ArrayList<String>();
            String[] fields;
            while ((fields = parser.readNext()) != null)
            {
                if (fields.length > hdr.length)
                {
                    throw new PersistenceException("Number of fields greater than number of header fields at line " + parser.getLineNumber());
                }

                for (int i = 0; i < fields.length; i++)
                    rawData.add(fields[i]);

                for (int i = 0; i < (hdr.length - fields.length); i++)
                    rawData.add(new String(""));
            }

            int numRows = rawData.size() / hdr.length;
            ObjectMatrix1D rows = ObjectFactory1D.dense.make(numRows);
            ObjectMatrix2D data = ObjectFactory2D.dense.make(numRows, numColumns);
            int offs = 0;
            for (int row = 0; row < numRows; row++)
            {
                rows.setQuick(row, rawData.get(offs++));
                for (int col = 0; col < numColumns; col++)
                    data.setQuick(row, col, rawData.get(offs++));
            }

            matrix.setRows(rows);
            matrix.setCells(data);

            rawData.clear();
            in.close();
        } catch (Exception e)
        {
            throw new PersistenceException(e);
        }

        return matrix;
    }

}
