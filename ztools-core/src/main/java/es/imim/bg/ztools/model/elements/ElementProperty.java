package es.imim.bg.ztools.model.elements;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso(value = {
		BeanElementProperty.class})
		
public class ElementProperty {

	protected String id;
	protected String name;
	protected String description;
	
	public ElementProperty() {
	}
	
	public ElementProperty(
			String id, String name, String description) {
		
		this.id = id;
		this.name = name;
		this.description = description;
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
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		if (name != null)
			sb.append(" : ").append(name);
		if (description != null)
			sb.append(" : ").append(description);
		return sb.toString();
	}
}
