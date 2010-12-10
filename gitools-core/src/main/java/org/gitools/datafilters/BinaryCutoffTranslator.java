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

import cern.colt.function.DoubleFunction;

public class BinaryCutoffTranslator	implements ValueTranslator<Double> {

	private static final long serialVersionUID = 4964176171201274622L;

	protected DoubleFunction filter;
	
	public BinaryCutoffTranslator(DoubleFunction filter) {
		this.filter = filter;
	}
	
	@Override
	public Double stringToValue(String str) {
		double value = Double.NaN;
		try {
			value = Double.parseDouble(str);
			value = filter.apply(value);
		}
		catch (NumberFormatException e) {}
		return value;
	}

	@Override
	public String valueToString(Double value) {
		if (Double.isNaN(value))
			return "-";

		String str = String.valueOf(value);
		double frac = value - Math.floor(value);
		if (frac == 0.0)
			str = str.substring(0, str.length() - 2);
		return str;
	}

}
