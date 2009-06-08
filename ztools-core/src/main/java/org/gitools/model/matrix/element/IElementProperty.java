package org.gitools.model.matrix.element;
		
public interface IElementProperty {

	String getId();
	String getName();
	String getDescription();
	
	Class<?> getValueClass();
	
}
