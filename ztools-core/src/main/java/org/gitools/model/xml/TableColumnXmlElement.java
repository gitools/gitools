package org.gitools.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.xml.adapter.FileXmlAdapter;
import org.gitools.resources.IResource;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "type", "reference" })
public class TableColumnXmlElement {

	@SuppressWarnings("unused")
	@XmlJavaTypeAdapter(FileXmlAdapter.class)
	private IResource reference;
	private String type;

	TableColumnXmlElement() {

	}

	public TableColumnXmlElement(String type, IResource reference) {
		this.type = type;
		this.reference = reference;
	}
}
