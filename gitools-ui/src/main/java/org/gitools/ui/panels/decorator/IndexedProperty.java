package org.gitools.ui.panels.decorator;

import org.gitools.matrix.model.element.IElementAttribute;

public class IndexedProperty {
	private int index;
	private IElementAttribute property;
	
	public IndexedProperty(int index, IElementAttribute property) {
		this.index = index;
		this.property = property;
	}
	
	public int getIndex() {
		return index;
	}
	
	public IElementAttribute getProperty() {
		return property;
	}
	
	@Override
	public String toString() {
		return property.getName();
	}
}