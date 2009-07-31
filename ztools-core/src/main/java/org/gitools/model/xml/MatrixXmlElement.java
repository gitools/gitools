package org.gitools.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.matrix.Matrix;
import org.gitools.model.xml.adapter.ResourceXmlAdapter;
import org.gitools.resources.IResource;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"reference", "matrix" })
public class MatrixXmlElement {

	@XmlJavaTypeAdapter(ResourceXmlAdapter.class)
	private IResource reference;
	private Matrix matrix;

	public MatrixXmlElement() {
	}

	public MatrixXmlElement(IResource reference, Matrix matrix) {
		this.reference = reference;
		this.matrix = matrix;
	}

	public IResource getReference() {
		return reference;
	}

	public void setReference(IResource reference) {
		this.reference = reference;
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
}
