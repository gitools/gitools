package org.gitools.matrix.sort;

import org.gitools.matrix.model.IMatrixView;

public interface MatrixSorter {

	void sort(IMatrixView matrixView, SortCriteria[] criteria);
}
