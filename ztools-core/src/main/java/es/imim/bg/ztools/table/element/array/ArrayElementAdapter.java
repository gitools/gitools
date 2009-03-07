package es.imim.bg.ztools.table.element.array;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import es.imim.bg.ztools.table.element.AbstractElementAdapter;
import es.imim.bg.ztools.table.element.AbstractElementProperty;
import es.imim.bg.ztools.table.element.IElementProperty;

@XmlRootElement
public class ArrayElementAdapter 
		extends AbstractElementAdapter {

	private static class ArrayElementProperty 
			extends AbstractElementProperty {

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
