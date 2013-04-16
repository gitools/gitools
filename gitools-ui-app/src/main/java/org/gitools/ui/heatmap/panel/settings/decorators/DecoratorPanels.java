package org.gitools.ui.heatmap.panel.settings.decorators;

import org.gitools.model.decorator.impl.CorrelationDecorator;
import org.gitools.model.decorator.impl.LinearDecorator;

import java.util.ArrayList;

public class DecoratorPanels extends ArrayList<DecoratorPanel>
{

    public DecoratorPanels()
    {
        add(new PValueDecoratorPanel());
        add(new ZScoreDecoratorPanel());
        add(new BinaryDecoratorPanel());
        add(new CategoricalDecoratorPanel());
        add(new LinearDecoratorPanel("Linear scale", new LinearDecorator()));
        add(new LinearDecoratorPanel("Correlation scale", new CorrelationDecorator()));
    }

}
