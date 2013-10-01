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

import com.jgoodies.binding.value.AbstractValueModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class KeepRatioModel extends JToggleButton.ToggleButtonModel implements ChangeListener {

    private int firstBase;
    private int secondBase;

    private AbstractValueModel first;
    private AbstractValueModel second;

    public KeepRatioModel(AbstractValueModel first, AbstractValueModel second) {
        super();
        setSelected(false);

        this.first = first;
        this.second = second;

        updateBase();

        first.addValueChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateSecond();
            }
        });

        second.addValueChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateFirst();
            }
        });

        addChangeListener(this);
    }

    private void updateFirst() {
        if (isSelected()) {
            int newValue = firstBase + (second.intValue() - secondBase);
            if (newValue > 0) {
                first.setValue(newValue);
            }

        }
    }

    private void updateSecond() {
        if (isSelected()) {
            int newValue = secondBase + (first.intValue() - firstBase);
            if (newValue > 0) {
                second.setValue(newValue);
            }
        }
    }

    private void updateBase() {
        firstBase = first.intValue();
        secondBase = second.intValue();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (isSelected()) {
            updateBase();
        }
    }
}
