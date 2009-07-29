package org.gitools.model.figure;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.AbstractModel;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.decorator.impl.AnnotationHeaderDecorator;
import org.gitools.model.decorator.impl.FormattedTextElementDecorator;
import org.gitools.model.matrix.TableFormatException;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.table.ITable;
import org.gitools.model.table.impl.TableView;
import org.gitools.model.xml.adapter.ColorXmlAdapter;

@SuppressWarnings("unused")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tableFigure")
public class TableFigure extends Figure implements Serializable {

	private static final long serialVersionUID = 9006041133309250290L;

	private TableView tableView;
	
	private List<AbstractModel> cellDecorators;

	private List<AbstractModel> headerDecorators;

	private boolean showGrid;

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	private Color gridColor;

	private int rowSize;

	public TableFigure() {

		showGrid = true;
		gridColor = Color.WHITE;
		rowSize = 18;

		cellDecorators = new ArrayList<AbstractModel>();
		headerDecorators = new ArrayList<AbstractModel>();
	}

	public TableFigure(TableView tableView) {

		showGrid = true;
		gridColor = Color.WHITE;
		rowSize = 18;

		this.tableView = tableView;
		cellDecorators = new ArrayList<AbstractModel>();
		headerDecorators = new ArrayList<AbstractModel>();

		Object cellElement;
		
		ElementDecorator cellDecorator;
		IElementAdapter elementAdapter;
		HeaderDecorator headerDecorator;

		for (int i = 0; i < tableView.getColumnCount(); i++) {
			elementAdapter = tableView.getCellColumnAdapter(i);
			cellDecorator = new FormattedTextElementDecorator(elementAdapter);

			headerDecorator = new AnnotationHeaderDecorator();
			cellDecorators.add(cellDecorator);
			headerDecorators.add(headerDecorator);

		}
	}

	public TableFigure(ITable table, List<AbstractModel> cellDecorators,
			List<AbstractModel> headerDecorators) throws TableFormatException {

		int columns = table.getColumnCount();

		if ((columns == cellDecorators.size())
				&& (columns == headerDecorators.size())) {

			this.cellDecorators = cellDecorators;
			this.headerDecorators = headerDecorators;
			showGrid = true;
			gridColor = Color.WHITE;
			rowSize = 18;
		} else
			throw new TableFormatException(
					"Wrong number of columns in table decorators");
	}

	public void setDecorator(AbstractModel decorator, int column) {
		cellDecorators.set(column, decorator);
	}

	public void setHeaderDecorator(AbstractModel decorator, int column) {
		cellDecorators.set(column, decorator);
	}

	public TableView getTableView() {
		return tableView;
	}

	public void setTableView(TableView tableView) {
		this.tableView = tableView;
	}

	public AbstractModel getHeaderDecorator(int column) {
		return headerDecorators.get(column);

	}

	public AbstractModel getDecorator(int column) {
		return cellDecorators.get(column);

	}

	
	
	
	

	
	
	
	
	
}
