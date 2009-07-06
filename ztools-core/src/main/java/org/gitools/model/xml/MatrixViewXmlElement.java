package org.gitools.model.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.matrix.IMatrix;

public class MatrixViewXmlElement {

	@XmlElement
	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	private IMatrix matrix;
	
}
