/*
 * #%L
 * gitools-ui-platform
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
package org.gitools.ui.platform.idea;

import java.awt.*;
import java.util.Map;

public class GraphicsUtil {

    public static void setupAntialiasing(java.awt.Graphics g2) {
        setupAntialiasing(g2, true, false);
    }

    public static void setupAntialiasing(Graphics g2, boolean enableAA, boolean ignoreSystemSettings) {
        if (g2 instanceof Graphics2D) {
            Graphics2D g = (Graphics2D)g2;
            Toolkit tk = Toolkit.getDefaultToolkit();
            //noinspection HardCodedStringLiteral
            Map map = (Map)tk.getDesktopProperty("awt.font.desktophints");

            if (map != null && !ignoreSystemSettings) {
                g.addRenderingHints(map);
            } else {
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        enableAA ? RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            }
        }
    }

}
