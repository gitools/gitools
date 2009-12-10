package org.gitools.ui.panels.decorator;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.figure.HeatmapFigure;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.IElementProperty;

public abstract class AbstractElementDecoratorPanel extends JPanel {

	private static final long serialVersionUID = 7349354490870110812L;

	protected HeatmapFigure model;
	
	protected List<IndexedProperty> valueProperties;

	public AbstractElementDecoratorPanel(HeatmapFigure model) {
		this.model = model;
	}
	
	protected IMatrixView getTable() {
		return model.getMatrixView();
	}
	
	protected ElementDecorator getDecorator() {
		return model.getCellDecorator();
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
