package org.gitools.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.model.matrix.Matrix;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixXmlElement {

	private Matrix matrix;

	public MatrixXmlElement() {
	}

	public MatrixXmlElement(Matrix matrix) {
		this.matrix = matrix;
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
}
