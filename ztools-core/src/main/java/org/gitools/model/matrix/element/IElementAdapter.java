package org.gitools.model.matrix.element;

import java.util.List;

//TODO Rename to IAttributeAdapter ?
public interface IElementAdapter {
	
	Class<?> getElementClass();
	
	int getPropertyCount();
	IElementProperty getProperty(int index);
	List<IElementProperty> getProperties();
	int getPropertyIndex(String string);
	
	Object getValue(Object element, int index);
	void setValue(Object element, int index, Object value);
}
