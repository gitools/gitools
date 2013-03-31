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

package org.gitools.utils.color.utils;

import java.awt.color.ColorSpace;

public class HLSColorSpace extends ColorSpace {


	public HLSColorSpace() {
		super(ColorSpace.TYPE_HLS, 3);
	}

	@Override
	public float[] toRGB(float[] colorvalue) {

		/**
		* This sample code is made available as part of the book "Digital Image
		* Processing - An Algorithmic Introduction using Java" by Wilhelm Burger
		* and Mark J. Burge, Copyright (C) 2005-2008 Springer-Verlag Berlin,
		* Heidelberg, New York.
		* Note that this code comes with absolutely no warranty of any kind.
		 * License
		 * This software is released under the terms of the GNU Lesser General Public License (LGPL).
		 * See http://www.imagingbook.com for details and licensing conditions.
		*
		* Date: 2007/11/10
		*/


		// H,L,S assumed to be in [0,1]
		float R = 0;
		float G = 0;
		float B = 0;
		float H = colorvalue[0];
		float L = colorvalue[1];
		float S = colorvalue[2];
		if (L <= 0) {
			R = G = B = 0;
		} else if (L >= 1) {
			R = G = B = 1;
		} else {
			float hh = (6 * H) % 6;
			int c1 = (int) hh;
			float c2 = hh - c1;
			float d = (L <= 0.5F) ? (S * L) : (S * (1 - L));
			float w = L + d;
			float x = L - d;
			float y = w - (w - x) * c2;
			float z = x + (w - x) * c2;
			switch (c1) {
				case 0:
					R = w;
					G = z;
					B = x;
					break;
				case 1:
					R = y;
					G = w;
					B = x;
					break;
				case 2:
					R = x;
					G = w;
					B = z;
					break;
				case 3:
					R = x;
					G = y;
					B = w;
					break;
				case 4:
					R = z;
					G = x;
					B = w;
					break;
				case 5:
					R = w;
					G = x;
					B = y;
					break;
			}
		}
		return new float[]{R, G, B};
	}

	@Override
	public float[] fromRGB(float[] rgbvalue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float[] toCIEXYZ(float[] colorvalue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float[] fromCIEXYZ(float[] colorvalue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
