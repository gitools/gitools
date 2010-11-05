/*
 *  Copyright 2010 cperez.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package edu.upf.bg.textpatt;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class BeanResolver implements TextPattern.VariableValueResolver {

	private static class BeanProperty {
		private Method m;

		public BeanProperty(Method m) {
			this.m = m;
		}

		public Object get(Object beanInstance) {
			try {
				return m.invoke(beanInstance, (Object[]) null);
			}
			catch (Exception ex) {
				return null;
			}
		}
	}
	
	private Class<?> beanClass;
	private Object beanInstance;

	private Map<String, BeanProperty> beanProperties;

	public BeanResolver(Class<?> beanClass) {
		this.beanClass = beanClass;
		this.beanProperties = readProperties(beanClass);
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public Object getBeanInstance() {
		return beanInstance;
	}

	public void setBeanInstance(Object beanInstance) {
		this.beanInstance = beanInstance;
	}

	@Override
	public String resolveValue(String variableName) {
		BeanProperty p = beanProperties.get(variableName.toLowerCase());
		if (p == null)
			return null;

		Object value = p.get(beanInstance);
		if (value == null)
			return null;

		return value.toString();
	}

	private Map<String, BeanProperty> readProperties(Class<?> beanClass) {
		Map<String, BeanProperty> map = new HashMap<String, BeanResolver.BeanProperty>();

		for (Method m : beanClass.getMethods()) {
			boolean isGet = m.getName().startsWith("get");
			boolean isIs = m.getName().startsWith("is");
			if (m.getParameterTypes().length == 0
					&& !m.getName().equals("getClass")
					&& (isGet || isIs)) {

				final String getterName = isGet ?
						m.getName().substring(3) : m.getName().substring(2);

				//final Class<?> propertyClass = m.getReturnType();

				BeanProperty p = new BeanProperty(m);
				map.put(getterName.toLowerCase(), p);
			}
		}

		return map;
	}
}
