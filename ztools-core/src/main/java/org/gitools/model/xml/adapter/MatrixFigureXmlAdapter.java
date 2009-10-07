package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.element.IElementAdapter;

//FIXME Stupid adapter?
public class MatrixFigureXmlAdapter
		extends XmlAdapter<MatrixFigure, MatrixFigure> {

	@Override
	public MatrixFigure marshal(MatrixFigure matrixFigure) throws Exception {
		return matrixFigure;
	}

	@Override
	public MatrixFigure unmarshal(MatrixFigure matrixFigure) throws Exception {
		
		IElementAdapter cellAdapter = matrixFigure.getMatrixView()
				.getContents().getCellAdapter();
		matrixFigure.getCellDecorator().setAdapter(cellAdapter);
		return matrixFigure;
	}

}
	