/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.heatmap.panel;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.drawer.header.HeatmapHeaderDrawer;
import org.gitools.heatmap.drawer.header.HeatmapHeaderIntersectionDrawer;
import org.gitools.heatmap.header.HeatmapHeader;

import java.beans.PropertyChangeEvent;

public class HeatmapHeaderIntersectionPanel extends AbstractHeatmapPanel
{

    private static final long serialVersionUID = 930370133535101914L;

    public HeatmapHeaderIntersectionPanel(Heatmap heatmap, HeatmapHeaderDrawer columnHeaderDrawer, HeatmapHeaderDrawer rowHeaderDrawer)
    {
        super(heatmap, new HeatmapHeaderIntersectionDrawer(heatmap, columnHeaderDrawer, rowHeaderDrawer));

    }


    private HeatmapHeaderIntersectionDrawer getHeaderDrawer()
    {
        return (HeatmapHeaderIntersectionDrawer) getDrawer();
    }

    @Override
    protected void heatmapPropertyChanged(PropertyChangeEvent evt)
    {
        super.heatmapPropertyChanged(evt);

        String pname = evt.getPropertyName();
        Object src = evt.getSource();


        if (src instanceof HeatmapDim && HeatmapDim.HEADERS_CHANGED.equals(pname))
        {
            getHeaderDrawer().updateDrawers((HeatmapDim) src);
        }
        else if (src instanceof HeatmapHeader && HeatmapHeader.VISIBLE_CHANGED.equals(pname))
        {
            getHeaderDrawer().updateDrawers(((HeatmapHeader) src).getHeatmapDim());
        }
    }
}

