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

package org.gitools.matrix.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.gitools.matrix.model.element.StringElementAdapter;

import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;

//TODO remove JAXB support
@XmlAccessorType(XmlAccessType.NONE)

public class StringMatrix extends ObjectMatrix {

	private static final long serialVersionUID = 5061265701379494159L;

	public StringMatrix() {
		super();
		
		setCellAdapter(new StringElementAdapter());
	}
	
	public StringMatrix(
			String title,
			ObjectMatrix1D rows,
			ObjectMatrix1D columns,
			ObjectMatrix2D cells) {
	    
		super(title, rows, columns, cells, new StringElementAdapter());
	}
	
	@Override
	public void makeCells() {
		cells = ObjectFactory2D.dense.make(
				rows.size(),
				columns.size());
	}
	
	@XmlTransient
	@Override
	public int getRowCount() {
		return cells.rows();
	}
	
	@XmlTransient
	@Override
	public int getColumnCount() {
		return cells.columns();
	}
	
	@Override
	public String getCell(int row, int column) {
		return (String) cells.get(row, column);
	}
	
	public void setCell(int row, int column, String cell) {
		cells.set(row, column, cell);
	}
}
