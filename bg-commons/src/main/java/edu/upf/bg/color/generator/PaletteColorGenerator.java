/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package edu.upf.bg.color.generator;

import java.awt.Color;

public class PaletteColorGenerator implements ColorGenerator {

	private static final int[] DEFAULT_PALETTE = {
		0x4bb2c5, 0xEAA228, 0xc5b47f, 0x546D61, 0x958c12,
		0x953579, 0xc12e2e, 0x4b5de4, 0xd8b83f, 0xff5800,
		0x0085cc, 0xc747a3, 0xcddf54, 0xFBD178, 0x26B4E3,
		0xbd70c7, 0xabdbeb, 0x40D800, 0x8AFF00, 0xD9EB00,
		0xFFFF71, 0x777B00,

		0x498991, 0xC08840, 0x9F9274, 0x579575, 0x646C4A,
		0x6F6621, 0x6E3F5F, 0x4F64B0, 0xA89050, 0xC45923,
		0x187399, 0x945381, 0x959E5C, 0xAF5714, 0x478396,
		0x907294, 0x426c7a, 0x878166, 0xAEA480, 0xFFFFD3,
		0xE9D5A4, 0xA29877};

	private Color[] palette;
	private int index = -1;

	public PaletteColorGenerator() {
		this(DEFAULT_PALETTE);
	}

	public PaletteColorGenerator(int[] palette) {
		this.palette = new Color[palette.length];
		for (int i = 0; i < palette.length; i++)
			this.palette[i] = new Color(palette[i]);
	}

	public PaletteColorGenerator(Color[] palette) {
		this.palette = palette;
	}

	@Override
	public void reset() {
		index = -1;
	}

	@Override
	public Color next() {
		index++;

		return palette[index % palette.length];
	}

	@Override
	public int getCount() {
		return index;
	}

}
