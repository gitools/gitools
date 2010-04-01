package org.gitools.matrix.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.gitools.model.Artifact;

@Deprecated
@XmlSeeAlso({BaseMatrix.class})	
public abstract class Matrix
		extends Artifact
		implements IMatrix, Serializable {

	private static final long serialVersionUID = -6646581633872643158L;

	public Matrix() {
		super();
	}
}
