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

import org.gitools.ui.view.details.*;
import org.gitools.ui.view.entity.EntityController;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.gitools.model.figure.HeatmapFigure;
import org.gitools.ui.view.AbstractView;

public class EntityView extends AbstractView {

	private JScrollPane scrollPane;

	private Map<Class<?>, EntityController> ctrlMap;

	public EntityView() {
		ctrlMap = new HashMap<Class<?>, EntityController>();

		createComponents();
	}

	private void createComponents() {
		scrollPane = new JScrollPane();

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	public void update(Object context) {
		EntityController controller = ctrlMap.get(context.getClass());
		if (controller == null) {
			if (context instanceof HeatmapFigure)
				controller = new HeatmapDetailsController();
			ctrlMap.put(context.getClass(), controller);
		}

		JComponent component = controller.getDetailsComponent(context);
		component.setBorder(null);
		scrollPane.setViewportView(component);
	}
}
