package org.gitools.ui.heatmap.panel.settings.headers;

import com.alee.extended.colorchooser.WebColorChooserField;

import javax.swing.*;
import java.awt.*;

public class ColorCellEditor extends DefaultCellEditor
{

    public ColorCellEditor()
    {
        super(new WebColorChooserField());

        delegate = new EditorDelegate ()
        {
            public void setValue ( Object value )
            {
                if (value == null)
                {
                    value = Color.WHITE;
                }
                WebColorChooserField field = (WebColorChooserField) editorComponent;
                field.setColor( (Color) value );
            }

            public Object getCellEditorValue ()
            {
                WebColorChooserField field = (WebColorChooserField) editorComponent;
                return field.getColor();
            }
        };

        WebColorChooserField field = (WebColorChooserField) editorComponent;
        field.addActionListener(delegate);
    }
}
