package org.gitools.ui.analysis.htest.wizard;

public enum DataContents {

	LIST_OF_ELEMENTS("List of elements"),
	BINARY_DATA_MATRIX("Binary data matrix"),
	CONTINUOUS_DATA_MATRIX("Continuous data matrix");

	private String title;

	private DataContents(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return title;
	}
}
