package org.gitools.matrix.model.element;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BeanElementAdapter extends AbstractElementAdapter {

	private static final long serialVersionUID = 2174377187447656241L;

	protected BeanElementAdapter() {
	}
	
	public BeanElementAdapter(Class<?> elementClass) {
		super(elementClass);
		
		readProperties();
	}

	@Override
	protected void setElementClass(Class<?> elementClass) {
		super.setElementClass(elementClass);
		
		readProperties();
	}
	
	protected void readProperties() {
		List<IElementProperty> properties = new ArrayList<IElementProperty>();
		
		for (Method m : elementClass.getMethods()) {
			boolean isGet = m.getName().startsWith("get");
			if (m.getParameterTypes().length == 0
					&& !m.getName().equals("getClass")
					&& (isGet || m.getName().startsWith("is"))) {
				
				final String getterName = isGet ?
						m.getName().substring(3) : m.getName().substring(2);
						
				final Class<?> propertyClass = m.getReturnType();
				
				String id = getterName;
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
				try {
					setterMethod = elementClass.getMethod(
							"set" + getterName,	new Class<?>[] { propertyClass });
				} catch (Exception e) {	}
				
				IElementProperty prop = new BeanElementProperty(
						id, name, description, propertyClass, 
						m, setterMethod);
				
				properties.add(prop);
			}
		}
		
		setProperties(properties);
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
		} catch (Exception e) {
			
		}
	}
}
