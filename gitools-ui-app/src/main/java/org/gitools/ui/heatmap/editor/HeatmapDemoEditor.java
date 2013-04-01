/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.heatmap.editor;

import cern.colt.matrix.*;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.matrix.model.MatrixView;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.ArrayElementAdapter;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;

public class HeatmapDemoEditor extends HeatmapEditor
{

    private static final long serialVersionUID = 2467164492764056062L;

    public HeatmapDemoEditor(int rows, int cols)
    {
        super(createModel(rows, cols));

        setName("Demo");
    }

    private static Heatmap createModel(int rows, int cols)
    {
        MatrixView matrixView = createTable(rows, cols);
        ElementDecorator decorator = ElementDecoratorFactory.create(
                ElementDecoratorNames.PVALUE, matrixView.getCellAdapter());

        return new Heatmap(matrixView, decorator,
                new HeatmapTextLabelsHeader(),
                new HeatmapTextLabelsHeader());
    }

    private static MatrixView createTable(int rows, int cols)
    {
        int k = 0;
        DoubleMatrix1D values = DoubleFactory1D.dense.random(2 * rows * cols);

        ObjectMatrix2D data = ObjectFactory2D.dense.make(rows, cols);
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                double[] v = new double[]{
                        values.getQuick(k++),
                        values.getQuick(k++)
                };
                data.setQuick(row, col, v);
            }
        }

        final ObjectMatrix1D rowNames = ObjectFactory1D.dense.make(data.rows());
        for (int i = 0; i < rowNames.size(); i++)
            rowNames.setQuick(i, "row " + (i + 1));

        final ObjectMatrix1D colNames = ObjectFactory1D.dense.make(data.columns());
        for (int i = 0; i < colNames.size(); i++)
            colNames.setQuick(i, "col " + (i + 1));

        ObjectMatrix resultsMatrix = new ObjectMatrix(
                "Demo",
                rowNames,
                colNames,
                data,
                new ArrayElementAdapter(new String[]{"p-value", "corrected-p-value"}));

        HtestAnalysis analysis = new HtestAnalysis();
        analysis.setResults(resultsMatrix);

        return new MatrixView(resultsMatrix);
    }
}
