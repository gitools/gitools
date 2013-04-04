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

import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class GeneMatrixTransposedFormat extends AbstractMatrixFormat<DoubleBinaryMatrix>
{

    public GeneMatrixTransposedFormat()
    {
        super(FileSuffixes.GENE_MATRIX_TRANSPOSED, DoubleBinaryMatrix.class);
    }

    @NotNull
    @Override
    protected DoubleBinaryMatrix readResource(@NotNull IResourceLocator resourceLocator, @NotNull IProgressMonitor progressMonitor) throws PersistenceException
    {

        progressMonitor.begin("Reading names ...", 1);

        DoubleBinaryMatrix matrix = new DoubleBinaryMatrix();


        try
        {

            InputStream in = resourceLocator.openInputStream();
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            String[] fields;

            List<String> colNames = new ArrayList<String>();
            List<int[]> colRowIndices = new ArrayList<int[]>();

            Map<String, Integer> rowIndices = new HashMap<String, Integer>();

            while ((fields = parser.readNext()) != null)
            {

                if (fields.length < 2)
                {
                    throw new PersistenceException("Invalid row, at least 2 columns required (name and description) at line " + parser.getLineNumber());
                }

                colNames.add(fields[0]);

                // fields[1] is for description, but currently not used

                int[] rows = new int[fields.length - 2];
                colRowIndices.add(rows);

                for (int i = 2; i < fields.length; i++)
                {
                    Integer rowIndex = rowIndices.get(fields[i]);
                    if (rowIndex == null)
                    {
                        rowIndex = rowIndices.size();
                        rowIndices.put(fields[i], rowIndex);
                    }

                    rows[i - 2] = rowIndex;
                }
            }

            // incorporate population labels

            String[] populationLabels = getPopulationLabels();
            if (populationLabels != null)
            {
                for (String name : populationLabels)
                {
                    Integer index = rowIndices.get(name);
                    if (index == null)
                    {
                        rowIndices.put(name, rowIndices.size());
                    }
                }
            }

            int numRows = rowIndices.size();
            int numColumns = colNames.size();

            matrix.makeCells(numRows, numColumns);

            // set row names

            for (Map.Entry<String, Integer> entry : rowIndices.entrySet())
                matrix.setRow(entry.getValue(), entry.getKey());

            // fill matrix with background value

            double backgroundValue = getBackgroundValue();
            for (int row = 0; row < numRows; row++)
                for (int col = 0; col < numColumns; col++)
                    matrix.setCellValue(row, col, 0, backgroundValue);

            // set column names and cell values

            int colIndex = 0;
            Iterator<String> colNameIt = colNames.iterator();
            Iterator<int[]> colRowsIt = colRowIndices.iterator();

            while (colNameIt.hasNext())
            {
                matrix.setColumn(colIndex, colNameIt.next());
                int[] rows = colRowsIt.next();
                for (int row : rows)
                    matrix.setCellValue(row, colIndex, 0, 1.0);
                colIndex++;
            }

            in.close();

            progressMonitor.info(matrix.getColumnCount() + " columns and " + matrix.getRowCount() + " rows");

            progressMonitor.end();
        } catch (IOException e)
        {
            throw new PersistenceException(e);
        }

        return matrix;
    }

    @Override
    protected void writeResource(@NotNull IResourceLocator resourceLocator, @NotNull DoubleBinaryMatrix matrix, @NotNull IProgressMonitor progressMonitor) throws PersistenceException
    {
        progressMonitor.begin("Saving matrix...", matrix.getColumnCount());

        try
        {

            OutputStream out = resourceLocator.openOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            final int rowCount = matrix.getRowCount();

            for (int ci = 0; ci < matrix.getColumnCount(); ci++)
            {
                pw.append(matrix.getColumnLabel(ci));
                pw.append('\t'); // description, but currently not used
                for (int ri = 0; ri < rowCount; ri++)
                {
                    Double value = MatrixUtils.doubleValue(matrix.getCellValue(ri, ci, 0));
                    if (value == 1.0)
                    {
                        pw.append('\t').append(matrix.getRowLabel(ri));
                    }
                }
                pw.println();
            }

            out.close();
        } catch (Exception e)
        {
            throw new PersistenceException(e);
        }

        progressMonitor.end();
    }
}
