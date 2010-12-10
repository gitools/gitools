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

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
public class AnnotationMatrix extends StringMatrix {

	private static final long serialVersionUID = 2941738380629859631L;

	public static class Annotation {
		private String key;
		private String value;

		public Annotation(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}

	private Map<String, Integer> rowMap;
	private Map<String, Integer> colMap;
	
	public AnnotationMatrix() {
		super();
	}
	
	public AnnotationMatrix(
			String title,
			ObjectMatrix1D rows,
			ObjectMatrix1D columns,
			ObjectMatrix2D cells) {
	    
		super(title, rows, columns, cells);
		
		updateRowsMap();
		updateColumnsMap();
	}
	
	@Override
	public void setRows(ObjectMatrix1D rows) {
		super.setRows(rows);
		updateRowsMap();
	}
	
	@Override
	public void setColumns(ObjectMatrix1D columns) {
		super.setColumns(columns);
		updateColumnsMap();
	}
	
	public int getRowIndex(String id) {
		int index = -1;
		Integer idx = rowMap.get(id);
		if (idx != null)
			index = idx.intValue();
		return index;
	}
	
	public int getColumnIndex(String id) {
		int index = -1;
		Integer idx = colMap.get(id);
		if (idx != null)
			index = idx.intValue();
		return index;
	}

	public List<Annotation> getAnnotations(String label) {
		List<Annotation> ann = new ArrayList<Annotation>();
		int index = getRowIndex(label);
		if (index >= 0) {
			int numAnn = getColumnCount();
			for (int i = 0; i < numAnn; i++) {
				String value = getCell(index, i);
				if (value != null) {
					String key = getColumnLabel(i);
					ann.add(new Annotation(key, value));
				}
			}
		}
		return ann;
	}

	private void updateRowsMap() {
		if (rowMap == null)
			rowMap = new HashMap<String, Integer>();
		
		rowMap.clear();
		
		for (int i = 0; i < rows.size(); i++)
			rowMap.put(rows.getQuick(i).toString(), i);
	}
	
	private void updateColumnsMap() {
		if (colMap == null)
			colMap = new HashMap<String, Integer>();
		
		colMap.clear();
		
		for (int i = 0; i < columns.size(); i++)
			colMap.put(columns.getQuick(i).toString(), i);
	}
}
