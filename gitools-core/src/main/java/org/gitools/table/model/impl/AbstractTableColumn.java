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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.gitools.table.model.Table;

@XmlSeeAlso({
	AnnotationTableColumn.class, 
	MatrixCellTableColumn.class, 
	MatrixPropertyTableColumn.class})

	@XmlAccessorType(XmlAccessType.FIELD)
public class AbstractTableColumn {
	// internal column, 
	// reference of a Table or Matrix column
	protected int column;
	
	@XmlTransient
	protected Table table;

	public AbstractTableColumn() {
		super();
	}

	public AbstractTableColumn(int column, Table table) {
		this.column = column;
		this.table = table;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
}
