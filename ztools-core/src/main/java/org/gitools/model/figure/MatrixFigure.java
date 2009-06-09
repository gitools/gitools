package org.gitools.model.figure;

import java.io.Serializable;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.matrix.IMatrixView;

public class MatrixFigure 
		extends Figure
		implements Serializable {

	private static final long serialVersionUID = 325437934312047512L;

	public static final String DECORATOR = "decorator";
	public static final String TABLE = "table";
	
	private ElementDecorator decorator;
	
	private IMatrixView matrixView;
	
	private int cellSize;
	
	public MatrixFigure() {
	}

	public MatrixFigure(
			IMatrixView matrixView,
			ElementDecorator decorator) {
		
		this.matrixView = matrixView;
		this.decorator = decorator;
		this.cellSize = 20;
	}
	
	public final ElementDecorator getDecorator() {
		return decorator;
	}
	
	public final void setDecorator(ElementDecorator decorator) {
		final ElementDecorator old = this.decorator;
		
		this.decorator = decorator;
		
		firePropertyChange(DECORATOR, old, decorator);
	}
	
	public final IMatrixView getMatrixView() {
		return matrixView;
	}
	
	public final void setMatrixView(IMatrixView matrixView) {
		this.matrixView = matrixView;
		firePropertyChange(TABLE);
	}
	
	public int getCellSize() {
		return cellSize;
	}
	
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
		firePropertyChange(PROPERTY_CHANGED);
	}
}
