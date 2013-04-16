package org.gitools.ui.heatmap.panel.settings;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.HeatmapLayers;
import org.gitools.model.Resource;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanel;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanelContainer;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanels;
import org.gitools.ui.utils.landf.MyWebColorChooserField;

import javax.swing.*;

public class SettingsPanel
{

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

    public SettingsPanel(Heatmap heatmap)
    {
        PresentationModel<Heatmap> model = new PresentationModel<Heatmap>(heatmap);

        // Data models
        PresentationModel<HeatmapDimension> rows = new PresentationModel<HeatmapDimension>(model.getModel(Heatmap.PROPERTY_ROWS));
        PresentationModel<HeatmapDimension> columns = new PresentationModel<HeatmapDimension>(model.getModel(Heatmap.PROPERTY_COLUMNS));
        PresentationModel<HeatmapLayers> layers = new PresentationModel<HeatmapLayers>(model.getModel(Heatmap.PROPERTY_LAYERS));

        // Layer selector
        Bindings.bind(layerselector, new SelectionInList<HeatmapLayer>(
                heatmap.getLayers().toList(),
                layers.getModel(HeatmapLayers.PROPERTY_TOP_LAYER)
        ));

        // Bind color scale controls
        DecoratorPanels decorators = new DecoratorPanels();
        DecoratorPanelContainer decoratorsPanels = (DecoratorPanelContainer) this.decoratorPanels;
        decoratorsPanels.init(decorators, heatmap);
        Bindings.bind(decoratorPanelSelector, new SelectionInList<DecoratorPanel>(
                decorators,
                decoratorsPanels.getCurrentPanelModel()
        ));

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
        cellSizeRows.setModel(SpinnerAdapterFactory.createNumberAdapter(cellSizeRowsModel, 1, 0, 300, 1));

        AbstractValueModel cellSizeColumnsModel = columns.getModel(HeatmapDimension.PROPERTY_CELL_SIZE);
        cellSizeColumns.setModel( SpinnerAdapterFactory.createNumberAdapter(cellSizeColumnsModel , 1, 0, 300, 1) );

        cellSizeKeepRatio.setModel(new KeepRatioModel(cellSizeRowsModel, cellSizeColumnsModel));


    }

    public JPanel getRootPanel()
    {
        return rootPanel;
    }


    private void createUIComponents()
    {
        this.gridRowsColor = new MyWebColorChooserField();
        this.gridColumnsColor = new MyWebColorChooserField();
        this.decoratorPanels = new DecoratorPanelContainer();
    }
}
