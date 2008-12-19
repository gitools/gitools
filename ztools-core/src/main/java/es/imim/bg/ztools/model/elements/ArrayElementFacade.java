package es.imim.bg.ztools.model.elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArrayElementFacade extends ElementFacade {

	protected String[] ids;
	
	protected ArrayElementFacade() {
	}
	
	public ArrayElementFacade(String[] ids) {
		super(double[].class);
		
		this.ids = ids;
		
		List<ElementProperty> properties = new ArrayList<ElementProperty>();
		
		for (String id : ids)
			properties.add(new ElementProperty(id, id, ""));

		setProperties(properties);
	}
	
	@Override
	public Object getValue(Object element, int index) {
		return ((double[])element)[index];
	}

	@Override
	public void setValue(Object element, int index, Object value) {
		((double[])element)[index] = (Double) value;
	}

}
