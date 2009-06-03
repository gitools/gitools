package es.imim.bg.ztools.ui.panels.decorator;

import javax.swing.JComponent;
import javax.swing.JPanel;

import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.decorator.impl.BinaryElementDecorator;
import es.imim.bg.ztools.table.decorator.impl.Log2RatioElementDecorator;
import es.imim.bg.ztools.table.decorator.impl.PValueElementDecorator;
import es.imim.bg.ztools.table.decorator.impl.ZScoreElementDecorator;
import es.imim.bg.ztools.ui.model.TableViewModel;

public class ElementDecoratorPanelFactory {

	public static JComponent create(Class<? extends ElementDecorator> decoratorClass, TableViewModel model) {
		
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
