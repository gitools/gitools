package org.gitools.model.matrix.element.array;

import org.gitools.model.matrix.element.IElementFactory;

public class ArrayElementFactory implements IElementFactory {

	private int length;
	
	public ArrayElementFactory(int length) {
		this.length = length;
	}
	
	@Override
	public Object create() {
		//return new Object[length];
		return new double[length];
	}
}
