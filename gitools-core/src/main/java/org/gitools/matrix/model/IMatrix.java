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

import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.IResource;

import java.util.List;

public interface IMatrix extends IResource {

	// rows
	
	int getRowCount();
	String getRowLabel(int index);
	
	// columns
	
	int getColumnCount();
	String getColumnLabel(int index);
	
	// cells

	@Deprecated
	Object getCell(int row, int column);

	Object getCellValue(int row, int column, int index);
	Object getCellValue(int row, int column, String id);
	void setCellValue(int row, int column, int index, Object value);
	void setCellValue(int row, int column, String id, Object value);

	@Deprecated
	IElementAdapter getCellAdapter();
	
	List<IElementAttribute> getCellAttributes();
	int getCellAttributeIndex(String id);
}
