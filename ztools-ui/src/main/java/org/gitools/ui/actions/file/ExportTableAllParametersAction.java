package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.gitools.exporter.TextMatrixExporter;
import org.gitools.model.figure.heatmap.Heatmap;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.element.IElementProperty;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;

public class ExportTableAllParametersAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportTableAllParametersAction() {
		super("Export table (all parameters)");
		
		setDesc("Export a data table with all parameters");
		setMnemonic(KeyEvent.VK_A);
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		IMatrixView matrixView = getMatrixView();
		if (matrixView == null)
			return;
		
		final List<IElementProperty> properties = matrixView.getCellAdapter().getProperties();
		final int[] propIndices = new int[properties.size()];
		for (int i = 0; i < properties.size(); i++)
			propIndices[i] = i;
		
		try {
			File file = getSelectedFile(
					"Select destination file",
					Settings.getDefault().getLastExportPath());
			if (file == null)
				return;
			
			Settings.getDefault().setLastExportPath(file.getAbsolutePath());
			
			TextMatrixExporter.exportProperties(matrixView, propIndices, file);
		}
		catch (IOException ex) {
			AppFrame.instance().setStatusText("There was an error exporting the data: " + ex.getMessage());
		}
		
		AppFrame.instance().setStatusText("Table exported.");
	}

}
