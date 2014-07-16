/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.plugins.mutex.actions;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.iterable.IdentifiersPredicate;
import org.gitools.matrix.model.matrix.element.LayerAdapter;
import org.gitools.plugins.mutex.MutualExclusiveBookmark;
import org.gitools.plugins.mutex.MutualExclusivePlugin;
import org.gitools.plugins.mutex.analysis.MutualExclusiveAnalysis;
import org.gitools.plugins.mutex.analysis.MutualExclusiveProcessor;
import org.gitools.plugins.mutex.analysis.MutualExclusiveResult;
import org.gitools.plugins.mutex.control.MutexAnalysisCommand;
import org.gitools.plugins.mutex.sort.MutualExclusiveMatrixViewSorter;
import org.gitools.plugins.mutex.ui.MutualExclusionSortPage;
import org.gitools.plugins.mutex.ui.MutualExclusiveResultPage;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.components.boxes.BoxManager;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.ui.platform.wizard.PageDialog;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;

public class MutualExclusiveSortAction extends HeatmapAction {

    private MatrixDimensionKey dimensionKey;

    public MutualExclusiveSortAction(MatrixDimensionKey dimensionKey) {
        super("<html><i>Sort</i> by mutual exclusion</html>");

        this.dimensionKey = dimensionKey;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final Heatmap hm = getHeatmap();

        final MutualExclusionSortPage page = new MutualExclusionSortPage(hm, dimensionKey);
        PageDialog dlg = new PageDialog(Application.get(), page);

        dlg.open();

        if (dlg.isCancelled()) {
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Sorting ...", 1);

                MutualExclusiveMatrixViewSorter.sortByMutualExclusion(
                        hm,
                        page.getPattern(),
                        page.getValues(),
                        page.isUseRegexChecked(),
                        page.getDimension().equals(MatrixDimensionKey.COLUMNS),
                        monitor,
                        Settings.get().isShowMutualExclusionProgress()
                );

                if (page.performTest()) {

                    //TEST
                    monitor.begin("Analyse ...", 1);

                    MutualExclusiveAnalysis analysis = new MutualExclusiveAnalysis();

                    IMatrixDimension testDimension = hm.getContents().getDimension(dimensionKey);
                    ArrayList<String> selected = newArrayList(
                            hm.newPosition()
                                    .iterate(testDimension)
                                    .filter(new IdentifiersPredicate<String>(testDimension, page.getValues(),
                                            page.getPattern(), hm.getDimension(dimensionKey).getAnnotations()))
                    );
                    MutexAnalysisCommand.prepareSingularAnalysis(analysis, testDimension, selected, hm);

                    MutualExclusiveProcessor processor = new MutualExclusiveProcessor(analysis);
                    processor.run(monitor);
                    if (monitor.isCancelled()) {
                        return;
                    }
                    IMatrix resultsMatrix = analysis.getResults().get();

                    MutualExclusivePlugin plugin = (MutualExclusivePlugin) getHeatmap().getPluggedBoxes().get(MutualExclusivePlugin.ID);
                    if (plugin == null) {
                        return;
                    }

                    //DIALOG
                    LayerAdapter<MutualExclusiveResult> adapter = new LayerAdapter<>(MutualExclusiveResult.class);
                    MutualExclusiveResult result = adapter.get(resultsMatrix, resultsMatrix.newPosition());


                    String name = "Mutex result";
                    String space = " ";

                    while (!plugin.uniqueName(name)) {
                        name = name + space + "I";
                        space = "";
                    }

                    MutualExclusiveBookmark bookmark = (dimensionKey == MatrixDimensionKey.ROWS) ?
                            new MutualExclusiveBookmark(name, selected, hm.getColumns().toList(),
                                    hm.getLayers().getTopLayer().getId(), testDimension.getId(), result) :
                            new MutualExclusiveBookmark(name, hm.getRows().toList(), selected,
                                    hm.getLayers().getTopLayer().getId(), testDimension.getId(), result);

                    MutualExclusiveResultPage resultPage = new MutualExclusiveResultPage(hm, bookmark);
                    PageDialog dlg = new PageDialog(Application.get(), resultPage);


                    dlg.open();

                    if (dlg.isCancelled()) {
                        return;
                    }

                    plugin.add(bookmark);

                    BoxManager.openOnly(MutualExclusivePlugin.ID);

                    Application.get().showNotification("Mutual exclusive sorting applied.");

                }
            }
        });
    }

}
