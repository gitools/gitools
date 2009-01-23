package es.imim.bg.ztools.model.elements;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringElementAdapter extends ElementAdapter {

	public StringElementAdapter() {
	}
	
	@Override
	public Object getValue(Object element, int index) {
		return element;
	}

	@Override
	public void setValue(Object element, int index, Object value) {
		throw new UnsupportedOperationException("StringElementFacade doesn't support change string value.");
	}

}
