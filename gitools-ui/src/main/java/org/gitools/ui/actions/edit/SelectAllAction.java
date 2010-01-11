package org.gitools.ui.actions.edit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.IconNames;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;

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
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		IMatrixView matrixView = ActionUtils.getMatrixView();
		
		if (matrixView != null)
			matrixView.selectAll();
		
		AppFrame.instance()
			.setStatusText("Selected all.");
	}

}
