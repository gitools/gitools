/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.persistence;

public class FileFormats {

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

	// Data

	public static final FileFormat GENE_MATRIX = new FileFormat(
			"Gene Matrix", FileSuffixes.GENE_MATRIX, MimeTypes.GENE_MATRIX);

	public static final FileFormat GENE_MATRIX_TRANSPOSED = new FileFormat(
			"Gene Matrix Transposed", FileSuffixes.GENE_MATRIX_TRANSPOSED, MimeTypes.GENE_MATRIX_TRANSPOSED);

	public static final FileFormat DOUBLE_BINARY_MATRIX = new FileFormat(
			"Binary data matrix", FileSuffixes.DOUBLE_BINARY_MATRIX, MimeTypes.DOUBLE_BINARY_MATRIX);

	public static final FileFormat DOUBLE_MATRIX = new FileFormat(
			"Continuous data matrix", FileSuffixes.DOUBLE_MATRIX, MimeTypes.DOUBLE_MATRIX);

	public static final FileFormat MODULES_INDEXED_MAP = new FileFormat(
			"Indexed mappings", FileSuffixes.MODULES_INDEXED_MAP, MimeTypes.MODULES_INDEXED_MAP);

	public static final FileFormat MODULES_2C_MAP = new FileFormat(
			"Two columns mappings", FileSuffixes.MODULES_2C_MAP, MimeTypes.MODULES_2C_MAP);

	public static final FileFormat MODULES_2C_MAP_COMPRESSED = new FileFormat(
			FileFormats.MODULES_2C_MAP.getTitle() + " compressed", FileFormats.MODULES_2C_MAP.getExtension() + ".gz", MimeTypes.MODULES_2C_MAP, true, false);

	public static final FileFormat RESULTS_MATRIX = new FileFormat(
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
}
