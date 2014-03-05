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
package org.gitools.utils.colorscale;

import java.awt.*;

public class ColorConstants {

    public static final Color notANumberColor = Color.WHITE;
    public static final Color posInfinityColor = Color.GREEN;
    public static final Color negInfinityColor = Color.CYAN;
    public static final Color emptyColor = Color.WHITE;

    public static final Color nonSignificantColor = new Color(187, 187, 187);

    public static final Color minColor = new Color(255, 0, 0);
    public static final Color maxColor = new Color(255, 255, 0);

    public static final Color binaryMinColor = nonSignificantColor;
    public static final Color binaryMaxColor = new Color(20, 120, 250);
}
