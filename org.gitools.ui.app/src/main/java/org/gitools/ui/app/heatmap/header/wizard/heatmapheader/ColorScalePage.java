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
package org.gitools.ui.app.heatmap.header.wizard.heatmapheader;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueModel;
import org.gitools.heatmap.decorator.Decorator;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.decorators.SaveDecoratorDialog;
import org.gitools.ui.app.heatmap.panel.settings.layer.decorators.DecoratorPanelContainer;
import org.gitools.ui.app.heatmap.panel.settings.layer.decorators.DecoratorPanels;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ColorScalePage extends AbstractWizardPage {
    private JPanel mainPanel;
    private JComboBox decoratorPanelSelector;
    private JLabel colorScaleSave;
    private JLabel colorScaleOpen;
    private JPanel decoratorPanels;


    public ColorScalePage(final HeatmapDecoratorHeader header) {
        super();

        // Bind color scale controls
        final DecoratorPanels decorators = new DecoratorPanels();
        final DecoratorPanelContainer decoratorsPanels = (DecoratorPanelContainer) this.decoratorPanels;

        final ValueModel decoratorModel = new AbstractValueModel() {
            @Override
            public Object getValue() {
                return header.getDecorator();
            }

            @Override
            public void setValue(Object newValue) {
                header.setDecorator((Decorator) newValue);
            }
        };

 /*       decoratorsPanels.init(decorators, decoratorModel);

        Bindings.bind(decoratorPanelSelector, new SelectionInList<>(
                decorators,
                decoratorsPanels.getCurrentPanelModel()
        ));     */

        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                decoratorsPanels.init(decorators, decoratorModel);

                Bindings.bind(decoratorPanelSelector, new SelectionInList<>(
                        decorators,
                        decoratorsPanels.getCurrentPanelModel()
                ));
            }
        };
        listener.propertyChange(null);
        header.addPropertyChangeListener(HeatmapHeader.PROPERTY_DECORATOR, listener);

        colorScaleSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorScaleSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SaveDecoratorDialog.actionSaveDecorator(header.getDecorator());
            }
        });

        colorScaleOpen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorScaleOpen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SaveDecoratorDialog.actionLoadDecorator(decoratorModel);
            }
        });

        if (getTitle().equals("")) {
            setTitle("Color scale");
        }
    }


    @Override
    public JComponent createControls() {
        return mainPanel;
    }

    private void createUIComponents() {
        this.decoratorPanels = new DecoratorPanelContainer();
    }
}
