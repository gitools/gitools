package org.gitools.ui.actions.edit;

import java.awt.event.ActionEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;

public class InvertSelectionAction extends BaseAction {

	private static final long serialVersionUID = 3124483059501436713L;

	public InvertSelectionAction() {
		super("Invert selection");
		
		setDesc("Invert selection");
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		IMatrixView matrixView = ActionUtils.getMatrixView();
		
		
		if (matrixView != null){	
			matrixView.invertSelection();
		}
		
		AppFrame.instance()
			.setStatusText("Selection inverted");
	}

}
