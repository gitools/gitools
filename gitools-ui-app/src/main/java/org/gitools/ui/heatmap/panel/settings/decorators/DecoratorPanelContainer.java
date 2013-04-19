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
package org.gitools.ui.heatmap.panel.settings.decorators;

import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.HeatmapLayers;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.model.decorator.Decorator;
import org.gitools.utils.EventUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class DecoratorPanelContainer extends JPanel implements PropertyChangeListener {

    private IMatrixLayers layers;
    private ValueModel decoratorModel;
    private ValueModel currentPanelModel;
    private List<DecoratorPanel> panels;

    public DecoratorPanelContainer() {
        super();

        setLayout(new CardLayout());

        this.currentPanelModel = new ValueHolder();
        this.currentPanelModel.addValueChangeListener(this);
    }

    public void init(List<DecoratorPanel> panels, List<String> layers, ValueModel decoratorModel) {
        this.decoratorModel = decoratorModel;
        this.panels = panels;
        this.decoratorModel.addValueChangeListener(this);

        removeAll();
        for (DecoratorPanel panel : panels) {

            add(panel.getRootPanel(), panel.getName());

            if (panel.isValid(getCurrentDecorator())) {
                getCurrentPanelModel().setValue(panel);
                panel.setValue(getCurrentDecorator());
            }

            panel.setLayers(layers);
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

        if (currentDecorator==null || !decoratorClass.isAssignableFrom(currentDecorator.getClass())) {
            setCurrentDecorator(getCurrentPanel().newDecorator());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (EventUtils.isAny(evt, HeatmapLayers.class, HeatmapLayers.PROPERTY_TOP_LAYER) ||
                EventUtils.isAny(evt, HeatmapLayer.class, HeatmapLayer.PROPERTY_DECORATOR)) {
            updateFromModel();
        } else {
            updateFromPanel();
        }
        showCurrentPanel();
    }
}
