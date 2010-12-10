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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.exporter.HtmlHeatmapExporter;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;

public class ExportHeatmapHtmlAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportHeatmapHtmlAction() {
		super("Export heatmap as html ...");
		
		setDesc("Export a matrix figure in html format");
		setMnemonic(KeyEvent.VK_H);
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		UnimplementedDialog.show(AppFrame.instance());
		if (true) return;

		final Heatmap figure = ActionUtils.getHeatmap();
		if (figure == null)
			return;

		final File basePath = FileChooserUtils.selectPath(
							"Select destination folder",
							Settings.getDefault().getLastExportPath());

		if (basePath == null)
			return;

		Settings.getDefault().setLastExportPath(basePath.getAbsolutePath());

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Exporting html ...", 1);

					HtmlHeatmapExporter exporter = new HtmlHeatmapExporter();
					exporter.setBasePath(basePath);
					exporter.setIndexName("index.html");
					exporter.exportHeatmap(figure);

					monitor.end();
				}
				catch (Exception ex) {
					monitor.exception(ex);
				}
			}
		});

		AppFrame.instance().setStatusText("Html exported.");
	}
}
