package org.gitools.matrix.model.element;
		
public interface IElementAttribute {

	String getId();
	String getName();
	String getDescription();
	
	Class<?> getValueClass();
}
