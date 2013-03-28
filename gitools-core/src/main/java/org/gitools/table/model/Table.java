/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.table.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.Artifact;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.Matrix;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.persistence.formats.analysis.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.table.model.impl.AbstractTableColumn;
import org.gitools.model.xml.TableColumnXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class Table extends Artifact	implements ITable, Serializable {

	private int rowCount = 0;

	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	private IMatrix matrix;
	
	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
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
