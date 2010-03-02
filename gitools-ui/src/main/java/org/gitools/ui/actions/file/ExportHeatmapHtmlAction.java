package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.exporter.HtmlHeatmapExporter;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.ui.actions.ActionUtils;
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
