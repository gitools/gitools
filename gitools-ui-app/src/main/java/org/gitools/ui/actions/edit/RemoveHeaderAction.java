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

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.core.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.ui.actions.HeatmapDimensionAction;
import org.gitools.ui.heatmap.drawer.HeatmapPosition;
import org.gitools.ui.heatmap.header.wizard.coloredlabels.ColoredLabelsHeaderWizard;
import org.gitools.ui.heatmap.header.wizard.heatmapheader.DecoratorHeaderWizard;
import org.gitools.ui.heatmap.header.wizard.textlabels.TextLabelsHeaderWizard;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.wizard.IWizard;
import org.gitools.ui.platform.wizard.WizardDialog;

import java.awt.event.ActionEvent;

public class RemoveHeaderAction extends HeatmapDimensionAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;

    public RemoveHeaderAction(MatrixDimensionKey dimensionKey, String name) {
        super(dimensionKey, name);
    }

    public RemoveHeaderAction(HeatmapHeader header) {
        super(header.getHeatmapDimension().getId(), header.getTitle());

        this.header = header;
    }

    public HeatmapHeader getHeader() {
        return header;
    }

    public void setHeader(HeatmapHeader header) {
        this.header = header;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        HeatmapDimension heatmapDimension = getDimension();
        heatmapDimension.getHeaders().remove(header);
        heatmapDimension.updateHeaders();

    }

    @Override
    public void onConfigure(HeatmapHeader object, HeatmapPosition position) {
        setHeader(object);
    }
}
