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
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ElementDecoratorPanelFactory
{

    @NotNull
    public static JComponent create(Class<? extends ElementDecorator> decoratorClass, Heatmap model)
    {

        if (PValueElementDecorator.class.equals(decoratorClass))
        {
            return new PValueElementDecoratorPanel(model);
        }
        else if (ZScoreElementDecorator.class.equals(decoratorClass))
        {
            return new ZScoreElementDecoratorPanel(model);
        }
        else if (BinaryElementDecorator.class.equals(decoratorClass))
        {
            return new BinaryElementDecoratorPanel(model);
        }
        else if (LinearTwoSidedElementDecorator.class.equals(decoratorClass))
        {
            return new LinearTwoSidedElementDecoratorPanel(model);
        }
        else if (CorrelationElementDecorator.class.equals(decoratorClass))
        {
            return new LinearTwoSidedElementDecoratorPanel(model);
        }
        else if ((CategoricalElementDecorator.class.equals(decoratorClass)))
        {
            return new CategoricalElementDecoratorPanel(model);
        }

        return new JPanel();
    }
}
