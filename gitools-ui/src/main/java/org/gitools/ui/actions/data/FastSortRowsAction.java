package org.gitools.ui.actions.data;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;
import org.gitools.aggregation.MultAggregator;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.matrix.MatrixUtils;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.sort.MatrixViewSorter;
import org.gitools.matrix.sort.SortCriteria;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

public class FastSortRowsAction extends BaseAction {

	private static final long serialVersionUID = -582380114189586206L;

	public FastSortRowsAction() {
		super("Sort rows");
		
		setDesc("Sort rows");
		setSmallIconFromResource(IconNames.sortSelectedColumns16);
		setLargeIconFromResource(IconNames.sortSelectedColumns24);
		setMnemonic(KeyEvent.VK_S);
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		final IMatrixView matrixView = ActionUtils.getMatrixView();
		
		if (matrixView == null)
			return;

		final int propIndex = matrixView.getSelectedPropertyIndex();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				SortCriteria[] criteriaArray = new SortCriteria[] {
					new SortCriteria(
							propIndex,
							new MultAggregator(),
							SortCriteria.SortDirection.ASCENDING) };

				monitor.begin("Sorting ...", 1);

				MatrixViewSorter.sortByValue(matrixView, criteriaArray, true, false);

				monitor.end();
			}
		});
		
		AppFrame.instance().setStatusText("Rows sorted.");
	}
}
