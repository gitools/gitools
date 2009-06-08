package org.gitools.model.matrix.element;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.gitools.model.matrix.element.bean.BeanElementProperty;


@XmlRootElement
@XmlSeeAlso(value = {
		BeanElementProperty.class})
		
public abstract class AbstractElementProperty 
		implements IElementProperty {

	protected String id;
	protected String name;
	protected String description;
	protected Class<?> valueClass;
	
	public AbstractElementProperty() {
	}
	
	public AbstractElementProperty(
			String id, String name, String description, 
			Class<?> valueClass) {
		
		this.id = id;
		this.name = name;
		this.description = description;
		this.valueClass = valueClass;
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public Class<?> getValueClass() {
		return valueClass;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		if (name != null)
			sb.append(" : ").append(name);
		if (valueClass != null)
			sb.append(" : ").append(valueClass.getSimpleName());
		if (description != null)
			sb.append(" : ").append(description);
		return sb.toString();
	}
}
