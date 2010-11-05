/*
 *  Copyright 2009 cperez.
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

package org.gitools.ui.view.entity;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.gitools.ui.platform.view.AbstractView;

//TODO allow definition of a <Class, Controller> map from outside
public abstract class EntityView extends AbstractView {

	private Map<Class<?>, EntityController> controllerCache;

	public EntityView() {
		controllerCache = new HashMap<Class<?>, EntityController>();

		createComponents();
	}

	private void createComponents() {
		setLayout(new BorderLayout());
	}

	public void updateContext(Object context) {
		JComponent component = null;

		if (context != null) {
			EntityController controller = controllerCache.get(context.getClass());
			if (controller == null) {
				controller = createController(context);
				controllerCache.put(context.getClass(), controller);
			}
			
			component = controller.getComponent(context);
		}

		if (component == null)
			component = new JPanel();

		component.setBorder(null);
		removeAll();
		add(component, BorderLayout.CENTER);
	}

	protected abstract EntityController createController(Object context);
}
