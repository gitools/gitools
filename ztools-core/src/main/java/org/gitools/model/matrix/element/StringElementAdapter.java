package org.gitools.model.matrix.element;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringElementAdapter extends AbstractElementAdapter {

	private static final long serialVersionUID = -5327572398900806886L;

	public StringElementAdapter() {
		super(String.class);
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

}
