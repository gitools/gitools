package org.gitools.model.matrix;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.gitools.model.Artifact;

@XmlSeeAlso( { 
	ObjectMatrix.class, StringMatrix.class, DoubleMatrix.class, AnnotationMatrix.class })

public abstract class Matrix extends Artifact 
	implements IMatrix, Serializable {

	private static final long serialVersionUID = -6646581633872643158L;

	public Matrix() {
		super();

	}

}
