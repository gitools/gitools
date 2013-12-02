package org.gitools.ui.heatmap.panel.settings;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.value.AbstractValueModel;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.model.Resource;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.utils.landf.MyWebColorChooserField;

import javax.swing.*;

public class GlobalSettingsPanel implements ISettingsSection {
    private JTextField gridRowsColor;
    private JTextField gridColumnsColor;
    private JSpinner gridRowsSize;
    private JSpinner gridColumnsSize;
    private JSpinner cellSizeRows;
    private JSpinner cellSizeColumns;
    private JCheckBox cellSizeKeepRatio;
    private JTextArea documentTitle;
    private JTextArea documentDescription;
    private JPanel root;

    public GlobalSettingsPanel(Heatmap heatmap) {

        PresentationModel<Heatmap> model = new PresentationModel<>(heatmap);
        PresentationModel<HeatmapDimension> rows = new PresentationModel<>(model.getModel(Heatmap.PROPERTY_ROWS));
        PresentationModel<HeatmapDimension> columns = new PresentationModel<>(model.getModel(Heatmap.PROPERTY_COLUMNS));

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


    }

    private void createUIComponents() {
        this.gridRowsColor = new MyWebColorChooserField();
        this.gridColumnsColor = new MyWebColorChooserField();
    }

    @Override
    public String getName() {
        return "Cell details";
    }

    @Override
    public JPanel getPanel() {
        return root;
    }
}

