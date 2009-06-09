package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;

import org.gitools.matrix.export.MatrixTsvExporter;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.element.IElementProperty;
import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.utils.Options;

public class ExportTableAllParametersAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportTableAllParametersAction() {
		super("Export all table parameters");
		
		setDesc("Export a data table with all parameters");
		setMnemonic(KeyEvent.VK_A);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		IMatrixView matrixView = getTable();
		if (matrixView == null)
			return;
		
		final List<IElementProperty> properties = matrixView.getCellAdapter().getProperties();
		final int[] propIndices = new int[properties.size()];
		for (int i = 0; i < properties.size(); i++)
			propIndices[i] = i;
		
		try {
			File file = getSelectedFile();
			if (file == null)
				return;
			
			MatrixTsvExporter.exportProperties(matrixView, propIndices, file);
		}
		catch (IOException ex) {
			AppFrame.instance().setStatusText("There was an error exporting the data: " + ex.getMessage());
		}
		
		AppFrame.instance().setStatusText("Table exported.");
	}
	

	private File getSelectedFile() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastExportPath());
		
		fileChooser.setDialogTitle("Select the file where the names will be saved");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Options.instance().setLastExportPath(file.getAbsolutePath());
			return file;
		}
	
		return null;
	}

}
