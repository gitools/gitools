package org.gitools.model.xml;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.gitools.model.decorator.ElementDecoratorDescriptor;
import org.gitools.model.matrix.element.IElementAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class ElementDecoratorXmlELement {

	@XmlElement(name = "descriptor")
	private ElementDecoratorDescriptor descriptor;
	
	private Class<IElementAdapter> elementAdapterClass;
	
	private Map<String, String> configuration;

	public ElementDecoratorXmlELement(){
		
	}
	
	
	public ElementDecoratorXmlELement(ElementDecoratorDescriptor descriptor,
			Map<String, String> configuration, Class<IElementAdapter> elementAdapterClass) {

		this.descriptor = descriptor;
		this.configuration = configuration;
		this.elementAdapterClass = elementAdapterClass;
	}

	public ElementDecoratorDescriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(ElementDecoratorDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public Map<String, String> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}

	public Class<IElementAdapter> getElementAdapterClass() {
		return elementAdapterClass;
	}

	public void setElementAdapterClass(Class<IElementAdapter> elementAdapterClass) {
		this.elementAdapterClass = elementAdapterClass;
	}

	
}
