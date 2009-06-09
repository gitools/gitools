package org.gitools.model.decorator.impl;

import org.gitools.model.AbstractModel;
import org.gitools.model.decorator.HeaderDecoration;
import org.gitools.model.matrix.StringMatrix;

public class AnnotatedHeaderDecorator extends AbstractModel {

	private static final long serialVersionUID = -2580139666999968074L;

	protected StringMatrix annotations;
	
	protected String namePattern;
	
	protected String urlPattern;
	
	public AnnotatedHeaderDecorator() {
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
	
	public HeaderDecoration decorate(HeaderDecoration decoration, Object header) {
		decoration.setText(header.toString());
		decoration.setUrl(null);
		return decoration;
	}
}
