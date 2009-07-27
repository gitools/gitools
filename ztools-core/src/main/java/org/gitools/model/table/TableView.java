package org.gitools.model.table;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.xml.adapter.IndexArrayAdapter;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "contents", "visibleRows", "visibleColumns" })
public class TableView implements ITable, Serializable {

	private static final long serialVersionUID = -3231844654295236093L;

	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	ITable contents;

	@XmlJavaTypeAdapter(IndexArrayAdapter.class)
	private int[] visibleRows;

	@XmlJavaTypeAdapter(IndexArrayAdapter.class)
	private int[] visibleColumns;

	public TableView() {
		visibleRows = new int[0];
		visibleColumns = new int[0];
	}

	public TableView(ITable table) {
		this.contents = table;
		visibleRows = new int[0];
		visibleColumns = new int[0];
	}

	@Override
	public IElementAdapter getCellColumnAdapter(int column) {
		return contents.getCellColumnAdapter(column);
	}

	@Override
	public ITableColumn getColumn(int index) {
		return contents.getColumn(index);

	}

	@Override
	public int getColumnCount() {
		return contents.getColumnCount();
	}

	@Override
	public String getHeader(int column) {
		return contents.getHeader(column);
	}

	@Override
	public int getRowCount() {
		return contents.getRowCount();
	}

	@Override
	public Object getValue(int row, int column) {
		return contents.getValue(row, column);
	}

	// getters and setters

	public ITable getContents() {
		return contents;
	}

	public void setContents(ITable contents) {
		this.contents = contents;
	}

	public int[] getVisibleRows() {
		return visibleRows;
	}

	public void setVisibleRows(int[] visibleRows) {
		this.visibleRows = visibleRows;
	}

	public int[] getVisibleColumns() {
		return visibleColumns;
	}

	public void setVisibleColumns(int[] visibleColumns) {
		this.visibleColumns = visibleColumns;
	}
}
