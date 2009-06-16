package org.gitools.model.decorator.impl;

import org.gitools.model.decorator.HeaderDecoration;
import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.matrix.AnnotationMatrix;

public class AnnotationHeaderDecorator extends HeaderDecorator {

	private static final long serialVersionUID = -8529301109846251890L;

	protected AnnotationMatrix annotations;
	protected String namePattern;
	protected String urlPattern;
	
	public AnnotationHeaderDecorator() {
	}

	public AnnotationMatrix getAnnotations() {
		return annotations;
	}
	
	public void setAnnotations(AnnotationMatrix annotations) {
		this.annotations = annotations;
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
	
	@Override
	public HeaderDecoration decorate(HeaderDecoration decoration, Object header) {
		super.decorate(decoration, header);
		return decoration;
	}
}
