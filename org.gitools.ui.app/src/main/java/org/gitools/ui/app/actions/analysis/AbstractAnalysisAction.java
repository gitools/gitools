/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.actions.analysis;

import com.google.common.base.Strings;
import org.gitools.analysis.Analysis;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.api.ApplicationContext;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.components.editor.AbstractEditor;
import org.gitools.ui.core.components.wizard.AnalysisWizard;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class AbstractAnalysisAction<A extends Analysis> extends HeatmapAction {

    public AbstractAnalysisAction(String name, int mnemonic) {
        super(name);
        setMnemonic(mnemonic);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AnalysisWizard<? extends A> wizard = newWizard(getHeatmap());
        WizardDialog wizDlg = new WizardDialog(Application.get(), wizard);
        wizDlg.open();
        if (wizDlg.isCancelled()) {
            return;
        }

        final A analysis = wizard.createAnalysis();
        final AnalysisProcessor cmd = newProcessor(analysis);

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                cmd.run(monitor);
                if (monitor.isCancelled()) {
                    return;
                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        AbstractEditor editor = (AbstractEditor) ApplicationContext.getEditorManger().createEditor(analysis);

                        String title = analysis.getTitle();

                        if (Strings.isNullOrEmpty(title)) {
                            title = editor.getClass().getSimpleName().replace("Editor", "").toLowerCase();
                        }

                        editor.setName(title);

                        Application.get().getEditorsPanel().addEditor(editor);
                        Application.get().refresh();
                    }
                });
            }
        });
    }

    protected abstract AnalysisWizard<? extends A> newWizard(Heatmap heatmap);

    protected abstract AnalysisProcessor newProcessor(A analysis);
}
