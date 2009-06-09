package org.gitools.model.decorator;

import org.gitools.model.AbstractModel;

public abstract class HeaderDecorator extends AbstractModel {

	private static final long serialVersionUID = -2580139666999968074L;
	
	public HeaderDecorator() {
	}
	
	public HeaderDecoration decorate(HeaderDecoration decoration, Object header) {
		decoration.setText(header.toString());
		decoration.setUrl(null);
		return decoration;
	}
}
