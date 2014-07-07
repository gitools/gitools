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
package org.gitools.ui.app.heatmap.panel.settings.headers;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;
import org.gitools.heatmap.decorator.Decorator;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.ui.app.decorators.SaveDecoratorDialog;
import org.gitools.ui.app.heatmap.panel.settings.layer.decorators.DecoratorPanelContainer;
import org.gitools.ui.app.heatmap.panel.settings.layer.decorators.DecoratorPanels;
import org.gitools.ui.platform.settings.ISettingsSection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorScaleSection implements ISettingsSection {

    // Components
    private JPanel rootPanel;
    private JComboBox decoratorPanelSelector;
    private JPanel decoratorPanels;
    private JLabel colorScaleSave;
    private JLabel colorScaleOpen;


    public ColorScaleSection(final HeatmapDecoratorHeader heatmapHeader) {

        PresentationModel<HeatmapDecoratorHeader> header = new PresentationModel<>(heatmapHeader);

        // Color scale
        DecoratorPanels decorators = new DecoratorPanels();
        DecoratorPanelContainer decoratorsPanels = (DecoratorPanelContainer) this.decoratorPanels;
        final AbstractValueModel decoratorValueModel = new AbstractValueModel() {
            @Override
            public Object getValue() {
                return heatmapHeader.getDecorator();
            }

            @Override
            public void setValue(Object newValue) {
                heatmapHeader.setDecorator((Decorator) newValue);
                fireValueChange(null, newValue);
            }
        };

        decoratorsPanels.init(decorators, decoratorValueModel);
        Bindings.bind(decoratorPanelSelector, new SelectionInList<>(
                decorators,
                decoratorsPanels.getCurrentPanelModel()
        ));

        colorScaleSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorScaleSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SaveDecoratorDialog.actionSaveDecorator(
                        heatmapHeader.getDecorator()
                );
            }
        });

        colorScaleOpen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorScaleOpen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SaveDecoratorDialog.actionLoadDecorator(decoratorValueModel);
            }
        });

    }

    @Override
    public String getName() {
        return "Color scale";
    }

    public JPanel getPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        this.decoratorPanels = new DecoratorPanelContainer();
    }


}
