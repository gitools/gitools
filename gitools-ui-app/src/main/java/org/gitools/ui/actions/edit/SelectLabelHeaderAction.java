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
package org.gitools.ui.actions.edit;

import org.apache.commons.lang.StringUtils;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.heatmap.header.ColoredLabel;
import org.gitools.core.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.actions.BaseAction;

import java.awt.event.ActionEvent;


public class SelectLabelHeaderAction extends BaseAction implements IHeatmapHeaderAction {

    private String annotationValue;
    private HeatmapColoredLabelsHeader coloredHeader;

    public SelectLabelHeaderAction() {
        super("Add label header to selection");
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (annotationValue == null) {
            return;
        }

        HeatmapDimension dimension = coloredHeader.getHeatmapDimension();

        for (String identifier :  dimension) {

            String value = coloredHeader.getColoredLabel(identifier).getValue();

            if (StringUtils.equals(value, annotationValue)) {
                dimension.getSelected().add(identifier);
            }
        }

    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {
        setEnabled(header instanceof HeatmapColoredLabelsHeader);

        if (header instanceof HeatmapColoredLabelsHeader) {

            coloredHeader = (HeatmapColoredLabelsHeader) header;
            annotationValue = position.getHeaderAnnotation();

            ColoredLabel coloredLabel = coloredHeader.getAssignedColoredLabel(annotationValue);

            setName("Select all '" + (coloredLabel == null ? annotationValue : coloredLabel.getDisplayedLabel()) + "' labels");
        }

    }
}
