package org.gitools.matrix.model.element;

import java.util.List;

//TODO Rename to IAttributeAdapter ?
public interface IElementAdapter {
	
	Class<?> getElementClass();
	
	int getPropertyCount();
	IElementAttribute getProperty(int index);
	List<IElementAttribute> getProperties();
	int getPropertyIndex(String string);
	
	Object getValue(Object element, int index);
	void setValue(Object element, int index, Object value);
}
