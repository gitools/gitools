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
import org.gitools.analysis._DEPRECATED.heatmap.HeatmapDimension;
import org.gitools.analysis._DEPRECATED.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.analysis._DEPRECATED.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.app.heatmap.drawer.HeatmapPosition;
import org.gitools.ui.app.heatmap.panel.settings.headers.ColorScaleSection;
import org.gitools.ui.app.heatmap.panel.settings.headers.ColorsSection;
import org.gitools.ui.app.heatmap.panel.settings.headers.DetailsSection;
import org.gitools.ui.app.heatmap.popupmenus.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.platform.settings.SettingsDialog;
import org.gitools.ui.platform.settings.SettingsPanel;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class EditHeaderAction extends HeatmapDimensionAction implements IHeatmapHeaderAction{

    private HeatmapHeader header;

    public EditHeaderAction(MatrixDimensionKey dimensionKey, String name) {
        super(dimensionKey, name);
    }

    public EditHeaderAction(HeatmapHeader header) {
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
        execute(getDimension(), header);
    }

    public static void execute(HeatmapDimension dimension, HeatmapHeader header) {


        /*
        Class<? extends HeatmapHeader> cls = header.getClass();
        IWizard wizard = null;

        if (HeatmapTextLabelsHeader.class.equals(cls))
            wizard = new TextLabelsHeaderWizard(dimension, (HeatmapTextLabelsHeader) header);
        else if (HeatmapColoredLabelsHeader.class.equals(cls)) {
            ColoredLabelsHeaderWizard wiz = new ColoredLabelsHeaderWizard(dimension, (HeatmapColoredLabelsHeader) header);
            wiz.setEditionMode(true);
            wizard = wiz;
        } else if (HeatmapDecoratorHeader.class.equals(cls)) {
            wizard = new DecoratorHeaderWizard((HeatmapDecoratorHeader) header);
        }

        if (wizard == null)
            return;

        WizardDialog wdlg = new WizardDialog(Application.get(), wizard);
        wdlg.setTitle("Edit header");
        wdlg.setVisible(true);*/

        List<ISettingsSection> sections = new ArrayList<>();

        sections.add( new DetailsSection(header) );

        if (header instanceof HeatmapDecoratorHeader) {
            sections.add(new ColorScaleSection((HeatmapDecoratorHeader) header));
        } else {
            sections.add(new ColorsSection(header));
        }

        SettingsPanel settingsPanel = new SettingsPanel(
                "Header '" + header.getTitle() + "' settings",
                header.getDescription(),
                IconNames.logoNoText,
                sections
        );

        SettingsDialog dialog = new SettingsDialog(Application.get(), settingsPanel, sections.get(0).getName());
        dialog.setVisible(true);
    }

    @Override
    public void onConfigure(HeatmapHeader object, HeatmapPosition position) {
        setHeader(object);
    }



}
