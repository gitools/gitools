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

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.app.heatmap.header.AddHeaderPage;
import org.gitools.ui.app.heatmap.header.wizard.coloredlabels.ColoredLabelsHeaderWizard;
import org.gitools.ui.app.heatmap.header.wizard.heatmapheader.AggregationDecoratorHeaderWizard;
import org.gitools.ui.app.heatmap.header.wizard.heatmapheader.AnnotationDecoratorHeaderWizard;
import org.gitools.ui.app.heatmap.header.wizard.textlabels.TextLabelsHeaderWizard;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.wizard.IWizard;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.ui.platform.wizard.WizardDialog;

import java.awt.event.ActionEvent;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;

public class AddHeaderAction extends HeatmapDimensionAction {

    public AddHeaderAction(MatrixDimensionKey dim) {
        super(dim, "<html><i>Add</i> header...</html>");

        setSmallIconFromResource(IconNames.add16);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        HeatmapDimension heatmapDimension = getDimension();

        AddHeaderPage headerPage = new AddHeaderPage();
        PageDialog tdlg = new PageDialog(Application.get(), headerPage);
        tdlg.setTitle("Header type selection");
        tdlg.setVisible(true);
        if (tdlg.isCancelled())
            return;

        HeatmapHeader header = null;
        IWizard wizard = null;
        Class<? extends HeatmapHeader> cls = headerPage.getHeaderClass();
        String headerTitle = headerPage.getHeaderTitle();

        if (cls.equals(HeatmapTextLabelsHeader.class)) {
            HeatmapTextLabelsHeader h = new HeatmapTextLabelsHeader(heatmapDimension);
            wizard = new TextLabelsHeaderWizard(heatmapDimension, h);
            header = h;
        } else if (cls.equals(HeatmapColoredLabelsHeader.class)) {
            HeatmapColoredLabelsHeader h = new HeatmapColoredLabelsHeader(heatmapDimension);
            wizard = new ColoredLabelsHeaderWizard(heatmapDimension, h);
            header = h;
        } else {
            if (cls.equals(HeatmapDecoratorHeader.class)) {
                HeatmapDecoratorHeader h = new HeatmapDecoratorHeader(heatmapDimension);
                header = h;
                if (headerTitle.equals(AddHeaderPage.ANNOTATION_HEATMAP)) {
                    wizard = new AnnotationDecoratorHeaderWizard(h, heatmapDimension);
                } else {
                    wizard = new AggregationDecoratorHeaderWizard(h, getHeatmap(), heatmapDimension, (getDimensionKey() == COLUMNS) ? getHeatmap().getRows() : getHeatmap().getColumns());
                }

            }
        }

        WizardDialog wdlg = new WizardDialog(Application.get(), wizard);
        wdlg.setTitle("Add header");
        wdlg.setVisible(true);
        if (wdlg.isCancelled())
            return;

        heatmapDimension.addHeader(header);

        Application.get().setStatusText("Header added");
    }

}
