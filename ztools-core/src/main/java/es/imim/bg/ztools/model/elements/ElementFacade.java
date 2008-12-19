package es.imim.bg.ztools.model.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso(value = {
		BeanElementFacade.class, 
		StringElementFacade.class,
		ArrayElementFacade.class})
		
public abstract class ElementFacade {
	
	protected Class<?> elementClass;
	
	private List<ElementProperty> properties = new ArrayList<ElementProperty>(0);
	
	private Map<String, Integer> propIdToIndexMap;
	
	public ElementFacade() {
	}
	
	public ElementFacade(Class<?> elementClass) {
		this.elementClass = elementClass;
	}
	
	public Class<?> getElementClass() {
		return elementClass;
	}
	
	public final int getPropertyCount() {
		return properties.size();
	}
	
	public final ElementProperty getProperty(int index) {
		return properties.get(index);
	}
	
	/*@XmlElementWrapper(name = "properties")
	@XmlAnyElement*/
	public final List<ElementProperty> getProperties() {
		return Collections.unmodifiableList(properties);
	}
	
	protected final void setProperties(List<ElementProperty> properties)  {
		this.properties = properties;
		propIdToIndexMap = new HashMap<String, Integer>();
		for (int index = 0; index < properties.size(); index++) {
			ElementProperty prop = properties.get(index); 
			propIdToIndexMap.put(prop.getId(), index);
		}
	}

	public abstract Object getValue(Object element, int index);
	
	public Object getValue(Object element, String id) {
		Integer index = propIdToIndexMap.get(id);
		return getValue(element, index.intValue());
	}
	
	public abstract void setValue(Object element, int index, Object value);
	
	public void setValue(Object element, String id, Object value) {
		Integer index = propIdToIndexMap.get(id);
		setValue(element, index.intValue(), value);
	}
}
