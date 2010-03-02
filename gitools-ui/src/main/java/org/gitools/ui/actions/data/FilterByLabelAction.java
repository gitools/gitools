package org.gitools.ui.actions.data;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.filter.MatrixViewLabelFilter;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.filter.LabelFilterDialog;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

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

		final LabelFilterDialog dlg =
				new LabelFilterDialog(AppFrame.instance());

		dlg.setVisible(true);

		if (dlg.getReturnStatus() != LabelFilterDialog.RET_OK) {
			AppFrame.instance().setStatusText("Filter cancelled.");
			return;
		}

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				monitor.begin("Filtering ...", 1);

				MatrixViewLabelFilter.filter(matrixView,
						dlg.getValues(),
						dlg.isUseRegexChecked(),
						dlg.isApplyToRowsChecked(),
						dlg.isApplyToColumnsChecked());

				monitor.end();
			}
		});

		AppFrame.instance().setStatusText("Filter by label done.");
	}
}
