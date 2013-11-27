package org.gitools.ui.heatmap.panel.settings;

import org.gitools.ui.utils.landf.MyWebColorChooserField;

import javax.swing.*;

public class GlobalSettingsPanel {
    private JTextField gridRowsColor;
    private JTextField gridColumnsColor;
    private JSpinner gridRowsSize;
    private JSpinner gridColumnsSize;
    private JSpinner cellSizeRows;
    private JSpinner cellSizeColumns;
    private JCheckBox cellSizeKeepRatio;
    private JTextArea documentTitle;
    private JTextArea documentDescription;

    private void createUIComponents() {
        this.gridRowsColor = new MyWebColorChooserField();
        this.gridColumnsColor = new MyWebColorChooserField();
    }
}

