package org.gitools.ui.platform.actions;

import org.gitools.ui.platform.editor.IEditor;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public abstract class DynamicActionSet extends ActionSet {

    private IEditor editor;

    public DynamicActionSet(String name, int mnemonic, String icon) {
        super(name, mnemonic, icon, null);
    }

    @Override
    public boolean updateEnabledByEditor(IEditor editor) {

        this.editor = editor;

        return isEnabledByEditor(editor);
    }

    protected <T extends BaseAction> T updateEnable(T action) {

        if (editor!=null) {
            action.updateEnabledByEditor(editor);
        }

        return action;
    }

    public JMenu createJMenu() {
        JMenu menu = new JMenu(this);
        menu.addMenuListener(new DynamicMenuListener());
        return menu;
    }

    protected abstract void populateMenu(JMenu menu);

    private class DynamicMenuListener implements MenuListener {

        public void menuCanceled(MenuEvent e) { }

        public void menuDeselected(MenuEvent e) { }

        public void menuSelected(MenuEvent e) {
            JMenu menu = (JMenu) e.getSource();
            populateMenu(menu);
        }
    }

}
