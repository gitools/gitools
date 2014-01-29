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
package org.gitools.ui.app.heatmap.panel.settings.layer.decorators;

import org.gitools.core.model.decorator.impl.CorrelationDecorator;
import org.gitools.core.model.decorator.impl.LinearDecorator;

import java.util.ArrayList;

public class DecoratorPanels extends ArrayList<DecoratorPanel> {

    public DecoratorPanels() {
        add(new PValueDecoratorPanel());
        add(new ZScoreDecoratorPanel());
        add(new BinaryDecoratorPanel());
        add(new CategoricalDecoratorPanel());
        add(new LinearDecoratorPanel("Linear scale", new LinearDecorator()));
        add(new LinearDecoratorPanel("Correlation scale", new CorrelationDecorator()));
    }

}
