package org.gitools.model.figure;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.decorator.impl.AnnotationHeaderDecorator;
import org.gitools.model.decorator.impl.FormattedTextElementDecorator;
import org.gitools.model.matrix.TableFormatException;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.table.ITable;
import org.gitools.model.table.ITableColumn;
import org.gitools.model.table.TableView;

@SuppressWarnings("unused")
public class TableFigure extends Figure implements Serializable {

	private static final long serialVersionUID = 9006041133309250290L;

	private TableView tableView;

	private List<ElementDecorator> cellDecorators;
	private List<HeaderDecorator> headerDecorators;

	private boolean showGrid;

	private Color gridColor;

	private int rowSize;

	public TableFigure() {

		showGrid = true;
		gridColor = Color.WHITE;
		rowSize = 18;

		cellDecorators = new ArrayList<ElementDecorator>();
		headerDecorators = new ArrayList<HeaderDecorator>();
	}

	
	public TableFigure(TableView tableView){
		
		showGrid = true;
		gridColor = Color.WHITE;
		rowSize = 18;
		
		this.tableView = tableView;
		cellDecorators = new ArrayList<ElementDecorator>();
		headerDecorators = new ArrayList<HeaderDecorator>();
		
		Object cellElement;
		ElementDecorator cellDecorator;
		IElementAdapter elementAdapter;
		HeaderDecorator headerDecorator;
		
		for (int i=0 ; i< tableView.getColumnCount(); i++) {
			elementAdapter = tableView.getCellColumnAdapter(i);
			cellDecorator = 
				new FormattedTextElementDecorator(elementAdapter);
			
			headerDecorator = new AnnotationHeaderDecorator();
			cellDecorators.add(cellDecorator);
			headerDecorators.add(headerDecorator);
			
		}
	}
	
	
	
	public TableFigure(
			ITable table, 
			List<ElementDecorator> cellDecorators,
			List<HeaderDecorator> headerDecorators) 
				throws TableFormatException {
		
		int columns = table.getColumnCount(); 
		
		if ((columns == cellDecorators.size()) 
				&&  (columns == headerDecorators.size())){
			
			this.cellDecorators = cellDecorators;
			this.headerDecorators = headerDecorators;
			showGrid = true;
			gridColor = Color.WHITE;
			rowSize = 18;
		}
		 else 
			throw new TableFormatException
				("Wrong number of columns in table decorators");
		}

	
	public void setDecorator(ElementDecorator decorator, int column) {
		cellDecorators.set(column, decorator);
	}

	public void setHeaderDecorator(ElementDecorator decorator, int column) {
		cellDecorators.set(column, decorator);
	}

	public TableView getTableView() {
		return tableView;
	}

	public void setTableView(TableView tableView) {
		this.tableView = tableView;
	}

	public List<ElementDecorator> getCellDecorators() {
		return cellDecorators;
	}

	public void setCellDecorators(List<ElementDecorator> cellDecorators) {
		this.cellDecorators = cellDecorators;
	}

	public List<HeaderDecorator> getHeaderDecorators() {
		return headerDecorators;
	}

	public void setHeaderDecorators(List<HeaderDecorator> headerDecorators) {
		this.headerDecorators = headerDecorators;
	}
}
