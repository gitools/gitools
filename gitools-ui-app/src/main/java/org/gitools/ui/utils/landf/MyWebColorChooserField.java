package org.gitools.ui.utils.landf;

import com.alee.extended.colorchooser.ColorChooserFieldType;
import com.alee.extended.colorchooser.WebColorChooserField;

import java.awt.*;

public class MyWebColorChooserField extends WebColorChooserField
{
    public MyWebColorChooserField()
    {
        setFieldType(ColorChooserFieldType.hex);
    }

    @Override
    public void setColor(Color color)
    {
        Color oldValue = super.getColor();
        super.setColor(color);
        firePropertyChange("color", oldValue, color);
    }
}
