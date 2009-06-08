package org.gitools.ui.panels.decorator;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.table.decorator.ElementDecorator;
import org.gitools.model.table.decorator.impl.BinaryElementDecorator;
import org.gitools.model.table.decorator.impl.Log2RatioElementDecorator;
import org.gitools.model.table.decorator.impl.PValueElementDecorator;
import org.gitools.model.table.decorator.impl.ZScoreElementDecorator;

public class ElementDecoratorPanelFactory {

	public static JComponent create(Class<? extends ElementDecorator> decoratorClass, MatrixFigure model) {
		
		if (PValueElementDecorator.class.equals(decoratorClass))
			return new PValueDecoratorPanel(model);
		else if (ZScoreElementDecorator.class.equals(decoratorClass))
			return new ZScoreDecoratorPanel(model);
		else if (BinaryElementDecorator.class.equals(decoratorClass))
			return new BinaryDecoratorPanel(model);
		else if (Log2RatioElementDecorator.class.equals(decoratorClass))
			return new Log2RatioDecoratorPanel(model);
		
		return new JPanel();
	}
}
