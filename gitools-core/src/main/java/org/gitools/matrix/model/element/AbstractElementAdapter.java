package org.gitools.matrix.model.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso(value = {
		BeanElementProperty.class})
		
public abstract class AbstractElementAdapter
		implements IElementAdapter, Serializable {

	private static final long serialVersionUID = -4797939915206004479L;

	protected Class<?> elementClass;
	
	private /*transient*/ List<IElementAttribute> properties = new ArrayList<IElementAttribute>(0);
	
	private /*transient*/ Map<String, Integer> propIdToIndexMap;
	
	public AbstractElementAdapter() {
	}
	
	public AbstractElementAdapter(Class<?> elementClass) {
		this.elementClass = elementClass;
	}
	
	@XmlElement
	@Override
	public Class<?> getElementClass() {
		return elementClass;
	}
	
	protected void setElementClass(Class<?> elementClass) {
		this.elementClass = elementClass;
	}
	
	@Override
	public final int getPropertyCount() {
		return properties.size();
	}
	
	@Override
	public final IElementAttribute getProperty(int index) {
		return properties.get(index);
	}
	
	//@XmlElement(name = "Property", type=ElementProperty.class)
	@Override
	public final List<IElementAttribute> getProperties() {
		return Collections.unmodifiableList(properties);
	}
	
	protected final void setProperties(List<IElementAttribute> properties)  {
		this.properties = properties;
		propIdToIndexMap = new HashMap<String, Integer>();
		for (int index = 0; index < properties.size(); index++) {
			IElementAttribute prop = properties.get(index);
			propIdToIndexMap.put(prop.getId(), index);
		}
	}

	@Override
	public int getPropertyIndex(String id) {
		Integer index = propIdToIndexMap.get(id);
		if (index == null)
			return -1;
			//throw new RuntimeException("There isn't any property with id: " + id);
		
		return index.intValue();
	}
	
	@Override
	public abstract Object getValue(Object element, int index);
	
	@Override
	public abstract void setValue(Object element, int index, Object value);
}