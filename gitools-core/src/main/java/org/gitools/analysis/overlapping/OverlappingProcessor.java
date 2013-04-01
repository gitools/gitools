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
package org.gitools.analysis.overlapping;

import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.BeanElementAdapter;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.BitSet;
import java.util.Date;


public class OverlappingProcessor implements AnalysisProcessor
{

    private OverlappingAnalysis analysis;

    public OverlappingProcessor(OverlappingAnalysis analysis)
    {
        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException
    {
        Date startTime = new Date();

        IMatrix data = analysis.getData();

        int attrIndex = 0;
        String attrName = analysis.getAttributeName();
        if (attrName != null && !attrName.isEmpty())
        {
            attrIndex = data.getCellAttributeIndex(attrName);
        }

        if (analysis.isTransposeData())
        {
            TransposedMatrixView mt = new TransposedMatrixView();
            mt.setMatrix(data);
            data = mt;
        }

        int numRows = data.getRowCount();
        int numColumns = data.getColumnCount();

        monitor.begin("Running Overlapping analysis ...", numColumns * (numColumns - 1) / 2);

        String[] labels = new String[numColumns];
        for (int i = 0; i < numColumns; i++)
            labels[i] = data.getColumnLabel(i);

        final ObjectMatrix results = new ObjectMatrix();
        analysis.setCellResults(results);

        results.setColumns(labels);
        results.setRows(labels);
        results.makeCells();

        results.setCellAdapter(
                new BeanElementAdapter(OverlappingResult.class));

        BitSet x = new BitSet(numRows);
        BitSet xna = new BitSet(numRows);

        Double replaceNanValue = analysis.getReplaceNanValue();
        if (replaceNanValue == null)
        {
            replaceNanValue = Double.NaN;
        }

        boolean cutoffEnabled = analysis.isBinaryCutoffEnabled();
        CutoffCmp cutoffCmp = analysis.getBinaryCutoffCmp();
        Double cutoffValue = analysis.getBinaryCutoffValue();

        Class<?> valueClass = data.getCellAttributes().get(attrIndex).getValueClass();
        final MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(valueClass);

        for (int i = 0; i < numColumns && !monitor.isCancelled(); i++)
        {
            int rowCount = 0;

            for (int row = 0; row < numRows; row++)
            {
                Object value = data.getCellValue(row, i, attrIndex);
                Double v = cast.getDoubleValue(value);
                v = transformValue(v, replaceNanValue, cutoffEnabled, cutoffCmp, cutoffValue, row, i);
                if (v == 1.0)
                {
                    rowCount++;
                }

                x.set(row, v == 1.0);
                xna.set(row, Double.isNaN(v));
            }

            for (int j = i; j < numColumns && !monitor.isCancelled(); j++)
            {
                monitor.info("Overlapping " + data.getColumnLabel(i) + " with " + data.getColumnLabel(j));

                //TODO Parallelize
                {
                    int columnCount = 0;
                    int bothCount = 0;

                    for (int row = 0; row < numRows; row++)
                    {
                        double v0 = xna.get(row) ? Double.NaN : (x.get(row) ? 1.0 : 0.0);

                        Object value = data.getCellValue(row, j, attrIndex);
                        Double v1 = cast.getDoubleValue(value);
                        v1 = transformValue(v1, replaceNanValue, cutoffEnabled, cutoffCmp, cutoffValue, row, j);

                        if (v1 == 1.0)
                        {
                            columnCount++;
                        }
                        if (v0 == 1.0 && v1 == 1.0)
                        {
                            bothCount++;
                        }
                    }

                    results.setCell(i, j,
                            new OverlappingResult(rowCount, columnCount, bothCount));
                }

                monitor.worked(1);
            }
        }

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

        monitor.end();
    }

    private Double transformValue(
            Double v, double replaceNanValue,
            boolean binaryCutoffEnabled,
            CutoffCmp cutoffCmp, Double cutoffValue,
            int row, int column) throws AnalysisException
    {

        boolean isNaN = v != null ? Double.isNaN(v) : true;

        if (isNaN)
        {
            v = replaceNanValue;
        }

        if (!isNaN && binaryCutoffEnabled)
        {
            v = cutoffCmp.compare(v, cutoffValue) ? 1.0 : 0.0;
        }

        if (!isNaN && v != 1.0 && v != 0.0)
        {
            throw new AnalysisException("Not binary value found at row " + row + " column " + column);
        }

        return v;
    }
}
