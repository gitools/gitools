package org.gitools.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.resources.IResource;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "type", "reference" })
public class MatrixXmlElement {

	@XmlJavaTypeAdapter(ResourceXmlAdapter.class)
	private IResource reference;
	private String type;

	public MatrixXmlElement() {
	}

	public MatrixXmlElement(String type, IResource reference) {
		this.type = type;
		this.reference = reference;
	}

	public IResource getReference() {
		return reference;
	}

	public void setReference(IResource reference) {
		this.reference = reference;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
