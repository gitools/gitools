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
