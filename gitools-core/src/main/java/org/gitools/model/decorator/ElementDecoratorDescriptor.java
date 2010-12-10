/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.model.decorator;


public class ElementDecoratorDescriptor {

	private String name;
	private Class<? extends ElementDecorator> decoratorClass;
	
	public ElementDecoratorDescriptor() {
	}

	public ElementDecoratorDescriptor(String name,
			Class<? extends ElementDecorator> decoratorClass) {
		this.name = name;
		this.decoratorClass = decoratorClass;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final Class<? extends ElementDecorator> getDecoratorClass() {
		return decoratorClass;
	}

	public final void setDecoratorClass(Class<? extends ElementDecorator> decoratorClass) {
		this.decoratorClass = decoratorClass;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (!(obj instanceof ElementDecoratorDescriptor))
			return false;
		ElementDecoratorDescriptor other = (ElementDecoratorDescriptor) obj;
		return name.equals(other.name) 
			&& decoratorClass.equals(other.decoratorClass);
	}
	
	@Override
	public int hashCode() {
		return 17 * name.hashCode() + decoratorClass.hashCode();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
