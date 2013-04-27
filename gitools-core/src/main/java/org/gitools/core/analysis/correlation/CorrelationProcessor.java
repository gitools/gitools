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
package org.gitools.core.analysis.correlation;

import org.gitools.core.analysis.AnalysisException;
import org.gitools.core.analysis.AnalysisProcessor;
import org.gitools.core.analysis.MethodException;
import org.gitools.core.analysis.correlation.methods.CorrelationMethodFactory;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.core.matrix.TransposedMatrixView;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.matrix.ObjectMatrix;
import org.gitools.core.matrix.model.matrix.element.BeanElementAdapter;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class CorrelationProcessor implements AnalysisProcessor {

    private final CorrelationAnalysis analysis;

    public CorrelationProcessor(CorrelationAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(@NotNull IProgressMonitor monitor) throws AnalysisException {

        Date startTime = new Date();

        CorrelationMethod method = CorrelationMethodFactory.createMethod(analysis.getMethod(), analysis.getMethodProperties());

        IMatrix data = analysis.getData().get();
        int attributeIndex = analysis.getAttributeIndex();

        if (analysis.isTransposeData()) {
            TransposedMatrixView mt = new TransposedMatrixView();
            mt.setMatrix(data);
            data = mt;
        }

        int numRows = data.getRows().size();
        int numColumns = data.getColumns().size();

        monitor.begin("Running correlation analysis ...", numColumns * (numColumns - 1) / 2);

        String[] labels = new String[numColumns];
        for (int i = 0; i < numColumns; i++)
            labels[i] = data.getColumns().getLabel(i);

        final ObjectMatrix results = new ObjectMatrix();
        analysis.setResults(new ResourceReference<IMatrix>("results", results));

        results.setColumns(labels);
        results.setRows(labels);
        results.makeCells();

        results.setObjectCellAdapter(new BeanElementAdapter(method.getResultClass()));

        double[] x = new double[numRows];
        double[] y = new double[numRows];
        int[] indices = new int[numRows];

        Double replaceNanValue = analysis.getReplaceNanValue();
        if (replaceNanValue == null) {
            replaceNanValue = Double.NaN;
        }

        Class<?> valueClass = data.getLayers().get(attributeIndex).getValueClass();
        final MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(valueClass);

        for (int i = 0; i < numColumns && !monitor.isCancelled(); i++) {
            for (int row = 0; row < numRows; row++) {
                Object value = data.getValue(row, i, attributeIndex);
                Double v = cast.getDoubleValue(value);
                if (v == null || Double.isNaN(v)) {
                    v = replaceNanValue;
                }
                x[row] = v;
            }

            for (int j = i; j < numColumns && !monitor.isCancelled(); j++) {
                monitor.info("Correlating " + data.getColumns().getLabel(i) + " with " + data.getColumns().getLabel(j));

                int numPairs = 0;
                for (int row = 0; row < numRows; row++) {
                    double v0 = x[row];

                    Object value = data.getValue(row, j, attributeIndex);

                    Double v1 = cast.getDoubleValue(value);
                    if (v1 == null || Double.isNaN(v1)) {
                        v1 = replaceNanValue;
                    }

                    if (!Double.isNaN(v0) && !Double.isNaN(v1)) {
                        y[row] = v1;

                        indices[numPairs++] = row;
                    }
                }

                try {
                    results.setCell(i, j, method.correlation(x, y, indices, numPairs));
                } catch (MethodException ex) {
                    throw new AnalysisException(ex);
                }

                monitor.worked(1);
            }
        }

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

        monitor.end();
    }
}