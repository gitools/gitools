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

package org.gitools.matrix;

import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.IResourceLocator;

import java.util.List;

public class DiagonalMatrix implements IMatrix {

	private IMatrix m;

    private IResourceLocator locator;

	public DiagonalMatrix(IMatrix matrix) {
		this.m = matrix;
	}

    public IResourceLocator getLocator() {
        return locator;
    }

    public void setLocator(IResourceLocator locator) {
        this.locator = locator;
    }

    @Override
	public int getRowCount() {
		return m.getRowCount();
	}

	@Override
	public String getRowLabel(int index) {
		return m.getRowLabel(index);
	}

	@Override
	public int getColumnCount() {
		return m.getColumnCount();
	}

	@Override
	public String getColumnLabel(int index) {
		return m.getColumnLabel(index);
	}

	@Override
	public Object getCell(int row, int column) {
		if (column < row) {
			int tmp = column;
			column = row;
			row = tmp;
		}
		return m.getCell(row, column);
	}

	@Override
	public Object getCellValue(int row, int column, int index) {
		if (column < row) {
			int tmp = column;
			column = row;
			row = tmp;
		}
		return m.getCellValue(row, column, index);
	}

	@Override
	public Object getCellValue(int row, int column, String id) {
		if (column < row) {
			int tmp = column;
			column = row;
			row = tmp;
		}
		return m.getCellValue(row, column, id);
	}

	@Override
	public void setCellValue(int row, int column, int index, Object value) {
		if (column < row) {
			int tmp = column;
			column = row;
			row = tmp;
		}

		m.setCellValue(row, column, index, value);
	}

	@Override
	public void setCellValue(int row, int column, String id, Object value) {
		if (column < row) {
			int tmp = column;
			column = row;
			row = tmp;
		}
		m.setCellValue(row, column, id, value);
	}

	@Override
	public IElementAdapter getCellAdapter() {
		return m.getCellAdapter();
	}

	@Override
	public List<IElementAttribute> getCellAttributes() {
		return m.getCellAttributes();
	}

	@Override
	public int getCellAttributeIndex(String id) {
		return m.getCellAttributeIndex(id);
	}

}
