package org.gitools.ui.actions.edit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.AppFrame;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.BaseAction;

import org.gitools.model.matrix.IMatrixView;

public class SelectAllAction extends BaseAction {

	private static final long serialVersionUID = 3088237733885396229L;

	public SelectAllAction() {
		super("Select all");
		
		setDesc("Select all");
		setSmallIconFromResource(IconNames.selectAll16);
		setLargeIconFromResource(IconNames.selectAll24);
		setMnemonic(KeyEvent.VK_A);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		IMatrixView matrixView = getMatrixView();
		
		if (matrixView != null)
			matrixView.selectAll();
		
		AppFrame.instance()
			.setStatusText("Selected all.");
	}

}
