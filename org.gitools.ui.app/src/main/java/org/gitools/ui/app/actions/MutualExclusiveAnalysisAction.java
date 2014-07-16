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
package org.gitools.ui.app.actions;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.plugins.mutex.control.MutexAnalysisCommand;
import org.gitools.plugins.mutex.ui.MutualExclusiveAnalysisPage;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.PageDialog;

import java.awt.event.ActionEvent;
import java.util.concurrent.CountDownLatch;


//TODO: move to plugin when EditorManager weld works well!!!

public class MutualExclusiveAnalysisAction extends HeatmapAction {


    public MutualExclusiveAnalysisAction() {
        super("<html>Mutual exclusion & Co-occurrence</html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final Heatmap hm = getHeatmap();

        final MutualExclusiveAnalysisPage page = new MutualExclusiveAnalysisPage(hm);
        PageDialog dlg = new PageDialog(Application.get(), page);

        dlg.open();

        if (dlg.isCancelled()) {
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {

                MutexAnalysisCommand command = new MutexAnalysisCommand("",
                        MatrixDimensionKey.ROWS,
                        "ascending",
                        page.getColumnGroupsPattern(),
                        page.getRowsGroupsPattern(),
                        page.isAllColumnsGroup());

                CountDownLatch waitingLatch = new CountDownLatch(1);
                JobThread.execute(Application.get(), command, waitingLatch);

                try {
                    waitingLatch.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                Application.get().getEditorsPanel().addEditor(new HeatmapEditor(command.getResults()));
            }
        });



    }

}
