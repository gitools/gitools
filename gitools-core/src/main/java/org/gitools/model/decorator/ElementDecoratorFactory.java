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

import java.util.ArrayList;
import java.util.List;

import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.model.decorator.impl.LinearTwoSidedElementDecorator;
import org.gitools.model.decorator.impl.PValueElementDecorator;
import org.gitools.model.decorator.impl.ZScoreElementDecorator;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.impl.CorrelationElementDecorator;


public class ElementDecoratorFactory {

	private static final List<ElementDecoratorDescriptor> descriptors
			= new ArrayList<ElementDecoratorDescriptor>();
	
	static {
		descriptors.add(new ElementDecoratorDescriptor(
				ElementDecoratorNames.BINARY, BinaryElementDecorator.class));
		
		descriptors.add(new ElementDecoratorDescriptor(
				ElementDecoratorNames.LINEAR_TWO_SIDED, LinearTwoSidedElementDecorator.class));
		
		descriptors.add(new ElementDecoratorDescriptor(
				ElementDecoratorNames.PVALUE, PValueElementDecorator.class));
		
		descriptors.add(new ElementDecoratorDescriptor(
				ElementDecoratorNames.ZSCORE, ZScoreElementDecorator.class));

		descriptors.add(new ElementDecoratorDescriptor(
				ElementDecoratorNames.CORRELATION, CorrelationElementDecorator.class));

		/*descriptors.add(new ElementDecoratorDescriptor(
				ElementDecoratorNames.FORMATTED_TEXT, FormattedTextElementDecorator.class));*/
	
	}
	
	public static ElementDecorator create(
			String name, 
			IElementAdapter adapter) {
		
		for (ElementDecoratorDescriptor descriptor : descriptors)
			if (descriptor.getName().equals(name))
				return create(descriptor, adapter);
		
		return null;
	}
	
	public static ElementDecorator create(
			ElementDecoratorDescriptor descriptor, 
			IElementAdapter adapter) {
		
		final Class<? extends ElementDecorator> cls = descriptor.getDecoratorClass();
		
		ElementDecorator decorator = null;
		try {
			decorator = cls.getConstructor(IElementAdapter.class)
								.newInstance(new Object[] { adapter });
		}
		catch (Exception e) {
			return null;
		}
		
		return decorator;
	}

	public static ElementDecoratorDescriptor getDescriptor(
			Class<? extends ElementDecorator> decoratorClass) {
		
		for (ElementDecoratorDescriptor desc : descriptors)
			if (desc.getDecoratorClass().equals(decoratorClass))
				return desc;
		
		return null;
	}
	
	public static List<ElementDecoratorDescriptor> getDescriptors() {
		return descriptors;
	}
}
