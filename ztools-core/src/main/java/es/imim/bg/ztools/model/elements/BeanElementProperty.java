package es.imim.bg.ztools.model.elements;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BeanElementProperty extends ElementProperty {

	protected Class<?> propertyClass;
	protected Method getterMethod;
	protected Method setterMethod;
	
	protected BeanElementProperty() {
	}
	
	public BeanElementProperty(
			String id, String name, String description,
			Class<?> propertyClass, Method getterMethod, Method setterMethod) {
		
		super(id, name, description);
		
		this.propertyClass = propertyClass;
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;
	}

	public Class<?> getPropertyClass() {
		return propertyClass;
	}
	
	public Method getGetterMethod() {
		return getterMethod;
	}
	
	public Method getSetterMethod() {
		return setterMethod;
	}
}
