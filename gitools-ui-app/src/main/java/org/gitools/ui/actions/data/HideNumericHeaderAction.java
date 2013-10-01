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
import org.gitools.core.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.actions.BaseAction;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;


public class HideNumericHeaderAction extends BaseAction implements IHeatmapHeaderAction {

    private boolean greater;
    private String title;

    private double thresholdValue;
    private HeatmapDecoratorHeader header;

    public HideNumericHeaderAction(boolean greater, String title) {
        super(title);
        this.greater = greater;
        this.title = title;
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (header == null) {
            return;
        }

        HeatmapDimension dimension = header.getHeatmapDimension();
        List<Integer> toHide = new ArrayList<>();
        for (int i = 0; i < dimension.size(); i++) {
            String value = getAnnotationValue(i);

            try {
                double numericValue = Double.parseDouble(value);

                if ((greater && numericValue > thresholdValue) || (!greater && numericValue < thresholdValue)) {
                    toHide.add(i);
                }

            } catch (NumberFormatException ex) {
            }

        }

        if (toHide.size() < dimension.size()) {
            dimension.hide(ArrayUtils.toPrimitive(toHide.toArray(new Integer[toHide.size()])));
        }

    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {

        if (!(header instanceof HeatmapDecoratorHeader)) {
            setEnabled(false);
            return;
        }

        setEnabled(true);

        this.header = (HeatmapDecoratorHeader) header;
        this.header.setSortLabel(position.getHeaderAnnotation());

        try {
            this.thresholdValue = Double.parseDouble(position.headerDecoration.getValue());
        } catch (NumberFormatException e) {
            setEnabled(false);
        }

        setName("Hide " + title + " than '" + position.headerDecoration.getFormatedValue() + "'");
    }

    private String getAnnotationValue(int index) {
        HeatmapDimension dimension = header.getHeatmapDimension();

        if (index < 0 || index >= dimension.size()) {
            return "NA";
        }

        String identifier = dimension.getLabel(index);
        return header.getHeatmapDimension().getAnnotations().getAnnotation(identifier, header.getSortLabel());
    }
}
