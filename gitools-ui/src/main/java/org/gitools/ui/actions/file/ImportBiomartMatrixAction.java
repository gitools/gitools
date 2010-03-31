package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import org.gitools.ui.IconNames;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.AppFrame;

public class ImportBiomartMatrixAction extends BaseAction {

	private static final long serialVersionUID = 4381993756203388654L;

	public ImportBiomartMatrixAction() {
		super("Matrix ...");
		setLargeIconFromResource(IconNames.biomart24);
		setSmallIconFromResource(IconNames.biomart16);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		UnimplementedDialog.show(AppFrame.instance());
	}

}
