package org.gitools.model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;

/**
 * <p>
 * Generate an ElementDecorator with null ElementAdapter.
 * </p>
 * 
 */
public class ElementDecoratorXmlAdapter extends
		XmlAdapter<ElementDecoratorXmlELement, ElementDecorator> {

	/**
	 * Generate an ElementDecorator with null ElementAdapter
	 */
	public ElementDecoratorXmlAdapter() {
	}

	
	@Override
	public ElementDecoratorXmlELement marshal(ElementDecorator v)
			throws Exception {
		return new ElementDecoratorXmlELement(ElementDecoratorFactory
				.getDescriptor(v.getClass()).getName(), v.getConfiguration());
	}

	@Override
	public ElementDecorator unmarshal(ElementDecoratorXmlELement v)
			throws Exception {

		ElementDecorator decorator = (ElementDecorator) ElementDecoratorFactory
				.create(v.getDecorator(), null);
		decorator.setConfiguration(v.getConfiguration());
		return decorator;
	}

}
