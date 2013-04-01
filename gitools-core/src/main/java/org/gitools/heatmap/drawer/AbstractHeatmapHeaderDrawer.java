/*
 * #%L
 * gitools-core
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
package org.gitools.heatmap.drawer;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapHeader;

import java.awt.*;

public abstract class AbstractHeatmapHeaderDrawer<HT extends HeatmapHeader> extends AbstractHeatmapDrawer
{

    protected static final Color highlightingColor = Color.YELLOW;

    protected HT header;
    protected boolean horizontal;

    public AbstractHeatmapHeaderDrawer(Heatmap heatmap, HT header, boolean horizontal)
    {
        super(heatmap);

        this.header = header;
        this.horizontal = horizontal;
    }

    public HT getHeader()
    {
        return header;
    }


    public void drawHeaderLegend(Graphics2D g, Rectangle headerIntersection, HeatmapHeader heatmapHeader)
    {
        return;
    }

    ;

    public boolean isHorizontal()
    {
        return horizontal;
    }
}
