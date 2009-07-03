package org.gitools.model.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;

public class ElementDecoratorXmlAdapter extends
		XmlAdapter<Map<String, String>, ElementDecorator> {

	Map <String, String> xmlMap = new HashMap <String, String>();
	
	@Override
	public Map<String, String> marshal(ElementDecorator v) throws Exception {
		xmlMap.put("nameDescriptor", ElementDecoratorFactory.getDescriptor(v.getClass()).getName());
	//TODO: recorrer las properties mediante los getters i setters
		
		
		
		return null;
	}

	@Override
	public ElementDecorator unmarshal(Map<String, String> v) throws Exception {
		
		xmlMap = v;
		
		//ElementDecoratorFactory.create(v.get("nameDescriptor",E));
		return null;
	}

}
