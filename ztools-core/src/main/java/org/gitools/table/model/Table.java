package org.gitools.table.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.Artifact;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.Matrix;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.table.model.impl.AbstractTableColumn;
import org.gitools.model.xml.adapter.MatrixXmlAdapter;
import org.gitools.model.xml.adapter.TableColumnXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "rowCount", "matrix", "annotations", "columns" })
public class Table extends Artifact
	implements ITable, Serializable {

	private int rowCount = 0;

	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	private IMatrix matrix;
	
	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	private AnnotationMatrix annotations;

	@XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
	@XmlJavaTypeAdapter(TableColumnXmlAdapter.class)
    private List<ITableColumn> columns;

	public Table() {
		columns = new ArrayList<ITableColumn>();
	}

	public Table(Matrix matrix, AnnotationMatrix annotations) {
		rowCount = matrix.getRowCount();
		columns = new ArrayList<ITableColumn>();
		this.matrix = matrix;
		this.annotations = annotations;
	}

	public void addColumn(ITableColumn column){
		columns.add(column);
	}

	@Override
	public IElementAdapter getCellColumnAdapter(int column) {
		return columns.get(column).getAdapter();
	}

	@Override
	public ITableColumn getColumn(int index) {
		return columns.get(index);
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public String getHeader(int column) {
		return columns.get(column).getHeader();
	}

	@Override
	public Object getValue(int row, int column) {
		return columns.get(column).getValue(row);
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	public void removeColumn(int index) {
		columns.remove(index);

	}

	public void setAnnotations(AnnotationMatrix annotations) {
		this.annotations = annotations;
	}

	public AnnotationMatrix getAnnotations() {
		return annotations;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	public IMatrix getMatrix() {
		return matrix;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for(int i=0; i<columns.size(); i++){
			AbstractTableColumn col= (AbstractTableColumn)columns.get(i);
			col.setTable(this);
		}
	}
}
