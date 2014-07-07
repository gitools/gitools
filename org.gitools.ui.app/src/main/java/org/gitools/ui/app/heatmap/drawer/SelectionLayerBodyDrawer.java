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
package org.gitools.ui.app.heatmap.drawer;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.decorator.Decoration;

import java.awt.*;

public class SelectionLayerBodyDrawer extends HeatmapLayerBodyDrawer {

    public SelectionLayerBodyDrawer(Heatmap heatmap) {
        super(heatmap);
    }

    @Override
    public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

        if (!isPictureMode()) {
            drawSelectedHighlightedAndFocus(g, box, rows, true, HIGHLIGHT_POLICY_AVOID);
            drawSelectedHighlightedAndFocus(g, box, columns, false, HIGHLIGHT_POLICY_AVOID);
        }

    }

    protected void decorateCell(Decoration decoration, int row, int col) {

    }
}
