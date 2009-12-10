package org.gitools.ui.panels.decorator;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.model.decorator.impl.LinearTwoSidedElementDecorator;
import org.gitools.model.decorator.impl.PValueElementDecorator;
import org.gitools.model.decorator.impl.ZScoreElementDecorator;
import org.gitools.model.figure.HeatmapFigure;

public class ElementDecoratorPanelFactory {

	public static JComponent create(Class<? extends ElementDecorator> decoratorClass, HeatmapFigure model) {
		
		if (PValueElementDecorator.class.equals(decoratorClass))
			return new PValueElementDecoratorPanel(model);
		else if (ZScoreElementDecorator.class.equals(decoratorClass))
			return new ZScoreElementDecoratorPanel(model);
		else if (BinaryElementDecorator.class.equals(decoratorClass))
			return new BinaryElementDecoratorPanel(model);
		else if (LinearTwoSidedElementDecorator.class.equals(decoratorClass))
			return new LinearTwoSidedElementDecoratorPanel(model);
		
		return new JPanel();
	}
}
