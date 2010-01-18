package org.gitools.ui.panels.decorator;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;

public abstract class AbstractElementDecoratorPanel extends JPanel {

	private static final long serialVersionUID = 7349354490870110812L;

	protected Heatmap model;
	
	protected List<IndexedProperty> valueProperties;

	public AbstractElementDecoratorPanel(Heatmap model) {
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
			final IElementAttribute property = adapter.getProperty(i);
			properties.add(new IndexedProperty(i, property));
		}
		
		return properties;
	}
}
