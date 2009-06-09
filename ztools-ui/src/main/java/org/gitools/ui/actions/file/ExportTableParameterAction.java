package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.gitools.matrix.export.MatrixTsvExporter;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.element.IElementProperty;
import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.utils.Options;

public class ExportTableParameterAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportTableParameterAction() {
		super("Export table parameter");
		
		setDesc("Export a data table of a parameter");
		setMnemonic(KeyEvent.VK_P);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		IMatrixView matrixView = getTable();
		if (matrixView == null)
			return;
		
		final List<IElementProperty> properties = matrixView.getCellAdapter().getProperties();
		final String[] propNames = new String[properties.size()];
		for (int i = 0; i < properties.size(); i++)
			propNames[i] = properties.get(i).getName();

		int selectedPropIndex = matrixView.getSelectedPropertyIndex();
		selectedPropIndex = selectedPropIndex >= 0 ? selectedPropIndex : 0;
		selectedPropIndex = selectedPropIndex < properties.size() ? selectedPropIndex : 0;
		
		final String selected = (String) JOptionPane.showInputDialog(AppFrame.instance(),
				"What do you want to export ?", "Export table data",
				JOptionPane.QUESTION_MESSAGE, null, propNames,
				propNames[selectedPropIndex]);

		if (selected == null || selected.isEmpty())
			return;
		
		int propIndex = 0;
		for (int j = 0; j < propNames.length; j++)
			if (propNames[j].equals(selected))
				propIndex = j;

		try {
			File file = getSelectedFile();
			if (file == null)
				return;
			
			MatrixTsvExporter.exportProperty(matrixView, propIndex, file);
		}
		catch (IOException ex) {
			AppFrame.instance().setStatusText("There was an error exporting the data: " + ex.getMessage());
		}
		
		AppFrame.instance().setStatusText(selected + " exported.");
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
