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

package org.gitools.stats.mtc;

import cern.colt.matrix.DoubleMatrix1D;

public class BenjaminiHochbergFdr implements MTC {

    public static String SHORT_NAME = "bh";

	@Override
	public String getName() {
		return "Benjamini Hochberg FDR";
	}

    @Override
    public String getShortName() {
        return SHORT_NAME;
    }

    @Override
	public void correct(final DoubleMatrix1D values) {
		
		DoubleMatrix1D sortedValues = values.viewSorted();
		
		int m = sortedValues.size();
		for (int idx = 0; idx < m; idx++) {
			int rank = idx + 1;
			double p = sortedValues.get(idx);
			sortedValues.set(idx, Math.min(1.0, p * m / rank));
		}
	}
}
