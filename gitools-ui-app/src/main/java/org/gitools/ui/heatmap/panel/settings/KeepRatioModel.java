package org.gitools.ui.heatmap.panel.settings;

import com.jgoodies.binding.value.AbstractValueModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class KeepRatioModel extends JToggleButton.ToggleButtonModel implements ChangeListener
{

    private int firstBase;
    private int secondBase;

    private AbstractValueModel first;
    private AbstractValueModel second;

    public KeepRatioModel(AbstractValueModel first, AbstractValueModel second)
    {
        super();
        setSelected(true);

        this.first = first;
        this.second = second;

        updateBase();

        first.addValueChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                updateSecond();
            }
        });

        second.addValueChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                updateFirst();
            }
        });

        addChangeListener(this);
    }

    private void updateFirst()
    {
        if (isSelected())
        {
            int newValue = firstBase + (second.intValue() - secondBase);
            if (newValue > 0)
            {
                first.setValue(newValue);
            }

        }
    }

    private void updateSecond()
    {
        if (isSelected())
        {
            int newValue = secondBase + (first.intValue() - firstBase);
            if (newValue > 0)
            {
                second.setValue(newValue);
            }
        }
    }

    private void updateBase()
    {
        firstBase = first.intValue();
        secondBase = second.intValue();
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        if (isSelected()) {
            updateBase();
        }
    }
}
