package org.gitools.model.figure;

import java.io.Serializable;

import org.gitools.model.table.IMatrixView;
import org.gitools.model.table.decorator.ElementDecorator;

public class MatrixFigure 
		extends Figure
		implements Serializable {

	private static final long serialVersionUID = 325437934312047512L;

	//public static final String DECORATOR_DESCRIPTOR = "decoratorDescriptor";
	public static final String DECORATOR = "decorator";
	public static final String TABLE = "table";
	
	//private ElementDecoratorDescriptor decoratorDescriptor;
	private ElementDecorator decorator;
	
	private IMatrixView matrixView;
	
	public MatrixFigure(
			IMatrixView matrixView,
			ElementDecorator decorator) {
		
		this.matrixView = matrixView;
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
	
	public final IMatrixView getTable() {
		return matrixView;
	}
	
	public final void setTable(IMatrixView matrixView) {
		this.matrixView = matrixView;
		firePropertyChange(TABLE);
	}
}
