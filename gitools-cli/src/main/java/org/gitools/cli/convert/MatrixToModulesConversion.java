/*
 * #%L
 * gitools-cli
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
package org.gitools.cli.convert;

import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixIterator;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.model.HashModuleMap;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;


public class MatrixToModulesConversion implements ConversionDelegate {

    @NotNull
    @Override
    public Object convert(String srcFormat, Object src, String dstFormat, IProgressMonitor progressMonitor) throws Exception {

        HashModuleMap moduleMap = new HashModuleMap();

        IMatrix matrix = (IMatrix) src;
        IMatrixDimension rows = matrix.getRows();
        IMatrixDimension columns = matrix.getColumns();
        IMatrixIterator iterator = matrix.newIterator().build();
        IMatrixLayer currentLayer = matrix.getLayers().get(iterator.get(matrix.getLayers()));
        MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(currentLayer.getValueClass());

        iterator.reset( rows );
        while (iterator.next( rows )) {

            iterator.reset( columns );
            while (iterator.next( columns )) {

                double value = cast.getDoubleValue(matrix.getValue(iterator));

                if (value == 1.0) {

                    String column = iterator.get( columns );
                    String row = iterator.get( rows );
                    moduleMap.addMapping(column, row);

                }
            }
        }

        return moduleMap;
    }

}
