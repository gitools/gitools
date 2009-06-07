package org.gitools.ui.panels.decorator;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.gitools.ui.model.TableViewModel;

import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.table.element.IElementProperty;

public abstract class AbstractDecoratorPanel extends JPanel {

	private static final long serialVersionUID = 7349354490870110812L;

	protected TableViewModel model;
	
	protected List<IndexedProperty> valueProperties;

	public AbstractDecoratorPanel(TableViewModel model) {
		this.model = model;
	}
	
	protected ITable getTable() {
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
