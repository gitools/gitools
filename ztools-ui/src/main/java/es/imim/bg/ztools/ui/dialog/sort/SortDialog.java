package es.imim.bg.ztools.ui.dialog.sort;

import java.awt.Window;

import javax.swing.JDialog;

import es.imim.bg.ztools.aggregation.IAggregator;

public class SortDialog extends JDialog {
	
	private static final long serialVersionUID = -4832384734759927405L;
	
	protected Object[] properties;
	protected IAggregator[] aggregators;
	
	public SortDialog(Window owner, String title, boolean modal) {
		super(owner, title);
		setModal(modal);
	}
}
