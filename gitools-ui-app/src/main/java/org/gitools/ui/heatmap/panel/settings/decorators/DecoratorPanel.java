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

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.AbstractValueModel;
import org.gitools.core.model.decorator.Decorator;

import javax.swing.*;
import java.util.List;

public abstract class DecoratorPanel {
    private String name;
    private Class<? extends Decorator> decoratorClass;

    private PresentationModel<Decorator> panelModel;
    private List<String> layers;

    public DecoratorPanel(String name, Decorator defaultDecorator) {
        this.name = name;
        this.decoratorClass = defaultDecorator.getClass();
        this.panelModel = new PresentationModel<Decorator>(defaultDecorator);
    }

    public boolean isValid(Object decorator) {
        if (decorator == null) {
            return false;
        }

        if (decoratorClass.equals(decorator.getClass())) {
            return true;
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public void setValue(Object decorator) {
        if (isValid(decorator)) {
            getPanelModel().setBean((Decorator) decorator);
        }
    }

    protected PresentationModel<Decorator> getPanelModel() {
        return panelModel;
    }

    protected AbstractValueModel model(String propertyName) {
        return getPanelModel().getModel(propertyName);
    }

    public List<String> getLayers() {
        return layers;
    }

    public void setLayers(List<String> layers) {
        this.layers = layers;
    }

    public abstract void bind();

    public abstract JPanel getRootPanel();

    public String toString() {
        return getName();
    }

    public Class<? extends Decorator> getDecoratorClass() {
        return decoratorClass;
    }

    public Decorator newDecorator() {
        try {
            return decoratorClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
