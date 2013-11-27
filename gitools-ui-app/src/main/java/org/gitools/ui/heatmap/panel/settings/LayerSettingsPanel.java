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
package org.gitools.ui.heatmap.panel.settings;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;
import org.gitools.core.heatmap.HeatmapLayer;
import org.gitools.core.model.decorator.Decorator;
import org.gitools.ui.IconNames;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanelContainer;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanels;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.settings.AbstractSettingsPanel;
import org.gitools.ui.settings.decorators.SaveDecoratorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LayerSettingsPanel extends AbstractSettingsPanel {

    // Components
    private JPanel rootPanel;
    private JComboBox decoratorPanelSelector;
    private JPanel decoratorPanels;
    private JLabel colorScaleSave;
    private JLabel colorScaleOpen;
    private JTextArea layerDescription;
    private JTextField layerDescriptionLink;
    private JTextField layerValueLink;

    /*
    private JTextField gridRowsColor;
    private JTextField gridColumnsColor;
    private JCheckBox cellSizeKeepRatio;
    private JSpinner cellSizeRows;
    private JSpinner cellSizeColumns;
    private JTextArea documentTitle;
    private JTextArea documentDescription;
    private JSpinner gridRowsSize;
    private JSpinner gridColumnsSize;
    private JLabel editRowHeaders;
    private JLabel editColumnsHeader;
    private JLabel newRowsHeader;
    private JLabel newColumnsHeader;
    */


    public LayerSettingsPanel(final HeatmapLayer heatmapLayer, Iterable<String> layers) {
        super("Layer settings", "this is the message of this layer settings panel");

        setLogo(IconUtils.getImageIconResource(IconNames.logoNoText));

        PresentationModel<HeatmapLayer> layer = new PresentationModel<>(heatmapLayer);

        // Color scale
        DecoratorPanels decorators = new DecoratorPanels();
        DecoratorPanelContainer decoratorsPanels = (DecoratorPanelContainer) this.decoratorPanels;
        final AbstractValueModel decoratorValueModel = new AbstractValueModel() {
            @Override
            public Object getValue() {
                return heatmapLayer.getDecorator();
            }

            @Override
            public void setValue(Object newValue) {
                heatmapLayer.setDecorator((Decorator) newValue);
                fireValueChange(null, newValue);
            }
        };

        decoratorsPanels.init(decorators, layers, decoratorValueModel);
        Bindings.bind(decoratorPanelSelector, new SelectionInList<>(
                decorators,
                decoratorsPanels.getCurrentPanelModel()
        ));

        colorScaleSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorScaleSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SaveDecoratorDialog.actionSaveDecorator(
                        heatmapLayer.getDecorator()
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

        // Bind value controls
        Bindings.bind(layerDescription, layer.getModel(HeatmapLayer.PROPERTY_DESCRIPTION));
        Bindings.bind(layerDescriptionLink, layer.getModel(HeatmapLayer.PROPERTY_DESCRIPTION_URL));
        Bindings.bind(layerValueLink, layer.getModel(HeatmapLayer.PROPERTY_VALUE_URL));

        /*
        // Bind grid controls
        Bindings.bind(gridRowsColor, "color", rows.getModel(HeatmapDimension.PROPERTY_GRID_COLOR));
        Bindings.bind(gridColumnsColor, "color", columns.getModel(HeatmapDimension.PROPERTY_GRID_COLOR));
        gridRowsSize.setModel(SpinnerAdapterFactory.createNumberAdapter(rows.getModel(HeatmapDimension.PROPERTY_GRID_SIZE), 1, 0, 10, 1));
        gridColumnsSize.setModel(SpinnerAdapterFactory.createNumberAdapter(columns.getModel(HeatmapDimension.PROPERTY_GRID_SIZE), 1, 0, 10, 1));

        // Bind document controls
        Bindings.bind(documentTitle, model.getModel(Resource.PROPERTY_TITLE));
        Bindings.bind(documentDescription, model.getModel(Resource.PROPERTY_DESCRIPTION));

        // Bind cell size controls
        AbstractValueModel cellSizeRowsModel = rows.getModel(HeatmapDimension.PROPERTY_CELL_SIZE);
        cellSizeRows.setModel(SpinnerAdapterFactory.createNumberAdapter(cellSizeRowsModel, 1, 1, 300, 1));
        AbstractValueModel cellSizeColumnsModel = columns.getModel(HeatmapDimension.PROPERTY_CELL_SIZE);
        cellSizeColumns.setModel(SpinnerAdapterFactory.createNumberAdapter(cellSizeColumnsModel, 1, 1, 300, 1));
        cellSizeKeepRatio.setModel(new KeepRatioModel(cellSizeRowsModel, cellSizeColumnsModel));
        */


    }

    @Override
    public JComponent createComponents() {
        return getRootPanel();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        //this.gridRowsColor = new MyWebColorChooserField();
        //this.gridColumnsColor = new MyWebColorChooserField();
        this.decoratorPanels = new DecoratorPanelContainer();
    }
}
