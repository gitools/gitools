package es.imim.bg.ztools.model.elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArrayElementAdapter extends ElementAdapter {

	protected String[] ids;
	
	protected ArrayElementAdapter() {
	}
	
	public ArrayElementAdapter(String[] ids) {
		super(double[].class);
		
		this.ids = ids;
		
		List<ElementProperty> properties = new ArrayList<ElementProperty>();
		
		for (String id : ids)
			properties.add(new ElementProperty(id, id, ""));

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
