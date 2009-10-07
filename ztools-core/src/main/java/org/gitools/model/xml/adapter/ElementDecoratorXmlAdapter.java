package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.xml.ElementDecoratorXmlElement;

public class ElementDecoratorXmlAdapter
	extends XmlAdapter<ElementDecoratorXmlElement, ElementDecorator> {
	
	@Override
	public ElementDecoratorXmlElement marshal(ElementDecorator v) throws Exception {
		return new ElementDecoratorXmlElement(ElementDecoratorFactory
				.getDescriptor(v.getClass()).getName(), v.getConfiguration());
	}

	@Override
	public ElementDecorator unmarshal(ElementDecoratorXmlElement v) throws Exception {

		ElementDecorator decorator = (ElementDecorator) ElementDecoratorFactory
				.create(v.getDecorator(), null);
		decorator.setConfiguration(v.getConfiguration());
		return decorator;
	}

}
