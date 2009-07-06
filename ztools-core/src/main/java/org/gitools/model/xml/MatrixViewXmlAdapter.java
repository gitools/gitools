package org.gitools.model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.MatrixView;

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
