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
	
	private List<ElementProperty> properties = new ArrayList<ElementProperty>(0);
	
	protected Map<String, Integer> propIdToIndexMap;
	
	public ElementFacade() {
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
	
	public abstract void setValue(Object element, int index, Object value);
	
	public Object getValue(Object element, String id) {
		Integer index = propIdToIndexMap.get(id);
		return getValue(element, index.intValue());
	}
}
