package org.gitools.ui.biomart.panel;

public interface BiomartLoadingListener {

	void loadingStarts();

	void loadingEnds();

	void loadingException(Throwable cause);
}
