package es.imim.bg.ztools.table.element.array;

import es.imim.bg.ztools.table.element.IElementFactory;

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
