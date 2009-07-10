package org.gitools.model.matrix.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.gitools.model.matrix.element.bean.BeanElementProperty;


@XmlRootElement
@XmlSeeAlso(value = {
		BeanElementProperty.class})
		
public abstract class AbstractElementAdapter
		implements IElementAdapter, Serializable {

	protected Class<?> elementClass;
	
	private transient List<IElementProperty> properties = new ArrayList<IElementProperty>(0);
	
	private transient Map<String, Integer> propIdToIndexMap;
	
	public AbstractElementAdapter() {
	}
	
	public AbstractElementAdapter(Class<?> elementClass) {
		this.elementClass = elementClass;
	}
	
	@XmlElement
	public Class<?> getElementClass() {
		return elementClass;
	}
	
	protected void setElementClass(Class<?> elementClass) {
		this.elementClass = elementClass;
	}
	
	public final int getPropertyCount() {
		return properties.size();
	}
	
	public final IElementProperty getProperty(int index) {
		return properties.get(index);
	}
	
	//@XmlElement(name = "Property", type=ElementProperty.class)
	public final List<IElementProperty> getProperties() {
		return Collections.unmodifiableList(properties);
	}
	
	protected final void setProperties(List<IElementProperty> properties)  {
		this.properties = properties;
		propIdToIndexMap = new HashMap<String, Integer>();
		for (int index = 0; index < properties.size(); index++) {
			IElementProperty prop = properties.get(index); 
			propIdToIndexMap.put(prop.getId(), index);
		}
	}

	@Override
	public int getPropertyIndex(String id) {
		Integer index = propIdToIndexMap.get(id);
		if (index == null)
			throw new RuntimeException("There isn't any property with id: " + id);
		
		return index.intValue();
	}
	
	public abstract Object getValue(Object element, int index);
	
	public Object getValue(Object element, String id) {
		return getValue(element, getPropertyIndex(id));
	}
	
	public abstract void setValue(Object element, int index, Object value);
	
	public void setValue(Object element, String id, Object value) {
		setValue(element, getPropertyIndex(id), value);
	}
}