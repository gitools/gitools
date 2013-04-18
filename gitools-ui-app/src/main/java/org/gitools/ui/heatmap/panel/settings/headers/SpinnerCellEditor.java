package org.gitools.ui.heatmap.panel.settings.headers;

import javax.swing.*;
import java.awt.*;


public class SpinnerCellEditor extends DefaultCellEditor
{
    private JSpinner spinner;

    public SpinnerCellEditor(SpinnerNumberModel numberModel)
    {
        super( new JTextField() );
        spinner = new JSpinner(numberModel);
        spinner.setBorder( null );
    }

    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column)
    {
        spinner.setValue( value );
        return spinner;
    }

    public Object getCellEditorValue()
    {
        return spinner.getValue();
    }
}
