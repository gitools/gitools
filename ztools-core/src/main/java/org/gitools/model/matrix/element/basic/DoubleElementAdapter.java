package org.gitools.model.matrix.element.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gitools.model.matrix.element.AbstractElementAdapter;
import org.gitools.model.matrix.element.AbstractElementProperty;
import org.gitools.model.matrix.element.IElementProperty;


public class DoubleElementAdapter extends AbstractElementAdapter {

	private class Property extends AbstractElementProperty implements Serializable {
		public Property(String id, String name, String description, Class<?> valueClass) {
			super(id,name, description, valueClass);
		}

	}

	public DoubleElementAdapter() {
		super(double.class);
		
		this.setProperties(getPropertyList());
	}

	@Override
	public Object getValue(Object element, int index) {
		return element;
	}

	@Override
	public void setValue(Object element, int index, Object value) {
		throw new UnsupportedOperationException(
				getClass().getSimpleName() + " doesn't support change string value.");
	}

	private List<IElementProperty> getPropertyList() {
		final List<IElementProperty> properties = new ArrayList<IElementProperty>();
		IElementProperty property = new Property("value", "Value", "" , double.class);
		properties.add(property);
		return properties;
	}
}
