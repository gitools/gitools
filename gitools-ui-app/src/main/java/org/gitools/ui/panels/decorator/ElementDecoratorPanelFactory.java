/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.panels.decorator;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.*;
import org.gitools.heatmap.Heatmap;

public class ElementDecoratorPanelFactory {

	public static JComponent create(Class<? extends ElementDecorator> decoratorClass, Heatmap model) {
		
		if (PValueElementDecorator.class.equals(decoratorClass))
			return new PValueElementDecoratorPanel(model);
		else if (ZScoreElementDecorator.class.equals(decoratorClass))
			return new ZScoreElementDecoratorPanel(model);
		else if (BinaryElementDecorator.class.equals(decoratorClass))
			return new BinaryElementDecoratorPanel(model);
		else if (LinearTwoSidedElementDecorator.class.equals(decoratorClass))
			return new LinearTwoSidedElementDecoratorPanel(model);
		else if (CorrelationElementDecorator.class.equals(decoratorClass))
			return new LinearTwoSidedElementDecoratorPanel(model);
        else if ((CategoricalElementDecorator.class.equals(decoratorClass)))
            return new CategoricalElementDecoratorPanel(model);
		
		return new JPanel();
	}
}
