package org.gitools.model.decorator.impl;

import org.gitools.model.decorator.HeaderDecoration;
import org.gitools.model.decorator.HeaderDecorator;

public class SimpleHeaderDecorator extends HeaderDecorator {

	private static final long serialVersionUID = -8529301109846251890L;

	public SimpleHeaderDecorator() {
	}

	public HeaderDecoration decorate(HeaderDecoration decoration, Object header) {
		decoration.setText(header.toString());
		decoration.setUrl(null);
		return decoration;
	}
}
