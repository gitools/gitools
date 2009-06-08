package org.gitools.model.REVIEW;

import org.gitools.model.figure.TableFigure;

public class FilteredMatrix extends TableFigure {
	/**
	 * Represents a matrix filtered, original data or final resuts of an
	 * analysis
	 */

	/** Descriptor of the Decorator to use in cells **/
	protected String decoratorDescriptor;

	/** Descriptor of the details panel **/
	protected String detailsDescriptor;

	public FilteredMatrix() {
		super();

	}

	public String getDecoratorDescriptor() {
		return decoratorDescriptor;
	}

	public void setDecoratorDescriptor(String decoratorDescriptor) {
		this.decoratorDescriptor = decoratorDescriptor;
	}

}
