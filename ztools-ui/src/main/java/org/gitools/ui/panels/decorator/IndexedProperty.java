package org.gitools.ui.panels.decorator;

import org.gitools.model.table.element.IElementProperty;

public class IndexedProperty {
	private int index;
	private IElementProperty property;
	
	public IndexedProperty(int index, IElementProperty property) {
		this.index = index;
		this.property = property;
	}
	
	public int getIndex() {
		return index;
	}
	
	public IElementProperty getProperty() {
		return property;
	}
	
	@Override
	public String toString() {
		return property.getName();
	}
}