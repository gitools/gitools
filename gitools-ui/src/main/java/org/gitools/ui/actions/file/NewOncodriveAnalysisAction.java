package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.SwingUtilities;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.analysis.htest.oncozet.OncodriveCommand;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.ui.analysis.htest.editor.HtestAnalysisEditor;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.analysis.htest.wizard.OncodriverAnalysisWizard;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

public class NewOncodriveAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public NewOncodriveAnalysisAction() {
		super("OncoDrive analysis ...");

		setDesc("Run an oncodrive analysis");
		setMnemonic(KeyEvent.VK_O);
		
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		UnimplementedDialog.show(AppFrame.instance());
		if (true) return;

		final OncodriverAnalysisWizard wizard = new OncodriverAnalysisWizard();

		WizardDialog wizDlg = new WizardDialog(AppFrame.instance(), wizard);

		wizDlg.open();

		if (wizDlg.isCancelled())
			return;

		final OncodriveAnalysis analysis = wizard.getAnalysis();

		File populationFile = wizard.getPopulationFile();

		final OncodriveCommand cmd = new OncodriveCommand(
				analysis,
				wizard.getDataFileMime(),
				wizard.getDataFile().getAbsolutePath(),
				populationFile != null ? populationFile.getAbsolutePath() : null,
				wizard.getModulesFileMime(),
				wizard.getModulesFile().getAbsolutePath(),
				wizard.getWorkdir(),
				wizard.getFileName());

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					cmd.run(monitor);

					if (monitor.isCancelled())
						return;

					final HtestAnalysisEditor editor = new HtestAnalysisEditor(analysis);

					editor.setName(PersistenceUtils.getBaseName(wizard.getFileName()));

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							AppFrame.instance().getEditorsPanel().addEditor(editor);
							AppFrame.instance().refresh();
						}
					});

					monitor.end();

					AppFrame.instance().setStatusText("Done.");
				}
				catch (Throwable ex) {
					monitor.exception(ex);
				}
			}
		});
	}
}
