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
package org.gitools.core.exporter;

import org.gitools.utils.datafilters.ValueTranslator;
import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.IMatrixLayers;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.utils.fileutils.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class TextMatrixViewExporter {

    public static void exportMatrix(@NotNull IMatrixView matrixView, IMatrixLayer layer, File file) throws IOException {
        PrintWriter pw = new PrintWriter(IOUtils.openWriter(file));

        IMatrixDimension rows = matrixView.getRows();
        IMatrixDimension columns = matrixView.getColumns();

        //header
        for (String column : columns)
            pw.print("\t" + column);
        pw.println();

        // body
        ValueTranslator translator = layer.getTranslator();

        for (String row : rows) {
            pw.print(row);
            for (String column : columns) {
                pw.print("\t" + translator.valueToString( matrixView.get(layer, row, column) ));
            }
            pw.println();
        }

        pw.close();
    }

    public static void exportTable(IMatrixView matrixView, int[] propIndices, File file) throws IOException {

        PrintWriter pw = new PrintWriter(IOUtils.openWriter(file));

        IMatrixLayers layers = matrixView.getLayers();
        IMatrixDimension rows = matrixView.getRows();
        IMatrixDimension columns = matrixView.getColumns();

        // header
        pw.print("column\trow");
        for (int l : propIndices) {
            IMatrixLayer layer = layers.get(l);
            pw.print("\t" + layer.getId());
        }
        pw.println();

        // body
        for (String column : columns) {
            for (String row : rows) {
                pw.print(column);
                pw.print("\t" + row);
                for (int l : propIndices) {
                    IMatrixLayer layer = layers.get(l);
                    pw.print("\t" + layer.getTranslator().valueToString( matrixView.get(layer, row, column )));
                }
                pw.println();
            }
        }
        pw.close();
    }
}
