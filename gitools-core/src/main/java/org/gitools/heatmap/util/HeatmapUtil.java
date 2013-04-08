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
package org.gitools.heatmap.util;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.*;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.jetbrains.annotations.NotNull;


public class HeatmapUtil
{

    @NotNull
    public static Heatmap createFromMatrixView(@NotNull IMatrixView matrixView)
    {
        final Heatmap figure = new Heatmap(matrixView);
        final int propertiesNb = figure.getMatrixView().getCellAdapter().getPropertyCount();

        while (matrixView.getContents() instanceof MatrixView)
            matrixView = (IMatrixView) matrixView.getContents();
        final IMatrix matrix = matrixView.getContents();

        if (matrix instanceof DoubleBinaryMatrix)
        {
            BinaryElementDecorator[] decorators = new BinaryElementDecorator[propertiesNb];
            for (int i = 0; i < decorators.length; i++)
            {
                BinaryElementDecorator decorator = (BinaryElementDecorator) ElementDecoratorFactory.create(ElementDecoratorNames.BINARY, matrix.getCellAdapter());
                decorator.setValueIndex(i);
                decorator.setCutoff(1.0);
                decorator.setCutoffCmp(CutoffCmp.EQ);
                decorators[i] = decorator;
            }
            figure.setCellDecorators(decorators);
            figure.getRowDim().setGridEnabled(false);
            figure.getColumnDim().setGridEnabled(false);
        }
        else if (matrix instanceof DoubleMatrix)
        {
            figure.getRowDim().setGridEnabled(false);
            figure.getColumnDim().setGridEnabled(false);
        }

        return figure;
    }
}
