package org.gitools.model.matrix;

import java.io.Serializable;

import org.gitools.model.Artifact;

public abstract class Matrix extends Artifact implements 
	IMatrix, Serializable {

	private static final long serialVersionUID = -6646581633872643158L;

	public Matrix() {
		super();
		
	}
	
}
