package org.gitools.model.xml;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ElementDecoratorXmlELement {

	private String decorator;
	private Map<String, String> configuration;

	
	public ElementDecoratorXmlELement(){
		
	}
	
	
	public ElementDecoratorXmlELement(String descriptor,
			Map<String, String> configuration) {
		this.decorator = descriptor;
		this.configuration = configuration;
	}

	public String getDecorator() {
		return decorator;
	}

	public void setDecorator(String decorator) {
		this.decorator = decorator;
	}

	public Map<String, String> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}

}
