package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.exporter.HtmlMatrixExporter;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.ui.actions.BaseAction;
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

		final Heatmap figure = getMatrixFigure();
		if (figure == null)
			return;
		
		try {
			File basePath = FileChooserUtils.getSelectedPath(
					"Select destination folder",
					Settings.getDefault().getLastExportPath());
			
			if (basePath == null)
				return;

			Settings.getDefault().setLastExportPath(basePath.getAbsolutePath());
			
			HtmlMatrixExporter exporter = new HtmlMatrixExporter();
			exporter.setBasePath(basePath);
			exporter.setIndexName("index.html");
			exporter.exportMatrixFigure(figure);
		}
		catch (Exception ex) {
			AppFrame.instance().setStatusText("There was an error exporting: " + ex.getMessage());
		}
		
		AppFrame.instance().setStatusText("Html exported.");
	}
}
