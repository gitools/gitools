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

import org.gitools.matrix.model.matrix.BaseMatrix;
import org.gitools.matrix.model.matrix.DoubleBinaryMatrix;
import org.gitools.matrix.model.matrix.DoubleMatrix;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MatrixConversion implements ConversionDelegate {

    @Nullable
    @Override
    public Object convert(String srcFormat, @NotNull Object src, String dstFormat, @NotNull IProgressMonitor progressMonitor) throws Exception {
        BaseMatrix srcMatrix = (BaseMatrix) src;

        BaseMatrix dstMatrix = null;

        Class<? extends BaseMatrix> cls = null;

        if (MimeTypes.DOUBLE_MATRIX.equals(dstFormat)) {
            cls = DoubleMatrix.class;
        } else if (MimeTypes.DOUBLE_BINARY_MATRIX.equals(dstFormat) || MimeTypes.GENE_MATRIX.equals(dstFormat) || MimeTypes.GENE_MATRIX_TRANSPOSED.equals(dstFormat)) {
            cls = DoubleBinaryMatrix.class;
        } else {
            throw new Exception("Unsupported mime type for destination matrix: " + dstFormat);
        }

        int numCols = srcMatrix.getColumns().size();
        int numRows = srcMatrix.getRows().size();

        progressMonitor.begin("Converting matrix ...", numRows);

        if (!cls.equals(src.getClass())) {
            dstMatrix = cls.newInstance();
            dstMatrix.makeCells(numRows, numCols);
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    Object value = srcMatrix.getValue(row, col, 0);
                    dstMatrix.setValue(row, col, 0, value);
                }

                progressMonitor.worked(1);
            }

            dstMatrix.setColumns(srcMatrix.getInternalColumns());
            dstMatrix.setRows(srcMatrix.getInternalRows());
        } else {
            dstMatrix = srcMatrix;
        }

        progressMonitor.end();

        return dstMatrix;
    }

}
