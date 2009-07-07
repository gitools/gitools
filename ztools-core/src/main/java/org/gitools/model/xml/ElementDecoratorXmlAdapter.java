package org.gitools.model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.matrix.element.IElementAdapter;

public class ElementDecoratorXmlAdapter extends
		XmlAdapter<ElementDecoratorXmlELement, ElementDecorator> {

	
	public ElementDecoratorXmlAdapter(){
	}
	
	@Override
	public ElementDecoratorXmlELement marshal(ElementDecorator v)
			throws Exception {

		return new ElementDecoratorXmlELement(
				ElementDecoratorFactory.getDescriptor(v.getClass()),
					v.getConfiguration(),(Class<IElementAdapter>) v.getAdapter().getClass());
	}

	@Override
	public ElementDecorator unmarshal(ElementDecoratorXmlELement v)
			throws Exception {
		ElementDecorator decorator = (ElementDecorator) ElementDecoratorFactory
				.create(v.getDescriptor(), v.getElementAdapterClass().newInstance());
		
		decorator.setConfiguration(v.getConfiguration());
		return decorator;
	}

}
