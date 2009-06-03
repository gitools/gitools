package es.imim.bg.ztools.ui.model;

import es.imim.bg.ztools.model.AbstractModel;
import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.decorator.ElementDecorator;

public class TableViewModel extends AbstractModel {

	//public static final String DECORATOR_DESCRIPTOR = "decoratorDescriptor";
	public static final String DECORATOR = "decorator";
	public static final String TABLE = "table";
	
	//private ElementDecoratorDescriptor decoratorDescriptor;
	private ElementDecorator decorator;
	
	private ITable table;
	
	public TableViewModel(
			ITable table,
			ElementDecorator decorator) {
		
		this.table = table;
		this.decorator = decorator;
	}
	
	/*public TableViewModel(
			ITable table) {
		
		decoratorDescriptor = ElementDecoratorFactory.getDescriptors().get(0);
		decorator = ElementDecoratorFactory.create(
				decoratorDescriptor, table.getCellAdapter());
		
		this(
				table, 
				decorator);
	}
	*/
	
	/*public ElementDecoratorDescriptor getDecoratorDescriptor() {
		return decoratorDescriptor;
	}
	
	public void setDecoratorDescriptor(
			ElementDecoratorDescriptor decoratorDescriptor) {
		
		final ElementDecoratorDescriptor old = this.decoratorDescriptor;
		
		this.decoratorDescriptor = decoratorDescriptor;
		
		setDecorator(ElementDecoratorFactory.create(
				decoratorDescriptor, table.getCellAdapter()));
		
		firePropertyChange(DECORATOR_DESCRIPTOR, old, decoratorDescriptor);
	}*/
	
	public final ElementDecorator getDecorator() {
		return decorator;
	}
	
	public final void setDecorator(ElementDecorator decorator) {
		final ElementDecorator old = this.decorator;
		
		this.decorator = decorator;
		
		firePropertyChange(DECORATOR, old, decorator);
	}
	
	public final ITable getTable() {
		return table;
	}
	
	public final void setTable(ITable table) {
		this.table = table;
		firePropertyChange(TABLE);
	}
}
