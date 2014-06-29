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
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.analysis.clustering.hierarchical.HierarchicalClusterHeatmapHeader;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.app.heatmap.header.wizard.coloredlabels.ColoredLabelsGroupsPage;
import org.gitools.ui.app.heatmap.panel.settings.headers.ColorScaleSection;
import org.gitools.ui.app.heatmap.panel.settings.headers.DetailsSection;
import org.gitools.ui.app.heatmap.panel.settings.headers.FormatSection;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.platform.settings.SettingsDialog;
import org.gitools.ui.platform.settings.SettingsPanel;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class EditHeaderAction extends HeatmapDimensionAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;

    public EditHeaderAction(MatrixDimensionKey dimensionKey, String name) {
        super(dimensionKey, name);
        setSmallIconFromResource(IconNames.edit16);
    }

    public EditHeaderAction(HeatmapHeader header) {
        super(header.getHeatmapDimension().getId(), header.getTitle());
        setSmallIconFromResource(IconNames.edit16);
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
        execute(header);
    }

    public static void execute(final HeatmapHeader header) {

        List<ISettingsSection> sections = new ArrayList<>();

        DetailsSection headerDetails = new DetailsSection(header);
        sections.add(headerDetails);

        if (header instanceof HeatmapDecoratorHeader) {
            sections.add(new ColorScaleSection((HeatmapDecoratorHeader) header));
            sections.add(new FormatSection(false, false, header));
        } else if (header instanceof HeatmapColoredLabelsHeader) {
            sections.add(new ColoredLabelsGroupsPage(((HeatmapColoredLabelsHeader)header).getClusters()));
            sections.add(new FormatSection(false, true, header));
        } else if (header instanceof HierarchicalClusterHeatmapHeader) {
            for (HeatmapColoredLabelsHeader level : ((HierarchicalClusterHeatmapHeader) header).getLevels()) {
                sections.add(new ColoredLabelsGroupsPage(level.getClusters(), level.getTitle()));
            }
        } else {
            sections.add(new FormatSection(true, false, header));
        }

        SettingsPanel settingsPanel = new SettingsPanel(
                "Header '" + header.getTitle() + "' settings",
                header.getDescription(),
                IconNames.logoNoText,
                sections
        );

        SettingsDialog dialog = new SettingsDialog(Application.get(), settingsPanel, sections.get(0).getName()) {

            @Override
            protected void apply() {
                header.getHeatmapDimension().updateHeaders();
            }
        };

        dialog.open();
    }

    @Override
    public void onConfigure(HeatmapHeader object, HeatmapPosition position) {
        setHeader(object);
    }


}
