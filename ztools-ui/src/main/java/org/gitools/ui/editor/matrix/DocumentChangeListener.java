package org.gitools.ui.editor.matrix;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class DocumentChangeListener implements DocumentListener {

	@Override
	public void changedUpdate(DocumentEvent e) {
		update(e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		update(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		update(e);
	}

	protected abstract void update(DocumentEvent e);
}
