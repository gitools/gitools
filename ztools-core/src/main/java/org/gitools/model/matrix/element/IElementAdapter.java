package org.gitools.model.matrix.element;

import java.util.List;
		
public interface IElementAdapter {
	
	Class<?> getElementClass();
	
	int getPropertyCount();
	IElementProperty getProperty(int index);
	List<IElementProperty> getProperties();
	int getPropertyIndex(String string);
	
	Object getValue(Object element, int index);
	Object getValue(Object element, String id);
	void setValue(Object element, int index, Object value);
	void setValue(Object element, String id, Object value);
}
