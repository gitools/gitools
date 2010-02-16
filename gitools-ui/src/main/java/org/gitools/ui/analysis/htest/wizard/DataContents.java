package org.gitools.ui.analysis.htest.wizard;

import org.gitools.persistence.MimeTypes;

public enum DataContents {

	LIST_OF_ELEMENTS("List of elements", MimeTypes.ELEMENT_LISTS),
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
