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

package org.gitools.ui.modules.editor;

import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import org.gitools.model.ModuleMap;
import org.gitools.ui.platform.editor.AbstractEditor;

public class ModuleMapEditor extends AbstractEditor {

	protected ModuleMap mmap;

	private ModuleMapTablePanel modPanel;
	private ModuleMapTablePanel featPanel;

	public ModuleMapEditor(ModuleMap mmap) {
		this.mmap = mmap;

		createComponents();
	}

	private void createComponents() {
		modPanel = new ModuleMapTablePanel();
		featPanel = new ModuleMapTablePanel();

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(0.5);
		splitPane.add(modPanel);
		splitPane.add(featPanel);

		setLayout(new BorderLayout());
		add(splitPane);
	}

	@Override
	public Object getModel() {
		return mmap;
	}
}
