package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.ui.analysis.combination.wizard.CombinationAnalysisWizard;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.AppFrame;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.wizard.WizardDialog;

public class NewCombinationAnalysisAction extends BaseAction {

	private static final long serialVersionUID = 4604642713057641252L;

	public NewCombinationAnalysisAction() {
		super("Combination analysis ...");
		
		setDesc("Run a combination analysis");
		setMnemonic(KeyEvent.VK_B);

		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final CombinationAnalysisWizard wizard = new CombinationAnalysisWizard();

		WizardDialog wizDlg = new WizardDialog(AppFrame.instance(), wizard);

		wizDlg.open();

		if (wizDlg.isCancelled())
			return;

		/*final CombinationAnalysis analysis = wizard.getAnalysis();

		final CorrelationCommand cmd = new CorrelationCommand(
				analysis,
				wizard.getDataFileMime(),
				wizard.getDataFile().getAbsolutePath(),
				wizard.getWorkdir(),
				wizard.getFileName());

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					cmd.run(monitor);

					if (monitor.isCancelled())
						return;

					final CorrelationAnalysisEditor editor = new CorrelationAnalysisEditor(analysis);

					editor.setName(PersistenceUtils.getFileName(wizard.getFileName()) + "." + FileSuffixes.HEATMAP);

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
		});*/
	}

}
