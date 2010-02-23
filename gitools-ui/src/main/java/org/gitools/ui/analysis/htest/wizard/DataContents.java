package org.gitools.ui.analysis.htest.wizard;

import org.gitools.persistence.MimeTypes;

public enum DataContents {

	GENE_MATRIX_TRANSPOSED("Concept elements lists by rows (GMT)", MimeTypes.GENE_MATRIX_TRANSPOSED),
	GENE_MATRIX("Concept elements lists by columns (GMX)", MimeTypes.GENE_MATRIX),
	BINARY_DATA_MATRIX("Binary data matrix", MimeTypes.DOUBLE_BINARY_MATRIX),
	CONTINUOUS_DATA_MATRIX("Continuous data matrix", MimeTypes.DOUBLE_MATRIX);

	private String title;
	private String mime;

	private DataContents(String title, String mime) {
		this.title = title;
		this.mime = mime;
	}

	@Override
	public String toString() {
		return title;
	}

	public String getMime() {
		return mime;
	}
}
