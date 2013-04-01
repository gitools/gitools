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
package org.gitools.persistence._DEPRECATED;

@Deprecated
public class FileFormats
{

    // Analyses

    public static final FileFormat ENRICHMENT = new FileFormat(
            "Enrichment analysis", FileSuffixes.ENRICHMENT, MimeTypes.ENRICHMENT_ANALYSIS);

    public static final FileFormat ONCODRIVE = new FileFormat(
            "Oncodrive analysis", FileSuffixes.ONCODRIVE, MimeTypes.ONCODRIVE_ANALYSIS);

    public static final FileFormat CORRELATIONS = new FileFormat(
            "Correlations analysis", FileSuffixes.CORRELATIONS, MimeTypes.CORRELATIONS_ANALYSIS);

    public static final FileFormat COMBINATION = new FileFormat(
            "Combination analysis", FileSuffixes.COMBINATION, MimeTypes.COMBINATION_ANALYSIS);

    public static final FileFormat OVERLAPPING = new FileFormat(
            "Overlapping analysis", FileSuffixes.OVERLAPPING, MimeTypes.OVERLAPPING_ANALYSIS);

    public static final FileFormat GROUP_COMPARISON = new FileFormat(
            "Overlapping analysis", FileSuffixes.GROUP_COMPARISON, MimeTypes.GROUPCOMPARISON_ANALYSIS);

    // Data

    public static final FileFormat GENE_MATRIX = new FileFormat(
            "Gene Matrix", FileSuffixes.GENE_MATRIX, MimeTypes.GENE_MATRIX);

    public static final FileFormat GENE_MATRIX_TRANSPOSED = new FileFormat(
            "Gene Matrix Transposed", FileSuffixes.GENE_MATRIX_TRANSPOSED, MimeTypes.GENE_MATRIX_TRANSPOSED);

    public static final FileFormat DOUBLE_BINARY_MATRIX = new FileFormat(
            "Binary data matrix", FileSuffixes.DOUBLE_BINARY_MATRIX, MimeTypes.DOUBLE_BINARY_MATRIX);

    public static final FileFormat DOUBLE_MATRIX = new FileFormat(
            "Continuous data matrix", FileSuffixes.DOUBLE_MATRIX, MimeTypes.DOUBLE_MATRIX);

    public static final FileFormat GENE_CLUSTER_TEXT = new FileFormat(
            "Gene cluster text", FileSuffixes.GENE_CLUSTER_TEXT, MimeTypes.DOUBLE_MATRIX);

    public static final FileFormat MODULES_INDEXED_MAP = new FileFormat(
            "Indexed mappings", FileSuffixes.MODULES_INDEXED_MAP, MimeTypes.MODULES_INDEXED_MAP);

    public static final FileFormat MODULES_2C_MAP = new FileFormat(
            "Two columns mappings", FileSuffixes.MODULES_2C_MAP, MimeTypes.MODULES_2C_MAP);

    public static final FileFormat MODULES_2C_MAP_COMPRESSED = new FileFormat(
            FileFormats.MODULES_2C_MAP.getTitle() + " compressed", FileFormats.MODULES_2C_MAP.getExtension() + ".gz", MimeTypes.MODULES_2C_MAP, true, false);

    public static final FileFormat MULTIVALUE_DATA_MATRIX = new FileFormat(
            "Multivalue data matrix", FileSuffixes.OBJECT_MATRIX, MimeTypes.OBJECT_MATRIX);

    // Heatmap

    public static final FileFormat HEATMAP = new FileFormat(
            "Heatmap", FileSuffixes.HEATMAP, MimeTypes.HEATMAP);

    // Image

    public static final FileFormat PNG = new FileFormat(
            "PNG", "png", "image/png", true, false);

    public static final FileFormat JPG = new FileFormat(
            "JPEG", "jpg", "image/jpeg", true, false);

    public static final FileFormat PDF = new FileFormat(
            "PDF", "pdf", "application/pdf", true, false);

    // Html

    public static final FileFormat HTML = new FileFormat(
            "HTML", "html", "text/html", true, false);

    // Text

    public static final FileFormat TEXT = new FileFormat(
            "TEXT", "txt", "text/plain", true, false);
}
