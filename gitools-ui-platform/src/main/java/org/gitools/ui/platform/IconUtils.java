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
package org.gitools.ui.platform;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class IconUtils
{

    public static final String nullResourceImage = "/img/null.gif";

    @NotNull
    public static Icon getIconResource(String name)
    {
        return getImageIconResource(name);
    }

    @NotNull
    public static Image getImageResource(String name)
    {
        Icon icon = getImageIconResource(name);
        return iconToImage(icon, icon.getIconWidth(), icon.getIconHeight());
    }

    @NotNull
    public static ImageIcon getImageIconResource(String name)
    {
        URL url = IconUtils.class.getResource(name);
        if (url == null)
        {
            url = IconUtils.class.getResource(nullResourceImage);
        }

        return new ImageIcon(url);
    }

    @NotNull
    public static ImageIcon getImageIconResourceScaledByHeight(String name, int height)
    {

        ImageIcon icon = getImageIconResource(name);
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        double ratio = (double) height / (double) h;
        int width = (int) Math.floor(w * ratio);

        return new ImageIcon(iconToImage(icon, width, height));
    }

    private static Image iconToImage(@NotNull Icon icon, int width, int height)
    {

        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        boolean sameSize = w == width && h == height;

        if (icon instanceof ImageIcon && sameSize)
        {
            return ((ImageIcon) icon).getImage();
        }
        else
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h, Transparency.BITMASK /*TRANSLUCENT*/);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            if (sameSize)
            {
                return image;
            }
            else
            {
                return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            }
        }
    }
}
