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
package org.gitools.ui.panels.decorator;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.decorator.ElementDecorator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractElementDecoratorPanel extends JPanel
{

    private static final long serialVersionUID = 7349354490870110812L;

    protected Heatmap model;

    protected List<IndexedProperty> valueProperties;

    public AbstractElementDecoratorPanel(Heatmap model)
    {
        this.model = model;
    }

    @Nullable
    protected IMatrixView getTable()
    {
        return model.getMatrixView();
    }

    protected ElementDecorator getDecorator()
    {
        return model.getActiveCellDecorator();
    }

    @Nullable
    protected List<IndexedProperty> loadAllProperties(@Nullable List<IndexedProperty> properties, @NotNull IElementAdapter adapter)
    {
        int numProps = adapter.getPropertyCount();

        if (properties == null)
        {
            properties = new ArrayList<IndexedProperty>(numProps);
        }

        for (int i = 0; i < numProps; i++)
        {
            final IElementAttribute property = adapter.getProperty(i);
            properties.add(new IndexedProperty(i, property));
        }

        return properties;
    }
}
