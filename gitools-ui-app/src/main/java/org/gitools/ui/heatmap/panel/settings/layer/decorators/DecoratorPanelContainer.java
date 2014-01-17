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
package org.gitools.ui.heatmap.panel.settings.layer.decorators;

import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import org.gitools.core.model.decorator.Decorator;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class DecoratorPanelContainer extends JPanel {

    private ValueModel decoratorModel;
    private ValueModel currentPanelModel;
    private List<DecoratorPanel> panels;

    public DecoratorPanelContainer() {
        super();

        setLayout(new CardLayout());

        this.currentPanelModel = new ValueHolder();
        this.currentPanelModel.addValueChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateFromPanel();
                showCurrentPanel();
            }
        });
    }

    public void init(List<DecoratorPanel> panels, ValueModel decoratorModel) {
        this.decoratorModel = decoratorModel;
        this.panels = panels;
        this.decoratorModel.addValueChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateFromModel();
                showCurrentPanel();
            }
        });

        removeAll();

        for (DecoratorPanel panel : panels) {

            add(panel.getRootPanel(), panel.getName());

            if (panel.isValid(getCurrentDecorator())) {
                getCurrentPanelModel().setValue(panel);
                panel.setValue(getCurrentDecorator());
            }

            panel.bind();
        }

        updateFromModel();

        showCurrentPanel();
    }

    private Decorator getCurrentDecorator() {
        return (Decorator) decoratorModel.getValue();
    }

    private void setCurrentDecorator(Decorator decorator) {
        decoratorModel.setValue(decorator);
    }

    private void showCurrentPanel() {
        getCardLayout().show(this, getCurrentPanel().getName());
    }

    private DecoratorPanel getCurrentPanel() {
        return (DecoratorPanel) getCurrentPanelModel().getValue();
    }

    public ValueModel getCurrentPanelModel() {
        return this.currentPanelModel;
    }

    private CardLayout getCardLayout() {
        return (CardLayout) DecoratorPanelContainer.this.getLayout();
    }

    private void updateFromModel() {
        Decorator decorator = getCurrentDecorator();

        for (DecoratorPanel panel : panels) {

            if (decorator == null) {
                getCurrentPanelModel().setValue(panel);
                return;
            }

            if (panel.isValid(decorator)) {
                getCurrentPanelModel().setValue(panel);
                panel.setValue(decorator);
            }
        }
    }

    private void updateFromPanel() {
        Class<? extends Decorator> decoratorClass = getCurrentPanel().getDecoratorClass();
        Decorator currentDecorator = getCurrentDecorator();

        if (currentDecorator == null || !decoratorClass.isAssignableFrom(currentDecorator.getClass())) {
            Decorator newDecorator = getCurrentPanel().newDecorator();
            setCurrentDecorator(newDecorator);
            getCurrentPanel().setValue(newDecorator);
        }
    }
}
