/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.fileimport.wizard.text;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.impl.CategoricalDecorator;
import org.gitools.ui.app.commands.AbstractCommand;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.app.heatmap.panel.settings.layer.ColorScaleSection;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.components.editor.AbstractEditor;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.platform.settings.SettingsDialog;
import org.gitools.ui.platform.settings.SettingsPanel;
import org.gitools.utils.color.ColorGenerator;
import org.gitools.utils.colorscale.ColorScalePoint;

import java.util.*;

/**
 * Created by mschroeder on 5/28/14.
 */
public class CommandEditAllColorScales extends AbstractCommand {
    private Map<IMatrixLayer, Map<String, Integer>> factorMap;

    public CommandEditAllColorScales(Map<IMatrixLayer, Map<String, Integer>> factorMap) {
        super();
        this.factorMap = factorMap;
    }


    @Override
    public void run() {
        super.run();
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {
        AbstractEditor editor = Application.get().getEditorsPanel().getSelectedEditor();
        if (editor instanceof HeatmapEditor) {
            Heatmap heatmap = ((HeatmapEditor) editor).getModel();

            List<ISettingsSection> sections = new ArrayList<>();


            for (HeatmapLayer layer : heatmap.getLayers()) {

                setCategorical(layer);

                ColorScaleSection colorSection = new ColorScaleSection(layer);
                colorSection.setName(layer.getName());
                sections.add(colorSection);

            }


            SettingsPanel settingsPanel = new SettingsPanel(
                    "Color scale options for " + editor.getName(),
                    "",
                    IconNames.logoNoText,
                    sections
            );

            SettingsDialog dialog = new SettingsDialog(Application.get(), settingsPanel, sections.get(0).getName()) {

                @Override
                protected void apply() {

                }
            };

            dialog.setVisible(true);

        }
    }

    private void setCategorical(HeatmapLayer layer) {
        if (factorMap != null) {
            Map<String, Integer> layerFactorMap = null;
            for (IMatrixLayer factorLayer : factorMap.keySet()) {
                if (factorLayer.getId().equals(layer.getId())) {
                    layerFactorMap = factorMap.get(factorLayer);
                }
            }

            if (layerFactorMap != null) {
                CategoricalDecorator decorator = new CategoricalDecorator();
                ColorGenerator cg = new ColorGenerator();
                Set<ColorScalePoint> factorPoints = new HashSet<>();
                for (String factor : layerFactorMap.keySet()) {
                    factorPoints.add(new ColorScalePoint(layerFactorMap.get(factor), cg.next(factor), factor));
                }
                decorator.setCategories(factorPoints.toArray(new ColorScalePoint[factorPoints.size()]));
                layer.setDecorator(decorator);
            }
        }
    }
}
