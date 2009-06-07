package org.gitools.model.table.element;
		
public interface IElementProperty {

	String getId();
	String getName();
	String getDescription();
	
	Class<?> getValueClass();
	
}
