/*
 * #%L
 * gitools-utils
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.utils.color;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorGenerator {

    private static final int[] DEFAULT_PALETTE = {0x4bb2c5, 0xEAA228, 0xc5b47f, 0x546D61, 0x958c12, 0x953579, 0xc12e2e, 0x4b5de4, 0xd8b83f, 0xff5800, 0x0085cc, 0xc747a3, 0xcddf54, 0xFBD178, 0x26B4E3, 0xbd70c7, 0xabdbeb, 0x40D800, 0x8AFF00, 0xD9EB00, 0xFFFF71, 0x777B00,

            0x498991, 0xC08840, 0x9F9274, 0x579575, 0x646C4A, 0x6F6621, 0x6E3F5F, 0x4F64B0, 0xA89050, 0xC45923, 0x187399, 0x945381, 0x959E5C, 0xAF5714, 0x478396, 0x907294, 0x426c7a, 0x878166, 0xAEA480, 0xFFFFD3, 0xE9D5A4, 0xA29877};

    private Color[] palette;
    private int index = -1;
    private ColorRegistry colorRegistry;

    private ArrayList<Color> used = new ArrayList<>();

    public ColorGenerator() {
        this(DEFAULT_PALETTE);
    }

    private ColorGenerator(int[] palette) {
        this.palette = new Color[palette.length];
        for (int i = 0; i < palette.length; i++)
            this.palette[i] = new Color(palette[i]);
        colorRegistry = ColorRegistry.get();
        int max = palette.length - 1;
        int min = 0;
        index = new Random().nextInt((max - min) + 1) + min;
    }

    public void reset() {
        index = -1;
    }

    private Color next() {
        Color c = null;
        while (isUsed(c)) {
            index++;
            c = palette[index % palette.length];
        }
        return c;
    }

    public Color next(String id) {
        Color c = colorRegistry.getColor(id);
        if (c == null || isUsed(c)) {
            c = next();
            colorRegistry.registerId(id, c);
        }
        if (!used.contains(c)) {
            used.add(c);
        }
        return c;
    }

    private boolean isUsed(Color c) {

        if (c == null) {
            return true;
        }

        if (used.contains(c)) {
            if (used.size() == palette.length) {
                //all colors have been used, start at the beginning;
                used.clear();
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void initUsed(List<Color> colors) {
        used.addAll(colors);
    }

    public int getCount() {
        return index;
    }

}
