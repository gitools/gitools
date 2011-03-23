package org.gitools.label;

import org.gitools.matrix.model.IMatrix;

public abstract class MatrixLabelProvider implements LabelProvider {

	protected IMatrix matrix;

	public MatrixLabelProvider(IMatrix m) {
		this.matrix = m;
	}
}
