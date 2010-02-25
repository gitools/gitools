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
