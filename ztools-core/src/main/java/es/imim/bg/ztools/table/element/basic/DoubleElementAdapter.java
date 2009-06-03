package es.imim.bg.ztools.table.element.basic;

import java.util.ArrayList;
import java.util.List;

import es.imim.bg.ztools.table.element.AbstractElementAdapter;
import es.imim.bg.ztools.table.element.AbstractElementProperty;
import es.imim.bg.ztools.table.element.IElementProperty;

public class DoubleElementAdapter extends AbstractElementAdapter {

	private class Property extends AbstractElementProperty {
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
