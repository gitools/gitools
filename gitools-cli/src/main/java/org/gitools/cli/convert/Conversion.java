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

package org.gitools.cli.convert;

class Conversion {

	public String src;
	public String dst;

	public ConversionDelegate delegate;

	public Conversion(String src, String dst) {
		this(src, dst, null);
	}

	public Conversion(String src, String dst, ConversionDelegate delegate) {
		this.src = src;
		this.dst = dst;
		this.delegate = delegate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (!(obj instanceof Conversion)) {
			return false;
		}
		Conversion o = (Conversion) obj;
		return o.src.equals(this.src) && o.dst.equals(this.dst);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + (this.src != null ? this.src.hashCode() : 0);
		hash = 89 * hash + (this.dst != null ? this.dst.hashCode() : 0);
		return hash;
	}
}
