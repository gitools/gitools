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
package org.gitools.ui.app.actions.edit;

import org.apache.commons.lang.StringUtils;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.AbstractAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;

import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class UnselectLabelHeaderAction extends AbstractAction implements IHeatmapHeaderAction {

    private String annotationValue;
    private HeatmapColoredLabelsHeader coloredHeader;

    public UnselectLabelHeaderAction() {
        super("<html><i>Remove</i> label header from selection</html>");
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

        ArrayList<String> removeFromSelected = new ArrayList();

        for (String identifier : dimension) {

            String value = coloredHeader.getColoredLabel(identifier).getValue();

            if (StringUtils.equals(value, annotationValue)) {
                removeFromSelected.add(identifier);
            }
        }
        if (removeFromSelected.size() > 0) {
            int selectedBefore = dimension.getSelected().size();
            dimension.getSelected().removeAll(removeFromSelected);
            int selectedDifference = Math.abs(selectedBefore - dimension.getSelected().size());
            Application.get().showNotification("Unselected " + selectedDifference + " " + dimension.getId().getLabel() + "s");
        } else {
            Application.get().showNotification("Removed none from selected");
        }

    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {

        if(header instanceof HierarchicalClusterHeatmapHeader) {
            header = ((HierarchicalClusterHeatmapHeader) header).getInteractionLevelHeader();
        }

        HeatmapDimension dimension = header.getHeatmapDimension();
        setEnabled(header instanceof HeatmapColoredLabelsHeader &&
                dimension.getSelected().contains(position.get(dimension)));

        if (header instanceof HeatmapColoredLabelsHeader) {

            coloredHeader = (HeatmapColoredLabelsHeader) header;
            annotationValue = position.getHeaderAnnotation();

            ColoredLabel coloredLabel = coloredHeader.getAssignedColoredLabel(annotationValue);
            String label = coloredHeaderLabel(coloredHeader, coloredLabel);

            setName("<html><i>Unselect all</i> <b>" + label + "</b> labels</html>");
        }

    }
}
