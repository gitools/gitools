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

public class DoubleTranslator implements ValueTranslator<Double> {

	@Override
	public Double stringToValue(String str) {
		return stringToValue(str,true);
	}

    public Double stringToValue(String str, boolean allowNull) {
        if (allowNull) {
            if (str == null || str.isEmpty())
                return null;
        }

        double value = Double.NaN;
        try {
            value = Double.parseDouble(str);
        }
        catch (NumberFormatException e) {}
        return value;
    }

	@Override
	public String valueToString(Double value) {
		if (value == null)
			return "";
		
		if (Double.isNaN(value))
			return "-";

		String s = String.valueOf(value);
		return s.endsWith(".0") ? s.substring(0, s.length() - 2) : s;
	}

}
