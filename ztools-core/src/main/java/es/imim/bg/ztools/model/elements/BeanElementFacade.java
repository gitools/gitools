package es.imim.bg.ztools.model.elements;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BeanElementFacade extends ElementFacade {
	
	protected Class<?> elementClass;
	
	protected BeanElementFacade() {
	}
	
	public BeanElementFacade(Class<?> elementClass) {
		this.elementClass = elementClass;
		
		readProperties();
	}

	protected void readProperties() {
		List<ElementProperty> properties = new ArrayList<ElementProperty>();
		
		for (Method m : elementClass.getMethods()) {
			boolean isGet = m.getName().startsWith("get");
			if (m.getParameterTypes().length == 0
					&& !m.getName().equals("getClass")
					&& (isGet || m.getName().startsWith("is"))) {
				
				String id = isGet ?
						m.getName().substring(3) : m.getName().substring(2);
						
				final Class<?> propertyClass = m.getReturnType();
				
				String name = id;
				String description = "";
				
				Property a = m.getAnnotation(Property.class);
				if (a != null) {
					if (a.id() != null)
						id = a.id();
					if (a.name() != null)
						name = a.name();
					if (a.description() != null)
						description = a.description();
				}
				
				Method setterMethod = null;
				for (Method sm : elementClass.getMethods()) {
					if (m.getParameterTypes().length == 0
							&& m.getName().equals("set" + id))
						setterMethod = sm;
				}
				
				ElementProperty prop = new BeanElementProperty(
						id, name, description, propertyClass, 
						m, setterMethod);
				
				properties.add(prop);
			}
		}
		
		setProperties(properties);
	}
	
	public Class<?> getElementClass() {
		return elementClass;
	}
	
	@Override
	public Object getValue(Object element, int index) {
		BeanElementProperty prop = (BeanElementProperty) getProperty(index);
		Method m = prop.getGetterMethod();
		Object value = null;
		try {
			value = m.invoke(element, (Object[]) null);
		} catch (Exception e) {}
		return value;
	}
	
	@Override
	public void setValue(Object element, int index, Object value) {
		BeanElementProperty prop = (BeanElementProperty) getProperty(index);
		Method m = prop.getSetterMethod();
		try {
			value = m.invoke(element, value);
		} catch (Exception e) {}
	}
}
