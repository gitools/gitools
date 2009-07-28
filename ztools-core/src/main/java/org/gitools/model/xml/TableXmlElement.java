package org.gitools.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.xml.adapter.MatrixXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "matrix", "annotations" })
public class TableXmlElement {

	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	MatrixXmlElement matrix;
	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	MatrixXmlElement annotations;

	public TableXmlElement() {
	}

	public TableXmlElement(MatrixXmlElement matrix, MatrixXmlElement annotations) {
		this.matrix = matrix;
		this.annotations = annotations;
	}

	public MatrixXmlElement getMatrix() {
		return matrix;
	}

	public MatrixXmlElement getAnnotations() {
		return annotations;
	}

}
