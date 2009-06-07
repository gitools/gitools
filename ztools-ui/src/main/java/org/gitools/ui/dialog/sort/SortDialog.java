package org.gitools.ui.dialog.sort;

import java.awt.Window;

import javax.swing.JDialog;

import org.gitools.aggregation.IAggregator;

public abstract class SortDialog extends JDialog {
	
	private static final long serialVersionUID = -4832384734759927405L;
	
	protected Object[] properties;
	protected IAggregator[] aggregators;
	
	public SortDialog(Window owner, String title, boolean modal) {
		super(owner, title);
		setModal(modal);
	}
}
