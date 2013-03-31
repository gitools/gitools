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

package org.gitools.utils.aggregation;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.function.DoubleFunction;

public abstract class AbstractAggregator implements IAggregator {

	protected double aggregate(double[] data, DoubleDoubleFunction reduceFunc, DoubleFunction mapFunc, double nanValue) {
		if (data.length == 0)
			return 0;
		else if (data.length == 1)
			return mapFunc.apply(checkNaN(data[0], nanValue));

		double value = reduceFunc.apply(
				mapFunc.apply(checkNaN(data[0], nanValue)),
				mapFunc.apply(checkNaN(data[1], nanValue)));

		for (int i = 2; i < data.length; i++)
			value = reduceFunc.apply(value, mapFunc.apply(checkNaN(data[i], nanValue)));

		return value;
	}

	protected double aggregate(double[] data, DoubleDoubleFunction reduceFunc, double nanValue) {
		if (data.length == 0)
			return 0;
		else if (data.length == 1)
			return checkNaN(data[0], nanValue);

		double value = reduceFunc.apply(
				checkNaN(data[0], nanValue),
				checkNaN(data[1], nanValue));

		for (int i = 2; i < data.length; i++)
			value = reduceFunc.apply(value, checkNaN(data[i], nanValue));

		return value;
	}

	private double checkNaN(double d, double nanValue) {
		return Double.isNaN(d) ? nanValue : d;
	}
}
