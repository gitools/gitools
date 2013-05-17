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
package org.gitools.ui.actions.data;

import org.apache.commons.lang.ArrayUtils;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.label.LabelProvider;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.actions.BaseAction;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;


public class HideEmptyLabelHeaderAction extends BaseAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;

    public HideEmptyLabelHeaderAction() {
        super("Hide empty values in header");
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        List<Integer> toHide = new ArrayList<>();
        HeatmapDimension dimension = header.getHeatmapDimension();
        LabelProvider labelProvider = header.getLabelProvider();
        for (int i=0; i < dimension.size(); i++) {
            String value = labelProvider.getLabel(i);
            if (value == null || value.isEmpty()) {
                toHide.add(i);
            }
        }

        if (toHide.size() < dimension.size()) {
            dimension.hide(ArrayUtils.toPrimitive(toHide.toArray(new Integer[toHide.size()])));
        }
    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {
        setName("Hide empty values in '" + header.getTitle() + "'");
        this.header = header;
    }
}
