package org.gitools.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.resources.IResource;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "matrixType", "reference" })
public class MatrixXmlElement {
	
	@XmlJavaTypeAdapter(ResourceXmlAdapter.class)
	IResource reference;

	String matrixType;

	public MatrixXmlElement() {
	}

	public MatrixXmlElement(String type, IResource reference) {
		this.matrixType = type;
		this.reference = reference;
	}

	public IResource getReference() {
		return reference;
	}

	public void setReference(IResource reference) {
		this.reference = reference;
	}

	public String getMatrixType() {
		return matrixType;
	}

	public void setMatrixType(String matrixType) {
		this.matrixType = matrixType;
	}


}
