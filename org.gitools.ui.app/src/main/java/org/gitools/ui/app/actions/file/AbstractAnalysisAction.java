package org.gitools.ui.app.actions.file;

import org.gitools.analysis.Analysis;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.analysis.htest.wizard.AnalysisWizard;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class AbstractAnalysisAction<A extends Analysis> extends HeatmapAction {

    public AbstractAnalysisAction(String name) {
        super(name);
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
                try {
                    cmd.run(monitor);
                    if (monitor.isCancelled()) {
                        return;
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            AbstractEditor editor = newEditor(analysis);
                            editor.setName(analysis.getTitle());

                            Application.get().getEditorsPanel().addEditor(editor);
                            Application.get().refresh();
                        }
                    });

                    monitor.end();

                    Application.get().setStatusText("Ok.");
                } catch (Throwable ex) {
                    monitor.exception(ex);
                }
            }
        });
    }

    protected abstract AbstractEditor newEditor(A analysis);

    protected abstract AnalysisWizard<? extends A> newWizard(Heatmap heatmap);

    protected abstract AnalysisProcessor newProcessor(A analysis);
}
