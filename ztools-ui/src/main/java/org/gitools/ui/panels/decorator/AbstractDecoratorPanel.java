package org.gitools.ui.panels.decorator;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.table.IMatrixView;
import org.gitools.model.table.decorator.ElementDecorator;
import org.gitools.model.table.element.IElementAdapter;
import org.gitools.model.table.element.IElementProperty;

public abstract class AbstractDecoratorPanel extends JPanel {

	private static final long serialVersionUID = 7349354490870110812L;

	protected MatrixFigure model;
	
	protected List<IndexedProperty> valueProperties;

	public AbstractDecoratorPanel(MatrixFigure model) {
		this.model = model;
	}
	
	protected IMatrixView getTable() {
		return model.getTable();
	}
	
	protected ElementDecorator getDecorator() {
		return model.getDecorator();
	}
	
	protected List<IndexedProperty> loadAllProperties(List<IndexedProperty> properties, IElementAdapter adapter) {
		int numProps = adapter.getPropertyCount();
		
		if (properties == null)
			properties = new ArrayList<IndexedProperty>(numProps);
		
		for (int i = 0; i < numProps; i++) {
			final IElementProperty property = adapter.getProperty(i);
			properties.add(new IndexedProperty(i, property));
		}
		
		return properties;
	}
}
