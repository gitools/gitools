package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import javax.swing.SwingUtilities;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.ui.dialog.progress.JobRunnable;
import org.gitools.ui.dialog.progress.JobThread;
import org.gitools.ui.analysis.htest.editor.HtestAnalysisEditor;
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
		final File file = FileChooserUtils.selectFile(
				"Select the analysis file",
				Settings.getDefault().getLastPath(),
				FileChooserUtils.MODE_OPEN);
		
		if (file != null) {
			Settings.getDefault().setLastPath(file.getParent());
			Settings.getDefault().save();

			JobThread.execute(AppFrame.instance(), new JobRunnable() {
				@Override
				public void run(IProgressMonitor monitor) {
					try {
						EnrichmentAnalysis analysis =
								(EnrichmentAnalysis) PersistenceManager.getDefault().load(
									file,
									MimeTypes.ENRICHMENT_ANALYSIS,
									monitor);

						if (monitor.isCancelled())
							return;

						final HtestAnalysisEditor editor = new HtestAnalysisEditor(analysis);

						editor.setName(PersistenceUtils.getBaseName(file.getName()));

						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								AppFrame.instance().getEditorsPanel().addEditor(editor);
								AppFrame.instance().refresh();
							}
						});
					} catch (Exception ex) {
						monitor.exception(ex);
					}
				}
			});
		}
	}
}
