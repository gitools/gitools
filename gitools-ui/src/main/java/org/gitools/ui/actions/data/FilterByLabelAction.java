package org.gitools.ui.actions.data;

import java.awt.event.ActionEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapMatrixViewAdapter;
import org.gitools.matrix.filter.MatrixViewLabelFilter;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.filter.LabelFilterDialog;
import org.gitools.ui.platform.editor.IEditor;

public class FilterByLabelAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public FilterByLabelAction() {
		super("Filter by label...");
		setDesc("Filter by label");
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		final IMatrixView matrixView = ActionUtils.getHeatmapMatrixView();

		if (matrixView == null)
			return;

		LabelFilterDialog dlg = new LabelFilterDialog(AppFrame.instance());
		dlg.setVisible(true);

		if (dlg.getReturnStatus() == LabelFilterDialog.RET_OK) {
			MatrixViewLabelFilter.filter(matrixView, dlg.getValues(),
					dlg.isUseRegexChecked(),
					dlg.isApplyToRowsChecked(),
					dlg.isApplyToColumnsChecked());

			AppFrame.instance().setStatusText("Filter by label done.");
		}
		else
			AppFrame.instance().setStatusText("Filter cancelled.");
	}
}
