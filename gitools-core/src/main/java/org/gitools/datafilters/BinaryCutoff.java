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

package org.gitools.datafilters;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import java.io.Serializable;

import cern.colt.function.DoubleFunction;

public class BinaryCutoff implements DoubleFunction, Serializable {

	private static final long serialVersionUID = 5091376519840044515L;

	protected CutoffCmp cmp;
	protected double cutoff;
	
	public BinaryCutoff(CutoffCmp cmp, double cutoff) {
		this.cmp = cmp;
		this.cutoff = cutoff;
	}

	@Override
	public double apply(double value) {
		return Double.isNaN(value) ? Double.NaN :
			cmp.compare(value, cutoff) ? 1 : 0;
	}
}
