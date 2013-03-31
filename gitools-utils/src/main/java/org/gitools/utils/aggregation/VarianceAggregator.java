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

import cern.jet.math.Functions;

import static cern.jet.stat.Descriptive.sampleVariance;

/** Sum of logarithms */
public class VarianceAggregator extends AbstractAggregator {

    public final static IAggregator INSTANCE = new VarianceAggregator();

    private VarianceAggregator() {
    }

	@Override
	public double aggregate(double[] data) {
        int size = data.length;
        double sum = aggregate(data, Functions.plus,0);
        double sumOfSquares = aggregate(data,Functions.plus,Functions.square,0);
        return sampleVariance(size,sum,sumOfSquares);
	}

	@Override
	public String toString() {
		return "Variance";
	}
}
