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
package org.gitools.matrix.sort;

import org.apache.commons.lang.ArrayUtils;
import org.gitools.label.AnnotationsPatternProvider;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.label.MatrixRowsLabelProvider;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.filter.MatrixViewLabelFilter;
import org.gitools.matrix.model.matrix.IAnnotations;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.sort.ValueSortCriteria.SortDirection;
import org.gitools.matrix.sort.mutualexclusion.MutualExclusionComparator;
import org.gitools.model.AbstractModel;
import org.gitools.utils.aggregation.IAggregator;
import org.gitools.utils.aggregation.SumAbsAggregator;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class MatrixViewSorter
{


    public static void sortByMutualExclusion(@NotNull final IMatrixView matrixView, String pattern, IAnnotations am, @NotNull List<String> values, boolean regExChecked, boolean applyToRows, boolean applyToColumns, IProgressMonitor monitor)
    {
        if (applyToRows)
        {
            sortRowsByMutualExclusion(matrixView, pattern, am, values, regExChecked, monitor);
        }

        if (applyToColumns)
        {
            sortColumnsByMutualExclusion(matrixView, pattern, am, values, regExChecked, monitor);
        }
    }

    private static void sortRowsByMutualExclusion(@NotNull final IMatrixView matrixView, String pattern, IAnnotations am, @NotNull List<String> values, boolean regExChecked, IProgressMonitor monitor)
    {

        int[] selColumns = matrixView.getColumns().getSelected(  );

        LabelProvider labelProvider = new MatrixRowsLabelProvider(matrixView);
        labelProvider = new AnnotationsPatternProvider(labelProvider, am, pattern);
        int[] visibleRows = matrixView.getRows().getVisible();
        int[] selRows = MatrixViewLabelFilter.filterLabels(labelProvider, values, regExChecked, visibleRows);


        int numRows = selRows.length;
        final Integer[] indices = new Integer[numRows];
        for (int i = 0; i < selRows.length; i++)
            indices[i] = ArrayUtils.indexOf(visibleRows, selRows[i]);

        if (selColumns == null || selColumns.length == 0)
        {
            selColumns = new int[matrixView.getColumns().size()];
            for (int i = 0; i < selColumns.length; i++)
                selColumns[i] = i;
        }


        final int[] selectedColumns = selColumns;

        Comparator<Integer> comparator = new MutualExclusionComparator(matrixView, numRows, selectedColumns, monitor);

        if (monitor.isCancelled()) {
            return;
        }

        monitor.begin("Sorting...", 0);
        Arrays.sort(indices, comparator);

        //put the chosen indices at the top of the matrix
        int[] rowIndices = new int[indices.length];
        for (int i = 0; i < indices.length; i++)
        {
            rowIndices[i] = visibleRows[indices[i]];
        }

        for (int i = 0; i < rowIndices.length; i++)
        {
            int rowPos = ArrayUtils.indexOf(visibleRows, rowIndices[i]);
            int replacingRow = rowIndices[i];
            for (int j = i; j <= rowPos; j++)
            {
                int rowToReplace = visibleRows[j];
                visibleRows[j] = replacingRow;
                replacingRow = rowToReplace;
            }
        }


        final int[] sortedVisibleRows = visibleRows;


        matrixView.getRows().setVisible(sortedVisibleRows);

        ValueSortCriteria[] criteriaArray = new ValueSortCriteria[1];
        int index = matrixView.getLayers().getTopLayerIndex();
        criteriaArray[0] = new ValueSortCriteria(index, SumAbsAggregator.INSTANCE, SortDirection.DESCENDING);

        monitor.begin("Sorting rows...", numRows);

        if (matrixView instanceof AbstractModel)
        {
            ((AbstractModel) matrixView).setQuiet(true);
        }

        for (int i = numRows - 1; i >= 0; i--)
        {
            monitor.worked(1);
            if (monitor.isCancelled()) {
                break;
            }

            int[] exclusiveRow = new int[1];
            exclusiveRow[0] = i;
            sortColumnsByValue(matrixView, null, exclusiveRow, criteriaArray);
        }

        if (matrixView instanceof AbstractModel)
        {
            ((AbstractModel) matrixView).setQuiet(false);
            matrixView.getColumns().setVisible(matrixView.getColumns().getVisible());
        }


    }

    private static void sortColumnsByMutualExclusion(IMatrixView matrixView, String pattern, IAnnotations am, List<String> values, boolean regExChecked, IProgressMonitor monitor)
    {


        throw new UnsupportedOperationException("Mutually exclusive sorting for columns is not yet implemented");
    }


    public static void sortByValue(@NotNull IMatrixView matrixView, ValueSortCriteria[] criteria, boolean applyToRows, boolean applyToColumns)
    {
        if (applyToRows)
        {
            sortRowsByValue(matrixView, matrixView.getColumns().getSelected(  ), matrixView.getRows().getSelected(  ), criteria);
        }

        if (applyToColumns)
        {
            sortColumnsByValue(matrixView, matrixView.getColumns().getSelected(  ), matrixView.getRows().getSelected(  ), criteria);
        }
    }

    private static void sortRowsByValue(@NotNull final IMatrixView matrixView, @Nullable int[] selColumns, @Nullable int[] selRows, @Nullable final ValueSortCriteria[] criteriaArray)
    {

        if (criteriaArray == null || criteriaArray.length == 0)
        {
            return;
        }

        if (selRows == null || selRows.length == 0)
        {
            selRows = new int[matrixView.getRows().size()];
            for (int i = 0; i < selRows.length; i++)
                selRows[i] = i;
        }
        int numRows = selRows.length;

        /*final Integer[] indices = new Integer[numRows];
          for (int i = 0; i < numRows; i++)
              indices[i] = i;      */

        if (selColumns == null || selColumns.length == 0)
        {
            selColumns = new int[matrixView.getColumns().size()];
            for (int i = 0; i < selColumns.length; i++)
                selColumns[i] = i;
        }

        final int[] selectedColumns = selColumns;
        final Integer[] selectedRows = ArrayUtils.toObject(selRows);
        final double[] valueBuffer = new double[selectedColumns.length];

        Comparator<Integer> comparator = new Comparator<Integer>()
        {
            @Override
            public int compare(Integer idx1, Integer idx2)
            {

                double aggr1 = 0.0;
                double aggr2 = 0.0;

                ValueSortCriteria criteria = null;
                int criteriaIndex = 0;

                while (criteriaIndex < criteriaArray.length && aggr1 == aggr2)
                {
                    criteria = criteriaArray[criteriaIndex];
                    IAggregator aggregator = criteria.getAggregator();
                    int propIndex = criteria.getAttributeIndex();

                    aggr1 = aggregateValue(matrixView, selectedColumns, idx1, propIndex, aggregator, valueBuffer);
                    aggr2 = aggregateValue(matrixView, selectedColumns, idx2, propIndex, aggregator, valueBuffer);

                    criteriaIndex++;
                }

                int res = (int) Math.signum(aggr1 - aggr2);
                return res * criteria.getDirection().getFactor();
            }

            private double aggregateValue(@NotNull IMatrixView matrixView, @NotNull int[] selectedColumns, int idx, int propIndex, @NotNull IAggregator aggregator, double[] valueBuffer)
            {

                for (int i = 0; i < selectedColumns.length; i++)
                {
                    int col = selectedColumns[i];

                    Object valueObject = matrixView.getCellValue(idx, col, propIndex);
                    valueBuffer[i] = MatrixUtils.doubleValue(valueObject);
                }

                return aggregator.aggregate(valueBuffer);
            }
        };

        Arrays.sort(selectedRows, comparator);

        final int[] visibleRows = matrixView.getRows().getVisible();
        final int[] sortedVisibleRows = new int[visibleRows.length];
        for (int i = 0; i < visibleRows.length; i++)
            sortedVisibleRows[i] = visibleRows[i];

        for (int i = 0; i < numRows; i++)
            sortedVisibleRows[selRows[i]] = visibleRows[selectedRows[i]];

        matrixView.getRows().setVisible(sortedVisibleRows);
    }

    private static void sortColumnsByValue(@NotNull final IMatrixView matrixView, @Nullable int[] selColumns, @Nullable int[] selRows, @Nullable final ValueSortCriteria[] criteriaArray)
    {

        if (criteriaArray == null || criteriaArray.length == 0)
        {
            return;
        }

        if (selColumns == null || selColumns.length == 0)
        {
            selColumns = new int[matrixView.getColumns().size()];
            for (int i = 0; i < selColumns.length; i++)
                selColumns[i] = i;
        }

        int numColumns = selColumns.length;

        if (selRows == null || selRows.length == 0)
        {
            selRows = new int[matrixView.getRows().size()];
            for (int i = 0; i < selRows.length; i++)
                selRows[i] = i;
        }

        final int[] selectedRows = selRows;
        final Integer[] selectedColumns = ArrayUtils.toObject(selColumns);
        final double[] valueBuffer = new double[selectedRows.length];

        Comparator<Integer> comparator = new Comparator<Integer>()
        {
            @Override
            public int compare(Integer idx1, Integer idx2)
            {

                double aggr1 = 0.0;
                double aggr2 = 0.0;

                ValueSortCriteria criteria = null;
                int criteriaIndex = 0;

                while (criteriaIndex < criteriaArray.length && aggr1 == aggr2)
                {
                    criteria = criteriaArray[criteriaIndex];
                    IAggregator aggregator = criteria.getAggregator();
                    int propIndex = criteria.getAttributeIndex();

                    aggr1 = aggregateValue(matrixView, selectedRows, idx1, propIndex, aggregator, valueBuffer);
                    aggr2 = aggregateValue(matrixView, selectedRows, idx2, propIndex, aggregator, valueBuffer);

                    criteriaIndex++;
                }

                int res = (int) Math.signum(aggr1 - aggr2);
                return res * criteria.getDirection().getFactor();
            }

            private double aggregateValue(@NotNull IMatrixView matrixView, @NotNull int[] selectedRows, int idx, int propIndex, @NotNull IAggregator aggregator, double[] valueBuffer)
            {

                for (int i = 0; i < selectedRows.length; i++)
                {
                    int row = selectedRows[i];

                    Object valueObject = matrixView.getCellValue(row, idx, propIndex);
                    valueBuffer[i] = MatrixUtils.doubleValue(valueObject);
                }

                return aggregator.aggregate(valueBuffer);
            }
        };

        Arrays.sort(selectedColumns, comparator);

        final int[] visibleColumns = matrixView.getColumns().getVisible();
        final int[] sortedVisibleColumns = new int[visibleColumns.length];
        for (int i = 0; i < visibleColumns.length; i++)
            sortedVisibleColumns[i] = visibleColumns[i];

        for (int i = 0; i < numColumns; i++)
            sortedVisibleColumns[selColumns[i]] = visibleColumns[selectedColumns[i]];

        matrixView.getColumns().setVisible(sortedVisibleColumns);
    }

    public static void sortByLabel(@NotNull IMatrixView matrixView, boolean sortRows, SortDirection rowsDirection, boolean rowsNumeric, boolean sortCols, SortDirection colsDirection, boolean colsNumeric)
    {

        sortByLabel(matrixView, sortRows, "${id}", null, rowsDirection, rowsNumeric, sortCols, "${id}", null, colsDirection, colsNumeric);
    }

    //TODO: sort by label with all selected properties!
    public static void sortByLabel(@NotNull IMatrixView matrixView, boolean sortRows, @NotNull String rowsPattern, IAnnotations rowsAnnMatrix, SortDirection rowsDirection, boolean rowsNumeric, boolean sortCols, @NotNull String colsPattern, IAnnotations colsAnnMatrix, SortDirection colsDirection, boolean colsNumeric)
    {

        if (sortRows)
        {
            LabelProvider labelProvider = new MatrixRowsLabelProvider(matrixView);
            if (!rowsPattern.equalsIgnoreCase("${id}"))
            {
                labelProvider = new AnnotationsPatternProvider(labelProvider, rowsAnnMatrix, rowsPattern);
            }

            matrixView.getRows().setVisible(sortLabels(labelProvider, rowsDirection, matrixView.getRows().getVisible(), rowsNumeric));
        }

        if (sortCols)
        {
            LabelProvider labelProvider = new MatrixColumnsLabelProvider(matrixView);
            if (!colsPattern.equalsIgnoreCase("${id}"))
            {
                labelProvider = new AnnotationsPatternProvider(labelProvider, colsAnnMatrix, colsPattern);
            }

            matrixView.getColumns().setVisible(sortLabels(labelProvider, colsDirection, matrixView.getColumns().getVisible(), colsNumeric));
        }
    }

    @NotNull
    private static int[] sortLabels(@NotNull final LabelProvider labelProvider, SortDirection direction, int[] visibleIndices, boolean numeric)
    {

        int count = labelProvider.getCount();
        Integer[] indices = new Integer[count];
        for (int i = 0; i < count; i++)
            indices[i] = i;

        final int dirSign = direction == SortDirection.ASCENDING ? 1 : -1;

        Comparator<Integer> comparator = new Comparator<Integer>()
        {
            @Override
            public int compare(Integer idx1, Integer idx2)
            {
                String label1 = labelProvider.getLabel(idx1);
                String label2 = labelProvider.getLabel(idx2);
                return label1.compareTo(label2) * dirSign;
            }
        };

        Comparator<Integer> numericComparator = new Comparator<Integer>()
        {
            @Override
            public int compare(Integer idx1, Integer idx2)
            {
                Double v1;
                Double v2;

                try
                {
                    v1 = Double.parseDouble(labelProvider.getLabel(idx1));
                } catch (NumberFormatException e)
                {
                    v1 = Double.NaN;
                }

                try
                {
                    v2 = Double.parseDouble(labelProvider.getLabel(idx2));
                } catch (NumberFormatException e)
                {
                    v2 = Double.NaN;
                }

                if (v1.isNaN() || v2.isNaN())
                {
                    return v1.compareTo(v2);
                }
                else
                {
                    return v1.compareTo(v2) * dirSign;
                }
            }
        };

        if (numeric)
        {
            Arrays.sort(indices, numericComparator);
        }
        else
        {
            Arrays.sort(indices, comparator);
        }

        int[] vIndices = new int[count];
        for (int i = 0; i < count; i++)
            vIndices[i] = visibleIndices[indices[i]];
        return vIndices;
    }

}