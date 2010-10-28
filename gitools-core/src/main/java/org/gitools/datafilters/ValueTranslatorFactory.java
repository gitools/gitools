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

package org.gitools.datafilters;


public class ValueTranslatorFactory {

	public static ValueTranslator createValueTranslator(Class<?> cls) {
		ValueTranslator vt = null;

		if (double.class.equals(cls) || Double.class.equals(cls))
			vt = new DoubleTranslator();
		else if (int.class.equals(cls) || Integer.class.equals(cls))
			vt = new IntegerTranslator();
		else
			vt = new ValueTranslator() {
				@Override public Object stringToValue(String str) {
					return str; }

				@Override public String valueToString(Object value) {
					return value != null ? value.toString() : ""; } };

		return vt;
	}
}
