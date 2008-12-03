package es.imim.bg.ztools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.model.ITableModel;
import es.imim.bg.ztools.ui.utils.Options;

public class ExportNames extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportNames() {
		super("Export names");
		
		setDesc("Export row or column names");
		setMnemonic(KeyEvent.VK_N);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		final String visibleRows = "Visible row names";
		final String visibleCols = "Visible column names";
		final String hiddenRows = "Hidden row names";
		final String hiddenCols = "Hidden column names";

		ITableModel tableModel = getTableModel();
		if (tableModel == null)
			return;
		
		String[] possibilities = { 
				visibleRows, visibleCols, hiddenRows, hiddenCols };

		final String selected = (String) JOptionPane.showInputDialog(AppFrame.instance(),
				"What do tou want to export ?", "Export names",
				JOptionPane.QUESTION_MESSAGE, null, possibilities,
				"Visible row names");

		if (selected == null || selected.isEmpty())
			return;

		try {
			File file = getSelectedFile();
			if (file == null)
				return;
			
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			
			if (visibleRows.equals(selected)) {
				for (int i = 0; i < tableModel.getRowCount(); i++)
					pw.println(tableModel.getRowName(i));
			}
			else if (visibleCols.equals(selected)) {
				for (int i = 0; i < tableModel.getColumnCount(); i++)
					pw.println(tableModel.getColumnName(i));
			} 
			else {
				AppFrame.instance().setStatusText("Unimplemented option.");
				return;
			}
			
			pw.close();
		}
		catch (IOException ex) {
			AppFrame.instance().setStatusText("There was an error exporting names: " + ex.getMessage());
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
