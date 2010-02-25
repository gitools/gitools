/*
 *  Copyright 2010 chris.
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

package org.gitools.matrix.model;

import cern.colt.bitvector.BitMatrix;
import cern.colt.matrix.ObjectFactory1D;
import org.gitools.matrix.model.element.DoubleElementAdapter;

public class DoubleBinaryMatrix extends BaseMatrix {

	private BitMatrix cells;
	private BitMatrix cellsNan;

	public DoubleBinaryMatrix() {
		this("", new String[0], new String[0], new BitMatrix(0, 0));
	}

	public DoubleBinaryMatrix(
			String title,
			String[] colNames,
			String[] rowNames,
			BitMatrix cells) {

		super(
				title,
				ObjectFactory1D.dense.make(rowNames),
				ObjectFactory1D.dense.make(colNames),
				new DoubleElementAdapter());

		this.cells = cells;
	}

	@Override
	public int getRowCount() {
		return cells.rows();
	}

	@Override
	public int getColumnCount() {
		return cells.columns();
	}

	// Cells

	/*public BitMatrix getCells() {
		return cells;
	}

	public void setCells(BitMatrix cells) {
		this.cells = cells;
	}*/

	@Override
	public Object getCell(int row, int column) {
		if (cellsNan.getQuick(column, row))
			return Double.NaN;
		else
			return cells.getQuick(column, row) ? 1.0 : 0.0;
	}

	@Override
	public Object getCellValue(int row, int column, int index) {
		return getCell(row, column);
	}

	@Override
	public void setCellValue(int row, int column, int index, Object value) {
		cells.putQuick(column, row, ((Double) value) == 1.0);
		cellsNan.putQuick(column, row, Double.isNaN((Double) value));
	}

	@Override
	public void makeCells(int rows, int columns) {
		cells = new BitMatrix(columns, rows);
		cells.clear();

		cellsNan = new BitMatrix(columns, rows);
		cellsNan.clear();

		if (this.rows == null || this.rows.cardinality() != rows)
			setRows(ObjectFactory1D.dense.make(rows));

		if (this.columns == null || this.columns.cardinality() != columns)
			setColumns(ObjectFactory1D.dense.make(columns));
	}
}
