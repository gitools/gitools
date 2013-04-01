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
package org.gitools.ui.view.entity;

import org.gitools.ui.platform.view.AbstractView;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

//TODO allow definition of a <Class, Controller> map from outside
public abstract class EntityView extends AbstractView
{

    private Map<Class<?>, EntityController> controllerCache;

    public EntityView()
    {
        controllerCache = new HashMap<Class<?>, EntityController>();

        createComponents();
    }

    private void createComponents()
    {
        setLayout(new BorderLayout());
    }

    public void updateContext(Object context)
    {
        JComponent component = null;

        if (context != null)
        {
            EntityController controller = controllerCache.get(context.getClass());
            if (controller == null)
            {
                controller = createController(context);
                controllerCache.put(context.getClass(), controller);
            }

            component = controller.getComponent(context);
        }

        if (component == null)
        {
            component = new JPanel();
        }

        component.setBorder(null);
        removeAll();
        add(component, BorderLayout.CENTER);
    }

    protected abstract EntityController createController(Object context);
}
