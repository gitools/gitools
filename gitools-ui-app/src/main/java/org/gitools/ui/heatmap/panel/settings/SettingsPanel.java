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
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueHolder;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.HeatmapLayers;
import org.gitools.model.Resource;
import org.gitools.model.decorator.Decorator;
import org.gitools.ui.actions.EditActions;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanel;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanelContainer;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanels;
import org.gitools.ui.settings.decorators.SaveDecoratorDialog;
import org.gitools.ui.utils.landf.MyWebColorChooserField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SettingsPanel {

    // Components
    private JPanel rootPanel;
    private JComboBox decoratorPanelSelector;
    private JPanel decoratorPanels;
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
    private JLabel colorScaleSave;
    private JLabel colorScaleOpen;
    private JComboBox layerselector;

    public SettingsPanel(final Heatmap heatmap) {
        PresentationModel<Heatmap> model = new PresentationModel<Heatmap>(heatmap);

        // Data models
        PresentationModel<HeatmapDimension> rows = new PresentationModel<HeatmapDimension>(model.getModel(Heatmap.PROPERTY_ROWS));
        PresentationModel<HeatmapDimension> columns = new PresentationModel<HeatmapDimension>(model.getModel(Heatmap.PROPERTY_COLUMNS));
        PresentationModel<HeatmapLayers> layers = new PresentationModel<HeatmapLayers>(model.getModel(Heatmap.PROPERTY_LAYERS));
        PresentationModel<HeatmapLayer> topLayer = new PresentationModel<HeatmapLayer>(layers.getModel(HeatmapLayers.PROPERTY_TOP_LAYER));

        // Layer selector
        Bindings.bind(layerselector, new SelectionInList<String>(
                heatmap.getLayers().getLayerNames(),
                new ValueHolder(),
                layers.getModel(HeatmapLayers.PROPERTY_TOP_LAYER_INDEX)
        ));

        // Bind color scale controls
        DecoratorPanels decorators = new DecoratorPanels();
        DecoratorPanelContainer decoratorsPanels = (DecoratorPanelContainer) this.decoratorPanels;
        final AbstractValueModel decoratorModel = new AbstractValueModel() {
            @Override
            public Object getValue() {
                return heatmap.getLayers().getTopLayer().getDecorator();
            }

            @Override
            public void setValue(Object newValue) {
                heatmap.getLayers().getTopLayer().setDecorator((Decorator) newValue);
                fireValueChange(null, newValue);
            }
        };
        layers.addBeanPropertyChangeListener(HeatmapLayers.PROPERTY_TOP_LAYER, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                decoratorModel.fireValueChange(null, heatmap.getLayers().getTopLayer().getDecorator());
            }
        });


        decoratorsPanels.init(decorators, heatmap.getLayers().getLayerNames(), decoratorModel);
        Bindings.bind(decoratorPanelSelector, new SelectionInList<DecoratorPanel>(
                decorators,
                decoratorsPanels.getCurrentPanelModel()
        ));

        colorScaleSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorScaleSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SaveDecoratorDialog.actionSaveDecorator(
                        heatmap.getLayers().getTopLayer().getDecorator()
                );
            }
        });

        colorScaleOpen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorScaleOpen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SaveDecoratorDialog.actionLoadDecorator(decoratorModel);
            }
        });

        // Bind headers controls
        newColumnsHeader.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newColumnsHeader.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EditActions.addColumnHeader.actionPerformed(new ActionEvent(this, 1, ""));
            }
        });

        editColumnsHeader.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editColumnsHeader.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EditActions.editColumnHeader.actionPerformed(new ActionEvent(this, 1, ""));
            }
        });

        newRowsHeader.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newRowsHeader.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EditActions.addRowHeader.actionPerformed(new ActionEvent(this, 1, ""));
            }
        });

        editRowHeaders.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editRowHeaders.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EditActions.editRowHeader.actionPerformed(new ActionEvent(this, 1, ""));
            }
        });

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
        cellSizeRows.setModel(SpinnerAdapterFactory.createNumberAdapter(cellSizeRowsModel, 1, 2, 300, 1));
        AbstractValueModel cellSizeColumnsModel = columns.getModel(HeatmapDimension.PROPERTY_CELL_SIZE);
        cellSizeColumns.setModel(SpinnerAdapterFactory.createNumberAdapter(cellSizeColumnsModel, 1, 2, 300, 1));
        cellSizeKeepRatio.setModel(new KeepRatioModel(cellSizeRowsModel, cellSizeColumnsModel));


    }

    public JPanel getRootPanel() {
        return rootPanel;
    }


    private void createUIComponents() {
        this.gridRowsColor = new MyWebColorChooserField();
        this.gridColumnsColor = new MyWebColorChooserField();
        this.decoratorPanels = new DecoratorPanelContainer();
    }
}
