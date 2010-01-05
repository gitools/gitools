package org.gitools.model.xml;

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.xml.adapter.FileXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixXmlElement {

	@XmlJavaTypeAdapter(FileXmlAdapter.class)
	private File resource;
	
	public MatrixXmlElement(){
		resource = null;
	}
	
	public MatrixXmlElement(File matrixfile){
		this.resource = matrixfile;
	}
	
	public File getResource() {
		return resource;
	}

	public void setResource(File resource) {
		this.resource = resource;
	}
}
