package es.imim.bg.ztools.ui.dialogs;

import javax.swing.JFrame;

public class SortColumnsDialog extends SortDialog {

	private static final long serialVersionUID = -4169404269473232755L;

	public SortColumnsDialog(JFrame owner, Object[] properties) {
		super(owner, properties, "Consider also hidden columns", "Consider all rows");
	}

}
