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

package org.gitools.utils.colorscale.util;

import java.awt.Color;

public class ColorConstants {

	public static final Color notANumberColor = Color.WHITE;
	public static final Color posInfinityColor = Color.GREEN;
	public static final Color negInfinityColor = Color.CYAN;
	public static final Color undefinedColor = Color.BLACK;
	public static final Color emptyColor = Color.WHITE;
	
	public static final Color nonSignificantColor = new Color(187, 187, 187);
	
	public static final Color minColor = new Color(255, 0, 0);
	public static final Color maxColor = new Color(255, 255, 0);

    public static final Color binaryMinColor = nonSignificantColor;
    public static final Color binaryMaxColor = new Color(20, 120, 250);
}
