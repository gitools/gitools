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

package org.gitools.table.model.impl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.table.model.ITableColumn;
import org.gitools.table.model.Table;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixCellTableColumn
		extends AbstractTableColumn
		implements ITableColumn, Serializable {

	private static final long serialVersionUID = -5968245911627777748L;

	public MatrixCellTableColumn() {
	}

	public MatrixCellTableColumn(int column, Table table) {
		super(column, table);

	}

	@Override
	public IElementAdapter getAdapter() {
		return table.getMatrix().getCellAdapter();
	}

	@Override
	public String getHeader() {
	    Object object = table.getMatrix().getColumnLabel(column);
		return object == null ? "null" : object.toString();
	}

	@Override
	public int getRowCount() {
		return table.getMatrix().getRowCount();
	}

	@Override
	public Object getValue(int row) {
		return table.getMatrix().getCell(row, column);

	}
}
