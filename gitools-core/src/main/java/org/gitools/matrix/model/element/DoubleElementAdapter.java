package org.gitools.matrix.model.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DoubleElementAdapter extends AbstractElementAdapter {

	private static final long serialVersionUID = 3053254525952874940L;

	private class InternalAttribute extends AbstractElementAttribute implements Serializable {

		private static final long serialVersionUID = -6461253830835864744L;

		public InternalAttribute(String id, String name, String description, Class<?> valueClass) {
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

	private List<IElementAttribute> getPropertyList() {
		final List<IElementAttribute> properties = new ArrayList<IElementAttribute>();
		IElementAttribute property = new InternalAttribute("value", "Value", "" , double.class);
		properties.add(property);
		return properties;
	}
}
