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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;

public abstract class AbstractElementDecoratorPanel extends JPanel {

	private static final long serialVersionUID = 7349354490870110812L;

	protected Heatmap model;
	
	protected List<IndexedProperty> valueProperties;

	public AbstractElementDecoratorPanel(Heatmap model) {
		this.model = model;
	}
	
	protected IMatrixView getTable() {
		return model.getMatrixView();
	}
	
	protected ElementDecorator getDecorator() {
		return model.getCellDecorator();
	}
	
	protected List<IndexedProperty> loadAllProperties(List<IndexedProperty> properties, IElementAdapter adapter) {
		int numProps = adapter.getPropertyCount();
		
		if (properties == null)
			properties = new ArrayList<IndexedProperty>(numProps);
		
		for (int i = 0; i < numProps; i++) {
			final IElementAttribute property = adapter.getProperty(i);
			properties.add(new IndexedProperty(i, property));
		}
		
		return properties;
	}
}
