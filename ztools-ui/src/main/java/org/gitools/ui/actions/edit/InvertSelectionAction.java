package org.gitools.ui.actions.edit;

import java.awt.event.ActionEvent;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;

import org.gitools.model.table.IMatrixView;

public class InvertSelectionAction extends BaseAction {

	private static final long serialVersionUID = 3124483059501436713L;

	public InvertSelectionAction() {
		super("Invert selection");
		
		setDesc("Invert selection");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		IMatrixView matrixView = getTable();
		
		
		if (matrixView != null){	
			matrixView.invertSelection();
		}
		
		AppFrame.instance()
			.setStatusText("Selection inverted");
	}

}
