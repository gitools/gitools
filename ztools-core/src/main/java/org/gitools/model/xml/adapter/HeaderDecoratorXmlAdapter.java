package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.decorator.impl.AnnotationHeaderDecorator;

public class HeaderDecoratorXmlAdapter extends XmlAdapter <AnnotationHeaderDecorator,HeaderDecorator>{

	@Override
	public AnnotationHeaderDecorator marshal(HeaderDecorator v)
			throws Exception {
	
		return (AnnotationHeaderDecorator)v;
	}

	@Override
	public HeaderDecorator unmarshal(AnnotationHeaderDecorator v)
			throws Exception {
		return v;
	}

}
