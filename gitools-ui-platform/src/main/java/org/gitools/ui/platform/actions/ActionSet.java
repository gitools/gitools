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

package org.gitools.ui.platform.actions;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;

import org.gitools.ui.platform.editor.IEditor;

public class ActionSet extends BaseAction {

	private static final long serialVersionUID = -1441656907811177103L;

	protected List<BaseAction> actions;
	
	public ActionSet(BaseAction[] actions) {
		this("", null, actions);
	}
	
	public ActionSet(String name, BaseAction[] actions) {
		this(name, null, actions);
	}
	
	public ActionSet(String name, ImageIcon icon, BaseAction[] actions) {
		super(name, icon);
		this.actions = Arrays.asList(actions);
		setEnabled(true);
	}

	public ActionSet(List<BaseAction> actions) {
		this(actions.toArray(new BaseAction[actions.size()]));
	}
	
	public List<BaseAction> getActions() {
		return actions;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (actions != null)
			for (BaseAction action : actions)
				action.actionPerformed(e);
	}

	@Override
	public void setTreeEnabled(boolean enabled) {
		setEnabled(enabled);
		if (actions != null)
			for (BaseAction action : actions)
				action.setTreeEnabled(enabled);
	}
	
	@Override
	public boolean updateEnabledByEditor(IEditor editor) {
		boolean someEnabled = false;
		
		for (BaseAction action : actions)
			someEnabled |= action.updateEnabledByEditor(editor);
		
		setEnabled(someEnabled);
		return someEnabled;
	}
}
