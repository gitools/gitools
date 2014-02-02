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
package org.gitools.matrix;

import org.gitools.matrix.format.TdmMatrixFormat;
import org.gitools.matrix.format.BdmMatrixFormat;
import org.gitools.matrix.format.CmatrixMatrixFormat;
import org.gitools.matrix.format.GmtMatrixFormat;
import org.gitools.matrix.modulemap.format.IxmModuleMapFormat;
import org.gitools.matrix.modulemap.format.TcmModuleMapFormat;
import org.gitools.matrix.format.CdmMatrixFormat;
import org.gitools.matrix.format.GctMatrixFormat;
import org.gitools.matrix.format.GmxMatrixFormat;
import org.gitools.api.persistence.FileFormat;

@Deprecated
public class FileFormats {

    // Data
    public static final FileFormat GENE_MATRIX = new FileFormat("Gene Matrix", GmxMatrixFormat.EXTENSION);

    public static final FileFormat GENE_MATRIX_TRANSPOSED = new FileFormat("Gene Matrix Transposed", GmtMatrixFormat.EXTENSION);

    public static final FileFormat DOUBLE_BINARY_MATRIX = new FileFormat("Binary data matrix", BdmMatrixFormat.EXTENSION);

    public static final FileFormat DOUBLE_MATRIX = new FileFormat("Continuous data matrix", CdmMatrixFormat.EXTENSION);

    public static final FileFormat GENE_CLUSTER_TEXT = new FileFormat("Gene cluster text", GctMatrixFormat.EXTENSION);

    public static final FileFormat MODULES_INDEXED_MAP = new FileFormat("Indexed mappings", IxmModuleMapFormat.EXTENSION);

    public static final FileFormat MODULES_2C_MAP = new FileFormat("Two columns mappings", TcmModuleMapFormat.EXTENSION);

    public static final FileFormat MULTIVALUE_DATA_MATRIX = new FileFormat("Multivalue data matrix", TdmMatrixFormat.EXSTENSION);

    public static final FileFormat COMPRESSED_MATRIX = new FileFormat("Compressed matrix", CmatrixMatrixFormat.EXTENSION);

    // Image
    public static final FileFormat PNG = new FileFormat("PNG", "png", true, false);

    public static final FileFormat JPG = new FileFormat("JPEG", "jpg", true, false);

    // Html
    public static final FileFormat HTML = new FileFormat("HTML", "html", true, false);

    // Text
    public static final FileFormat TEXT = new FileFormat("TEXT", "txt", true, false);
}
