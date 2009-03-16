package es.imim.bg.ztools.ui.model;

import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.decorator.ElementDecoratorDescriptor;
import es.imim.bg.ztools.table.decorator.ElementDecoratorFactory;
import es.imim.bg.ztools.ui.model.table.ITable;

public class TableViewModel extends AbstractModel {

	private static final String DECORATOR_DESCRIPTOR = "decoratorDescriptor";
	private static final String DECORATOR = "decorator";
	private static final String TABLE = "table";
	
	private ElementDecoratorDescriptor decoratorDescriptor;
	private ElementDecorator decorator;
	
	private ITable table;
	
	public TableViewModel(
			ITable table) {
		
		this.decoratorDescriptor = ElementDecoratorFactory.getDescriptors().get(0);
		this.decorator = ElementDecoratorFactory.create(
				decoratorDescriptor, table.getCellAdapter());
		
		this.table = table;
	}
	
	public ElementDecoratorDescriptor getDecoratorDescriptor() {
		return decoratorDescriptor;
	}
	
	public void setDecoratorDescriptor(
			ElementDecoratorDescriptor decoratorDescriptor) {
		
		this.decoratorDescriptor = decoratorDescriptor;
		
		setDecorator(ElementDecoratorFactory.create(
				decoratorDescriptor, table.getCellAdapter()));
		
		firePropertyChange(DECORATOR_DESCRIPTOR);
	}
	
	public final ElementDecorator getDecorator() {
		return decorator;
	}
	
	public final void setDecorator(ElementDecorator decorator) {
		this.decorator = decorator;
		firePropertyChange(DECORATOR);
	}
	
	public final ITable getTable() {
		return table;
	}
	
	public final void setTable(ITable table) {
		this.table = table;
		firePropertyChange(TABLE);
	}
}
