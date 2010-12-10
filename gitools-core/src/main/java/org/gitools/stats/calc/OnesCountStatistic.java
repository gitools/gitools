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

package org.gitools.stats.calc;

import cern.colt.matrix.DoubleMatrix1D;

public class OnesCountStatistic implements Statistic {

	@Override
	public String getName() {
		return "count-of-ones";
	}
	
	@Override
	public double calc(DoubleMatrix1D values) {
		int size = values.size();
		int count = 0;
		
		for (int i = 0; i < size; i++)
			count += values.getQuick(i) == 1.0 ? 1 : 0;
		
		return (double) count;
	}

}
