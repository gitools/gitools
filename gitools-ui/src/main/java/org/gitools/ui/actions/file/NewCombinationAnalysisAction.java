package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.SwingUtilities;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.combination.CombinationCommand;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.ui.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.analysis.combination.wizard.CombinationAnalysisWizard;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.AppFrame;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
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

		final CombinationAnalysis analysis = wizard.getAnalysis();

		final String analysisPath = wizard.getSaveFilePage().getFilePath();
		File columnSetsFile = wizard.getColumnSetsPage().getFile();
		String columnSetsPath = columnSetsFile != null ? columnSetsFile.getAbsolutePath() : null;
		String columnSetsMime = columnSetsFile != null ? wizard.getColumnSetsPage().getFileFormat().getMime() : null;

		final CombinationCommand cmd = new CombinationCommand(
				analysis,
				wizard.getDataFilePage().getFileFormat().getMime(),
				wizard.getDataFilePage().getFile().getAbsolutePath(),
				columnSetsMime,
				columnSetsPath,
				wizard.getSaveFilePage().getFolder(),
				analysisPath);

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					cmd.run(monitor);

					if (monitor.isCancelled())
						return;

					final CombinationAnalysisEditor editor = new CombinationAnalysisEditor(analysis);

					editor.setName(PersistenceUtils.getFileName(analysisPath) + "." + FileSuffixes.HEATMAP);

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
