package org.gitools.model.decorator;

import org.gitools.model.AbstractModel;
import org.gitools.model.matrix.StringMatrix;

public class HeaderDecorator extends AbstractModel {

	private static final long serialVersionUID = -2580139666999968074L;

	protected StringMatrix annotations;
	
	protected String namePattern;
	
	protected String urlPattern;
	
	protected int size;
	
	public HeaderDecorator() {
	}
	
	public StringMatrix getAnnotations() {
		return annotations;
	}
	
	public void setAnnotations(StringMatrix annotations) {
		this.annotations = annotations;
		
		//TODO: Create maps for items and attributes indices
	}
	
	public String getNamePattern() {
		return namePattern;
	}
	
	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public String getUrlPattern() {
		return urlPattern;
	}
	
	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public HeaderDecoration decorate(HeaderDecoration decoration, Object header) {
		decoration.setName(header.toString());
		decoration.setUrl("");
		return decoration;
	}
}
