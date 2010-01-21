package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.gitools.exporter.TextMatrixViewExporter;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.dialog.attributes.AttributesSelectionDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;

public class ExportTable extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportTable() {
		super("Export table ...");
		
		setDesc("Export a table");
		setMnemonic(KeyEvent.VK_A);
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		IMatrixView matrixView = ActionUtils.getMatrixView();
		if (matrixView == null)
			return;

		final List<IElementAttribute> properties = matrixView.getCellAdapter().getProperties();
		final String[] attributeNames = new String[properties.size()];
		for (int i = 0; i < properties.size(); i++)
			attributeNames[i] = properties.get(i).getName();

		AttributesSelectionDialog dlg = new AttributesSelectionDialog(AppFrame.instance(), attributeNames);
		dlg.setVisible(true);

		List<Integer> selectedIndices = dlg.getSelectedIndices();
		int[] attributeIndices = new int[selectedIndices.size()];
		for (int i = 0; i < selectedIndices.size(); i++)
			attributeIndices[i] = selectedIndices.get(i);


		if (dlg.getReturnStatus() == AttributesSelectionDialog.RET_OK) {
			try {
				File file = FileChooserUtils.selectFile(
						"Select destination file",
						Settings.getDefault().getLastExportPath(),
						FileChooserUtils.MODE_SAVE);
				if (file == null)
					return;

				Settings.getDefault().setLastExportPath(file.getAbsolutePath());

				TextMatrixViewExporter.exportTable(matrixView, attributeIndices, file);
			}
			catch (IOException ex) {
				AppFrame.instance().setStatusText("There was an error exporting the data: " + ex.getMessage());
			}

			AppFrame.instance().setStatusText("Table exported.");
		}
		else
			AppFrame.instance().setStatusText("Table export cancelled.");
	}

}
