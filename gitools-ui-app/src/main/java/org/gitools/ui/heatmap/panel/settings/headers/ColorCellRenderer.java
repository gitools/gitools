package org.gitools.ui.heatmap.panel.settings.headers;

import org.gitools.utils.color.utils.ColorUtils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ColorCellRenderer extends JLabel implements TableCellRenderer
{
    public ColorCellRenderer()
    {
        setOpaque(true);
        setFont(getFont().deriveFont(Font.BOLD));
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (value == null)
        {
            setText("None");
        } else {
            Color color = (Color) value;
            setText(ColorUtils.colorToHexHtml(color));
            setForeground(color);
        }

        return this;
    }
}
