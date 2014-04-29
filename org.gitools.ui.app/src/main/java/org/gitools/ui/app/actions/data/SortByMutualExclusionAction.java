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
package org.gitools.ui.app.actions.data;

import org.gitools.analysis.mutualexclusive.MutualExclusiveAnalysis;
import org.gitools.analysis.mutualexclusive.MutualExclusiveProcessor;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.MatrixViewSorter;
import org.gitools.matrix.model.iterable.IdentifiersPredicate;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.matrix.modulemap.HashModuleMap;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.app.sort.MutualExclusionSortPage;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import java.awt.event.ActionEvent;

public class SortByMutualExclusionAction extends HeatmapAction {

    private MatrixDimensionKey dimensionKey;

    public SortByMutualExclusionAction(MatrixDimensionKey dimensionKey) {
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

                MatrixViewSorter.sortByMutualExclusion(
                        hm,
                        page.getPattern(),
                        page.getValues(),
                        page.isUseRegexChecked(),
                        page.getDimension().equals(MatrixDimensionKey.COLUMNS),
                        monitor,
                        Settings.get().isShowMutualExclusionProgress()
                );

                if (page.performTest()) {
                    monitor.begin("Analyse ...", 1);

                    MutualExclusiveAnalysis analysis = new MutualExclusiveAnalysis();

                    HashModuleMap weightMap = new HashModuleMap();
                    HashModuleMap testMap = new HashModuleMap();
                    IMatrixDimension weightDimension = dimensionKey.equals(MatrixDimensionKey.ROWS) ?
                            hm.getDimension(MatrixDimensionKey.COLUMNS) :
                            hm.getDimension(MatrixDimensionKey.ROWS);
                    IMatrixDimension testDimension = hm.getContents().getDimension(dimensionKey);
                    AnnotationMatrix testDimensionAnnotations = hm.getDimension(dimensionKey).getAnnotations();

                    weightMap.addMapping(weightDimension.getId().getLabel(), hm.newPosition().iterate(weightDimension));

                    testMap.addMapping(testDimension.getId().getLabel(), hm.newPosition().
                            iterate(testDimension)
                            .filter(new IdentifiersPredicate<String>(testDimension, page.getValues(), page.getPattern(), testDimensionAnnotations)));

                    analysis.setTestDimension(testDimension);
                    analysis.setTestGroupsModuleMap(testMap);
                    analysis.setWeightDimension(weightDimension);
                    analysis.setWeightGroupsModuleMap(weightMap);
                    analysis.setData(hm);
                    analysis.setLayer(hm.getLayers().getTopLayer().getId());
                    analysis.setCutoffCmp(CutoffCmp.NE);
                    analysis.setCutoff(0);

                    MutualExclusiveProcessor processor = new MutualExclusiveProcessor(analysis);


                    processor.run(monitor);
                    analysis.getResults();

                    System.out.println(analysis.toString());
                }
            }
        });
        Application.get().setStatusText("Sorted.");
    }
}
