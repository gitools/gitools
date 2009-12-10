package org.gitools.ui.actions.edit;

import java.awt.event.ActionEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.model.figure.HeatmapFigure;
import org.gitools.model.matrix.IMatrixView;

public class InvertSelectionAction extends BaseAction {

	private static final long serialVersionUID = 3124483059501436713L;

	public InvertSelectionAction() {
		super("Invert selection");
		
		setDesc("Invert selection");
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof HeatmapFigure
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		IMatrixView matrixView = getMatrixView();
		
		
		if (matrixView != null){	
			matrixView.invertSelection();
		}
		
		AppFrame.instance()
			.setStatusText("Selection inverted");
	}

}
