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
package org.gitools.plugins.mutex;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.plugins.mutex.control.MutualExclusiveAnalysisCommand;
import org.gitools.plugins.mutex.ui.MutualExclusiveAnalysisWizard;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import java.awt.event.ActionEvent;

public class MutualExclusiveAnalysisAction extends HeatmapAction {

    public MutualExclusiveAnalysisAction() {
        super("<html>Mutual exclusion & Co-occurrence</html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final MutualExclusiveAnalysisWizard wizard = new MutualExclusiveAnalysisWizard(getHeatmap());

        WizardDialog wizDlg = new WizardDialog(Application.get(), wizard);
        wizDlg.open();
        if (wizDlg.isCancelled()) {
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {

                MutualExclusiveAnalysisCommand command = new MutualExclusiveAnalysisCommand(wizard.createAnalysis());
                JobThread.execute(Application.get(), command);

            }
        });


    }

}
