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
package org.gitools.core.analysis.groupcomparison;

import org.apache.commons.lang.ArrayUtils;
import org.gitools.core.analysis.AnalysisException;
import org.gitools.core.analysis.htest.HtestProcessor;
import org.gitools.core.datafilters.BinaryCutoff;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.core.matrix.TransposedMatrixView;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.matrix.ObjectMatrix;
import org.gitools.core.matrix.model.matrix.element.BeanElementAdapter;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.core.stats.mtc.MTC;
import org.gitools.core.stats.test.MannWhitneyWilxoxonTest;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GroupComparisonProcessor extends HtestProcessor {

    private final GroupComparisonAnalysis analysis;

    public GroupComparisonProcessor(GroupComparisonAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(@NotNull IProgressMonitor monitor) throws AnalysisException {

        Date startTime = new Date();

        // Prepare data
        IMatrix data = analysis.getData().get();
        if (analysis.isTransposeData()) {
            data = new TransposedMatrixView(data);
        }

        final int numRows = data.getRows().size();

        // Prepare results matrix
        final ObjectMatrix resultsMatrix = new ObjectMatrix();

        String[] columnLabels = new String[1];
        columnLabels[0] = analysis.getTest().getName();
        String[] rlabels = new String[numRows];
        for (int i = 0; i < numRows; i++)
            rlabels[i] = data.getRows().getLabel(i);

        resultsMatrix.setColumns(columnLabels);
        resultsMatrix.setRows(rlabels);
        resultsMatrix.makeCells();

        resultsMatrix.setObjectCellAdapter(new BeanElementAdapter(GroupComparisonResult.class));

        monitor.begin("Running group comparison analysis ...", numRows);

        int attrIndex = analysis.getAttributeIndex();

        Class<?> valueClass = data.getLayers().get(attrIndex).getValueClass();
        final MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(valueClass);


        for (int column = 0; column < columnLabels.length; column++) {
            for (int row = 0; row < numRows; row++) {

                int[] group1 = getColumnIndices(data, analysis.getGroups1(), row);
                int[] group2 = getColumnIndices(data, analysis.getGroups2(), row);


                double[] groupVals1 = new double[group1.length];
                double[] groupVals2 = new double[group2.length];
                for (int gi = 0; gi < group1.length; gi++) {
                    Object value = data.getValue(row, group1[gi], attrIndex);
                    Double v = cast.getDoubleValue(value);
                    if (v == null || Double.isNaN(v)) {
                        v = Double.NaN;
                    }
                    groupVals1[gi] = v;
                }
                for (int gi = 0; gi < group2.length; gi++) {
                    Object value = data.getValue(row, group2[gi], attrIndex);
                    Double v = cast.getDoubleValue(value);
                    if (v == null || Double.isNaN(v)) {
                        v = Double.NaN;
                    }
                    groupVals2[gi] = v;
                }

                MannWhitneyWilxoxonTest test = (MannWhitneyWilxoxonTest) analysis.getTest();
                GroupComparisonResult r = test.processTest(groupVals1, groupVals2);

                resultsMatrix.setCell(row, column, r);

                monitor.worked(1);
            }
        }

        analysis.setResults(new ResourceReference<IMatrix>("results", resultsMatrix));
        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

		/* Multiple test correction */
        MTC mtc = analysis.getMtc();

        multipleTestCorrection(resultsMatrix, mtc, monitor.subtask());

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

        analysis.setResults(new ResourceReference<IMatrix>("results", resultsMatrix));

        monitor.end();

    }

    private int[] getColumnIndices(@NotNull IMatrix data, @NotNull ColumnGroup group, int row) {
        if (group.getColumns() != null && group.getColumns().length > 0) {
            return group.getColumns();
        }

        int attrIndex = group.getCutoffAttributeIndex();
        Class<?> valueClass = data.getLayers().get(attrIndex).getValueClass();
        final MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(valueClass);
        //TODO: place the cast in a different place: inside Group??

        BinaryCutoff binaryCutoff = group.getBinaryCutoff();

        List<Integer> columnIndicesList = new ArrayList<Integer>();
        for (int col = 0; col < data.getColumns().size(); col++) {
            Object value = data.getValue(row, col, attrIndex);
            Double v = cast.getDoubleValue(value);
            if (v == null || Double.isNaN(v)) {
                continue;
            } else {
                double compliesCutoff = binaryCutoff.apply(v);
                if (compliesCutoff == 1.0) {
                    columnIndicesList.add(col);
                }
            }
        }

        Integer[] columnIndices = new Integer[columnIndicesList.size()];
        return ArrayUtils.toPrimitive(columnIndicesList.toArray(columnIndices));
    }

}
