/*
 *  Copyright 2010 cperez.
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

package org.gitools.heatmap.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;


public class HeatmapMatrixViewXmlAdapter extends XmlAdapter<MatrixView, IMatrixView> {

	@Override
	public IMatrixView unmarshal(MatrixView v) throws Exception {
		return v;
	}

	@Override
	public MatrixView marshal(IMatrixView v) throws Exception {
		return new MatrixView(v);
	}

}
