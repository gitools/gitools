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

package org.gitools.ui.welcome;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.gitools.ui.actions.file.ImportBiomartModulesAction;
import org.gitools.ui.actions.file.ImportBiomartTableAction;
import org.gitools.ui.actions.file.ImportGoModulesAction;
import org.gitools.ui.actions.file.ImportIntogenMatrixAction;
import org.gitools.ui.actions.file.ImportIntogenOncomodulesAction;
import org.gitools.ui.actions.file.ImportKeggModulesAction;
import org.gitools.ui.actions.file.NewCombinationAnalysisAction;
import org.gitools.ui.actions.file.NewCorrelationAnalysisAction;
import org.gitools.ui.actions.file.NewOverlappingAnalysisAction;
import org.gitools.ui.actions.file.NewEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.NewOncodriveAnalysisAction;

import org.gitools.ui.actions.file.OpenAnalysisAction;
import org.gitools.ui.actions.file.OpenHeatmapAction;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.editor.Html4Editor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.slf4j.LoggerFactory;


public class WelcomeEditor extends Html4Editor {

	private static final long serialVersionUID = 6851947500231401412L;

	public WelcomeEditor() {
		super("Welcome");

		try {
			URL url = getClass().getResource("/html/welcome.html");
			navigate(url);
		} catch (Exception e) {
			ExceptionDialog.show(AppFrame.instance(), e);
		}
	}

	@Override
	protected void performUrlAction(String name, Map<String, String> params) {
		if (name.equals("goHome")) {
			try {
				Desktop.getDesktop().browse(new URI("http://www.gitools.org"));
			}
			catch (Exception ex) {
				ExceptionDialog.show(AppFrame.instance(), ex);
			}
		}
		else if (name.equals("importIntogen")) {
			IntogenTypeDialog dlg = new IntogenTypeDialog(AppFrame.instance());
			dlg.setVisible(true);
			if (!dlg.isCancelled()) {
				switch (dlg.getSelection()) {
					case IntogenTypeDialog.MATRIX:
						new ImportIntogenMatrixAction()
								.actionPerformed(new ActionEvent(this, 0, name));
						break;

					case IntogenTypeDialog.ONCOMODULES:
						new ImportIntogenOncomodulesAction()
								.actionPerformed(new ActionEvent(this, 0, name));
						break;
				}
			}
		}
		else if (name.equals("importGo")) {
			new ImportGoModulesAction()
					.actionPerformed(new ActionEvent(this, 0, name));
		}
		else if (name.equals("importKegg")) {
			new ImportKeggModulesAction()
					.actionPerformed(new ActionEvent(this, 0, name));
		}
		else if (name.equals("importBiomart")) {
			BiomartTypeDialog dlg = new BiomartTypeDialog(AppFrame.instance());
			dlg.setVisible(true);
			if (!dlg.isCancelled()) {
				switch (dlg.getSelection()) {
					case BiomartTypeDialog.TABLE:
						new ImportBiomartTableAction()
								.actionPerformed(new ActionEvent(this, 0, name));
						break;

					case BiomartTypeDialog.MODULES:
						new ImportBiomartModulesAction()
								.actionPerformed(new ActionEvent(this, 0, name));
						break;
				}
			}
		}
		else if (name.equals("analysis")) {
			final Map<String, Class<? extends BaseAction>> actions =
					new HashMap<String, Class<? extends BaseAction>>();
			
			actions.put("Enrichment", NewEnrichmentAnalysisAction.class);
			actions.put("Oncodrive", NewOncodriveAnalysisAction.class);
			actions.put("Correlations", NewCorrelationAnalysisAction.class);
			actions.put("Overlapping", NewOverlappingAnalysisAction.class);
			actions.put("Combination", NewCombinationAnalysisAction.class);
			
			String ref = params.get("ref");
			Class<? extends BaseAction> actionClass = actions.get(ref);
			if (actionClass != null) {
				try {
					ActionEvent event = new ActionEvent(this, 0, name);
					actionClass.newInstance().actionPerformed(event);
				} catch (Exception ex) {
					ExceptionDialog.show(AppFrame.instance(), ex);
				}
			}
			else {
				UnimplementedDialog.show(AppFrame.instance());
			}
		}
		else if (name.equals("open")) {
			String ref = params.get("ref");
			if (ref.equals("Analysis"))
				new OpenAnalysisAction().actionPerformed(new ActionEvent(this, 0, name));
			else if (ref.equals("DataHeatmap"))
				new OpenHeatmapAction().actionPerformed(new ActionEvent(this, 0, name));
		}
		else if (name.equals("example")) {
			LoggerFactory.getLogger(WelcomeEditor.class).debug("example: " + params);
		}
		else if (name.equals("downloadExamples")) {
			DownloadExamplesDialog dlg = new DownloadExamplesDialog(AppFrame.instance());
			dlg.setPath(Settings.getDefault().getLastWorkPath());
			dlg.setVisible(true);
			downloadExamples(dlg.getPath());
		}
		else if (name.equals("dataMatrices")
				|| name.equals("dataModules")
				|| name.equals("dataTables")) {
				DataHelpDialog dlg = new DataHelpDialog(AppFrame.instance());
				dlg.setVisible(true);
		}
	}

	@Override
	public void doVisible() {
		AppFrame.instance().setLeftPanelVisible(false);
	}

	private void downloadExamples(final String path) {
		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Connecting ...", 1);

					URL url = new URL("http://webstart.gitools.org/examples.zip");

					ZipInputStream zin = new ZipInputStream(url.openStream());

					File pathFile = new File(path);

					monitor.end();
					
					monitor.begin("Downloading ...", 1);

					ZipEntry ze;
					while ((ze = zin.getNextEntry()) != null) {
						IProgressMonitor mnt = monitor.subtask();

						long totalKb = ze.getSize() / 1024;

						String name = ze.getName();

						mnt.begin("Extracting " + name + " ...", (int) ze.getSize());

						File outFile = new File(pathFile, name);
						if (!outFile.getParentFile().exists())
							outFile.getParentFile().mkdirs();

						OutputStream fout = new FileOutputStream(outFile);

						final int BUFFER_SIZE = 4 * 1024;
						byte[] data = new byte[BUFFER_SIZE];
						int partial = 0;
						int count;
						while ((count = zin.read(data, 0, BUFFER_SIZE)) != -1) {
							fout.write(data, 0, count);
							partial += count;
							mnt.info((partial / 1024) + " Kb read");
							mnt.worked(count);
						}

						zin.closeEntry();
						fout.close();

						mnt.end();
					}

					zin.close();
					
					monitor.end();
				}
				catch (Exception ex) {
					monitor.exception(ex);
				}
			}
		});
	}
}
