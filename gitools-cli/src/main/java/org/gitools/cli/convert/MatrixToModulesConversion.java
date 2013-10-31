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
import org.gitools.core.model.HashModuleMap;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;


public class MatrixToModulesConversion implements ConversionDelegate {

    @NotNull
    @Override
    public Object convert(String srcFormat, Object src, String dstFormat, IProgressMonitor progressMonitor) throws Exception {
        final int attrIndex = 0; // TODO get from configuration

        IMatrix matrix = (IMatrix) src;

        HashModuleMap moduleMap = new HashModuleMap();

        MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(matrix.getLayers().get(attrIndex).getValueClass());

        int numRows = matrix.getRows().size();
        int numCols = matrix.getColumns().size();
        for (int row = 0; row < numRows; row++) {
            String rowLabel = matrix.getRows().getLabel(row);

            for (int col = 0; col < numCols; col++) {
                String colLabel = matrix.getColumns().getLabel(col);
                Object cellValue = matrix.getValue(row, col, attrIndex);
                double value = cast.getDoubleValue(cellValue);
                if (value == 1.0) {
                    moduleMap.addMapping(colLabel, rowLabel);
                }
            }
        }

        return moduleMap;
    }

}
