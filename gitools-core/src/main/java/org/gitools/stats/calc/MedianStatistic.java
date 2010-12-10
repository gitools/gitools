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

import java.util.Arrays;

import cern.colt.matrix.DoubleMatrix1D;

public class MedianStatistic implements Statistic {

	@Override
	public String getName() {
		return "median";
	}
	
	@Override
	public double calc(DoubleMatrix1D values) {
		
		final int size = values.size();
		
		if (size == 0)
			return Double.NaN;
		else if (size == 1)
			return values.getQuick(0);
		
		double[] tmp = new double[size];

		values.toArray(tmp);
		
		Arrays.sort(tmp);

		final int middle = size / 2;
		double median = tmp[middle];
		
		if (size % 2 == 0)
			median = (tmp[middle - 1] + median) / 2.0;
		
		return median;
	}

}
