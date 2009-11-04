package org.gitools.model.matrix.element;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class BeanElementProperty extends AbstractElementAttribute {

	private static final long serialVersionUID = 1735870808859461498L;
	
	protected Method getterMethod;
	protected Method setterMethod;
	
	protected BeanElementProperty() {
	}
	
	public BeanElementProperty(
			String id, String name, String description,
			Class<?> valueClass, Method getterMethod, Method setterMethod) {
		
		super(id, name, description, valueClass);
		
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;
	}

	@XmlElement(name = "Class")
	@Override
	public Class<?> getValueClass() {
		return valueClass;
	}
	
	static class MethodAdapter extends XmlAdapter<String, Method>{

		@Override
		public String marshal(Method v) throws Exception {
			final String className = v.getDeclaringClass().getCanonicalName();
			Class<?> returnType = v.getReturnType();
			Class<?>[] paramTypes = v.getParameterTypes();
			Class<?> type = returnType != null ? returnType : paramTypes[0];
			
			return className + ":" + v.getName() + ":" + type.getCanonicalName();
		}

		@Override
		public Method unmarshal(String v) throws Exception {
			final String[] names = v.split(":");
			final String className = names[0];
			final String methodName = names[1];
			final String paramClassName = names[2];
			
			Class<?> paramClass = Class.forName(paramClassName);
			
			Class<?> cls = Class.forName(className);
			return cls.getMethod(methodName, paramClass);
		}
	}
	
	@XmlElement(name = "getter")
	@XmlJavaTypeAdapter(MethodAdapter.class)
	public Method getGetterMethod() {
		return getterMethod;
	}
	
	@XmlElement(name = "setter")
	@XmlJavaTypeAdapter(MethodAdapter.class)
	public Method getSetterMethod() {
		return setterMethod;
	}
}
