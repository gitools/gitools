package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;

//FIXME Review
public class MatrixViewXmlAdapter extends XmlAdapter<MatrixView, IMatrixView> {

	@Override
	public MatrixView marshal(IMatrixView matrixView) throws Exception {
		return (MatrixView) matrixView;
	}

	@Override
	public IMatrixView unmarshal(MatrixView matrixView) throws Exception {
		return matrixView;
	}

}
