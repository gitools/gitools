package es.imim.bg.ztools.ui.dialogs;

import javax.swing.JFrame;

public class SortRowsDialog extends SortDialog {

	private static final long serialVersionUID = 6485721953671431425L;

	public SortRowsDialog(JFrame owner, Object[] properties) {
		super(owner, properties, "Consider all columns", "Consider also hidden rows");
	}

}
