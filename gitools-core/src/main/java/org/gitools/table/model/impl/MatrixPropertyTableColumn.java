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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.table.model.Table;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixPropertyTableColumn extends MatrixCellTableColumn {

	private static final long serialVersionUID = 7454639163784197446L;
	
	protected int property;

	public MatrixPropertyTableColumn() {
		super();
	}

	public MatrixPropertyTableColumn(int column, Table table, int property) {
		super(column, table);
		this.property = property;
	}

	@Override
	public Object getValue(int row) {
		return table.getMatrix().getCellValue(row, column, property);
	}

}
