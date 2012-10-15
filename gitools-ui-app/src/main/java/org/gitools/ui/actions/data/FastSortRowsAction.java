/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.actions.data;

import edu.upf.bg.aggregation.IAggregator;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import edu.upf.bg.aggregation.MultAggregator;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.sort.MatrixViewSorter;
import org.gitools.matrix.sort.ValueSortCriteria;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

public class FastSortRowsAction extends BaseAction {

	private static final long serialVersionUID = -582380114189586206L;

    private ValueSortCriteria.SortDirection currentSort;

	public FastSortRowsAction() {
		super("Sort rows");
		
		setDesc("Sort rows");

        currentSort = ValueSortCriteria.SortDirection.ASCENDING;
        updateIcon();

		setMnemonic(KeyEvent.VK_S);


	}

    private void updateIcon() {

        if (currentSort == ValueSortCriteria.SortDirection.ASCENDING) {
            setSmallIconFromResource(IconNames.sortSelectedColumns16Desc);
            setLargeIconFromResource(IconNames.sortSelectedColumns24Desc);
        }else {
            setSmallIconFromResource(IconNames.sortSelectedColumns16Asc);
            setLargeIconFromResource(IconNames.sortSelectedColumns24Asc);
        }

    }

	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		final IMatrixView matrixView = ActionUtils.getMatrixView();


        // Deduce default Aggregator from the associated ColorScale
        IAggregator defaultAggregator;
        try {
            Heatmap heatmap = ActionUtils.getHeatmap();
            defaultAggregator = heatmap.getActiveCellDecorator().getScale().defaultAggregator();
        } catch (Exception ex) {
            defaultAggregator = MultAggregator.INSTANCE;
        }
        final IAggregator aggregator = defaultAggregator;
		
		if (matrixView == null)
			return;

		final int propIndex = matrixView.getSelectedPropertyIndex();

        final ValueSortCriteria.SortDirection sort = currentSort;
        currentSort = (currentSort == ValueSortCriteria.SortDirection.ASCENDING ? ValueSortCriteria.SortDirection.DESCENDING : ValueSortCriteria.SortDirection.ASCENDING);
        updateIcon();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				ValueSortCriteria[] criteriaArray = new ValueSortCriteria[] {
					new ValueSortCriteria(
							propIndex,
							aggregator,
							sort) };

				monitor.begin("Sorting ...", 1);

				MatrixViewSorter.sortByValue(matrixView, criteriaArray, true, false);

				monitor.end();
			}
		});
		
		AppFrame.instance().setStatusText("Rows sorted.");
	}
}
