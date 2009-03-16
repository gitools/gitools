package es.imim.bg.ztools.table.decorator;

import java.util.ArrayList;
import java.util.List;

import es.imim.bg.ztools.table.decorator.pvalue.PValueElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;

public class ElementDecoratorFactory {

	private static List<ElementDecoratorDescriptor> descriptors
			= new ArrayList<ElementDecoratorDescriptor>();
	
	static {
		descriptors.add(new ElementDecoratorDescriptor("P-Value scale", PValueElementDecorator.class));
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

	public static List<ElementDecoratorDescriptor> getDescriptors() {
		return descriptors;
	}
}
