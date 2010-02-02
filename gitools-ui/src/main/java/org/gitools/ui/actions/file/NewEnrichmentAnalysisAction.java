package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentCommand;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.analysis.htest.wizard.EnrichmentAnalysisWizard;
import org.gitools.ui.dialog.progress.ProgressJob;
import org.gitools.ui.editor.analysis.AnalysisEditor;

public class NewEnrichmentAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public NewEnrichmentAnalysisAction() {
		super("Enrichment analysis ...");

		setDesc("Run an enrichment analysis");
		setMnemonic(KeyEvent.VK_E);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		EnrichmentAnalysisWizard wizard = new EnrichmentAnalysisWizard();
		
		WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(), wizard);
		
		wizDlg.open();

		if (wizDlg.isCancelled())
			return;

		final EnrichmentAnalysis analysis = wizard.getAnalysis();

		final EnrichmentCommand cmd = new EnrichmentCommand(
				analysis,
				wizard.getDataFileMime(),
				wizard.getDataFile().getAbsolutePath(),
				wizard.getModulesFileMime(),
				wizard.getModulesFile().getAbsolutePath(),
				wizard.getWorkdir(),
				wizard.getFileName());

		new ProgressJob(null) {
			@Override
			protected void runJob() {
				IProgressMonitor monitor = getProgressMonitor();

				try {
					cmd.run(monitor);

					final AnalysisEditor editor = new AnalysisEditor(analysis);

					editor.setName(analysis.getTitle());

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
				catch (Exception ex) {
					exception(ex);
				}
			}
		}.execute();
	}
}
