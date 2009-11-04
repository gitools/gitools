package org.gitools.model.matrix.element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArrayElementAdapter 
		extends AbstractElementAdapter {

	private static final long serialVersionUID = 5864596809781257355L;

	private static class ArrayElementProperty extends AbstractElementAttribute {

		private static final long serialVersionUID = 7803752573190009823L;

		public ArrayElementProperty(String id, String name, String description) {
			super(id, name, description, double.class);
		} 
	}
	
	protected String[] ids;
	
	protected ArrayElementAdapter() {
	}
	
	public ArrayElementAdapter(String[] ids) {
		super(double[].class);
		
		this.ids = ids;
		
		List<IElementProperty> properties = new ArrayList<IElementProperty>();
		
		for (String id : ids)
			properties.add(new ArrayElementProperty(id, id, ""));

		setProperties(properties);
	}
	
	@Override
	public Object getValue(Object element, int index) {
		return element != null ? ((double[])element)[index] : null;
	}

	@Override
	public void setValue(Object element, int index, Object value) {
		if (element != null)
			((double[])element)[index] = (Double) value;
	}
}
