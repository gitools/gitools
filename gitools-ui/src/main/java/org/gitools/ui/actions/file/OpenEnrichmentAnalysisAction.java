package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gitools.persistence.PersistenceException;

import org.gitools.ui.IconNames;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.jobs.OpenAnalysisJob;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import javax.swing.SwingUtilities;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.dialog.progress.ProgressJob;
import org.gitools.ui.editor.analysis.AnalysisEditor;
import org.gitools.ui.utils.FileChooserUtils;

public class OpenEnrichmentAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public OpenEnrichmentAnalysisAction() {
		super("Enrichment analysis ...");
		setDesc("Open an enrichment analysis from the file system");
		setSmallIconFromResource(IconNames.openAnalysis16);
		setLargeIconFromResource(IconNames.openAnalysis24);
		setMnemonic(KeyEvent.VK_A);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final File selectedFile = FileChooserUtils.selectFile(
				"Select the analysis file",
				Settings.getDefault().getLastPath(),
				FileChooserUtils.MODE_OPEN);
		
		if (selectedFile != null) {
			Settings.getDefault().setLastPath(selectedFile.getParent());
			Settings.getDefault().save();

			new ProgressJob(AppFrame.instance()) {
				@Override
				protected void runJob() {
					final IProgressMonitor monitor = getProgressMonitor();

					try {
						EnrichmentAnalysis analysis =
								(EnrichmentAnalysis) PersistenceManager.getDefault().load(
									selectedFile,
									MimeTypes.ENRICHMENT_ANALYSIS,
									monitor);

						final AnalysisEditor editor = new AnalysisEditor(analysis);

						editor.setName(analysis.getTitle());

						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								AppFrame.instance().getEditorsPanel().addEditor(editor);
								AppFrame.instance().refresh();
							}
						});
					} catch (Exception ex) {
						exception(ex);
					}
				}
			}.execute();
		}
	}
}
