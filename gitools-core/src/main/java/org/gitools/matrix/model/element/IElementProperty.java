package org.gitools.matrix.model.element;
		
public interface IElementProperty {

	String getId();
	String getName();
	String getDescription();
	
	Class<?> getValueClass();
}
