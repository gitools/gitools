/*
 *  Copyright 2010 cperez.
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

	public static final FileFormat PNG = new FileFormat(
			"PNG", "png", "image/png");

	public static final FileFormat JPG = new FileFormat(
			"JPEG", "jpg", "image/jpeg");

	public static final FileFormat PDF = new FileFormat(
			"PDF", "pdf", "application/pdf");
}
