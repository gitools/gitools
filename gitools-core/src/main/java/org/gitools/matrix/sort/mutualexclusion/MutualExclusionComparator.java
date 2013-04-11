package org.gitools.matrix.sort.mutualexclusion;


import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.sort.ValueSortCriteria;
import org.gitools.utils.aggregation.SumAbsAggregator;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MutualExclusionComparator implements Comparator<Integer>
{

    private final IMatrixView matrixView;
    private final int[] selectedColumns;
    private final double[] valueBuffer;
    private final Map<Integer, Double> aggregationCache;

    public MutualExclusionComparator(IMatrixView matrixView, int numRows, int[] selectedColumns, IProgressMonitor monitor)
    {
        this.matrixView = matrixView;
        this.selectedColumns = selectedColumns;
        this.valueBuffer = new double[selectedColumns.length];

        this.aggregationCache = new HashMap<Integer, Double>(numRows);

        monitor.begin("Aggregating values...", numRows);
        for (int i=0; i < numRows; i++)
        {
            this.aggregationCache.put(i, aggregateValue(i));
            monitor.worked(1);
            if (monitor.isCancelled())
            {
                return;
            }
        }
    }

    @Override
    public int compare(Integer idx1, Integer idx2)
    {
        double value1 = aggregationCache.get(idx1);
        double value2 = aggregationCache.get(idx2);

        int res = (int) Math.signum(value1 - value2);
        return res * ValueSortCriteria.SortDirection.DESCENDING.getFactor();
    }

    private double aggregateValue( int idx )
    {

        for (int i = 0; i < selectedColumns.length; i++)
        {
            int col = selectedColumns[i];

            Object valueObject = matrixView.getCellValue(idx, col, matrixView.getSelectedPropertyIndex());
            valueBuffer[i] = MatrixUtils.doubleValue(valueObject);
        }

        return SumAbsAggregator.INSTANCE.aggregate(valueBuffer);
    }
}
