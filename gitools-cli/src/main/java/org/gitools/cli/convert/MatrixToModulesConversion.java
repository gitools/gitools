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

package org.gitools.cli.convert;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.ModuleMap;


public class MatrixToModulesConversion implements ConversionDelegate {

	@Override
	public Object convert(String srcMime, Object src, String dstMime, IProgressMonitor monitor) throws Exception {
		final int attrIndex = 0; // TODO get from configuration
		
		IMatrix matrix = (IMatrix) src;

		Map<String, Set<String>> map = new HashMap<String, Set<String>>();

		MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(
				matrix.getCellAttributes().get(attrIndex).getValueClass());

		int numRows = matrix.getRowCount();
		int numCols = matrix.getColumnCount();
		for (int row = 0; row < numRows; row++) {
			String rowLabel = matrix.getRowLabel(row);
			Set<String> items = new HashSet<String>();
			for (int col = 0; col < numCols; col++) {
				String colLabel = matrix.getColumnLabel(col);
				Object cellValue = matrix.getCellValue(row, col, attrIndex);
				double value = cast.getDoubleValue(cellValue);
				if (value == 1.0)
					items.add(colLabel);
			}
			map.put(rowLabel, items);
		}
		ModuleMap mmap = new ModuleMap(map);
		return mmap;
	}

}
