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
