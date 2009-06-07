package org.gitools.model.table.decorator;

import org.gitools.model.AbstractModel;
import org.gitools.model.table.element.IElementAdapter;

public abstract class ElementDecorator extends AbstractModel {

	public static final String PROPERTY_CHANGED = "propertyChanged";
	
	protected IElementAdapter adapter;
	
	public ElementDecorator(IElementAdapter adapter) {
		this.adapter = adapter;
	}
	
	public IElementAdapter getAdapter() {
		return adapter;
	}
	
	public abstract void decorate(
			ElementDecoration decoration, 
			Object element);
	
	protected int getPropertyIndex(String[] names) {
		int index = -1;
		int nameIndex = 0;
		
		while (index == -1 && nameIndex < names.length) {
			try {
				index = adapter.getPropertyIndex(names[nameIndex++]);
			}
			catch (Exception e) {}
		}
		
		if (index == -1)
			index = 0;
		
		return index;
	}
}
