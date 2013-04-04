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
package org.gitools.utils.colorscale.drawer;

import org.gitools.utils.colorscale.IColorScale;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ColorScalePanel extends JPanel
{

    private IColorScale scale;
    private ColorScaleDrawer drawer;

    public ColorScalePanel(IColorScale scale)
    {
        setScale(scale);
    }

    public IColorScale getScale()
    {
        return scale;
    }

    public void setScale(IColorScale scale)
    {
        this.scale = scale;

        this.drawer = new ColorScaleDrawer(scale);
        setPreferredSize(this.drawer.getSize());

        repaint();
    }

    public void update()
    {
        drawer.resetZoom();

        setPreferredSize(drawer.getSize());
        repaint();
    }

    @Override
    protected void paintComponent(@NotNull Graphics g)
    {
        super.paintComponent(g);

        Dimension size = getSize();
        Rectangle box = new Rectangle(0, 0, size.width, size.height);
        Rectangle clip = g.getClipBounds();
        drawer.draw((Graphics2D) g, box, clip);
    }
}
