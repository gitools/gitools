package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.actions.UnimplementedAction;

public class ImportEnsemblAnnotationsAction extends BaseAction {

	private static final long serialVersionUID = 4381993756203388654L;

	public ImportEnsemblAnnotationsAction() {
		super("Ensembl annotations ...");
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new UnimplementedAction().actionPerformed(e);
	}

}
