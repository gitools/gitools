/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.model.Analysis;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.analysis.correlation.editor.CorrelationAnalysisEditor;
import org.gitools.ui.analysis.groupcomparison.editor.GroupComparisonAnalysisEditor;
import org.gitools.ui.analysis.htest.editor.EnrichmentAnalysisEditor;
import org.gitools.ui.analysis.htest.editor.OncodriveAnalysisEditor;
import org.gitools.ui.analysis.overlapping.OverlappingAnalysisEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;
import org.gitools.ui.utils.FileFormatFilter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class OpenAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public OpenAnalysisAction() {
		super("Analysis ...");
		setDesc("Open an analysis from the file system");
		setSmallIconFromResource(IconNames.openAnalysis16);
		setLargeIconFromResource(IconNames.openAnalysis24);
		setMnemonic(KeyEvent.VK_A);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FileFilter[] filters = new FileFilter[] {
			new FileFormatFilter("Any analysis", null, new FileFormat[] {
				FileFormats.ENRICHMENT,
				FileFormats.ONCODRIVE,
				FileFormats.CORRELATIONS,
				FileFormats.COMBINATION,
				FileFormats.OVERLAPPING,
                FileFormats.GROUP_COMPARISON
			}),
			new FileFormatFilter(FileFormats.ENRICHMENT),
			new FileFormatFilter(FileFormats.ONCODRIVE),
			new FileFormatFilter(FileFormats.CORRELATIONS),
			new FileFormatFilter(FileFormats.COMBINATION),
			new FileFormatFilter(FileFormats.OVERLAPPING),
            new FileFormatFilter(FileFormats.GROUP_COMPARISON)
		};

		FileChooserUtils.FileAndFilter ret = FileChooserUtils.selectFile(
				"Select the analysis file",
				Settings.getDefault().getLastPath(),
				FileChooserUtils.MODE_OPEN,
				filters);

		if (ret == null)
			return;
		
		final File file = ret.getFile();
		final FileFormatFilter filter = (FileFormatFilter) ret.getFilter();

		if (file != null) {
			Settings.getDefault().setLastPath(file.getParent());
			Settings.getDefault().save();

			JobThread.execute(AppFrame.instance(), new JobRunnable() {
				@Override
				public void run(IProgressMonitor monitor) {
					try {
						AbstractEditor editor = null;

						String mime = filter.getMime();
						if (mime == null)
							mime = PersistenceManager.getDefault().getMimeFromFile(file.getName());
						
						Analysis analysis =	(Analysis) PersistenceManager.getDefault()
								.load(file, mime, monitor);

						if (monitor.isCancelled())
							return;

						if (mime.equals(MimeTypes.ENRICHMENT_ANALYSIS))
							editor = new EnrichmentAnalysisEditor((EnrichmentAnalysis) analysis);
						else if (mime.equals(MimeTypes.ONCODRIVE_ANALYSIS))
							editor = new OncodriveAnalysisEditor((OncodriveAnalysis) analysis);
						else if (mime.equals(MimeTypes.CORRELATIONS_ANALYSIS))
							editor = new CorrelationAnalysisEditor((CorrelationAnalysis) analysis);
						else if (mime.equals(MimeTypes.COMBINATION_ANALYSIS))
							editor = new CombinationAnalysisEditor((CombinationAnalysis) analysis);
						else if (mime.equals(MimeTypes.OVERLAPPING_ANALYSIS))
							editor = new OverlappingAnalysisEditor((OverlappingAnalysis) analysis);
                        else if (mime.equals(MimeTypes.GROUPCOMPARISON_ANALYSIS))
                            editor = new GroupComparisonAnalysisEditor((GroupComparisonAnalysis) analysis);

						editor.setName(file.getName());
                        editor.abbreviateName(Settings.getDefault().getEditorTabLength());

                        final AbstractEditor newEditor = editor;
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								AppFrame.instance().getEditorsPanel().addEditor(newEditor);
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
