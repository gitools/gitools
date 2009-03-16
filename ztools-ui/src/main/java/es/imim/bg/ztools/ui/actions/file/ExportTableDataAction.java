package es.imim.bg.ztools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.model.table.ITableContents;
import es.imim.bg.ztools.ui.utils.Options;

public class ExportTableDataAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportTableDataAction() {
		super("Export table data");
		
		setDesc("Export data from the table");
		setMnemonic(KeyEvent.VK_N);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {


		ITable table = getTable();
		if (table == null)
			return;
		
		Object[] props = table.getCellAdapter().getProperties().toArray();
		String[] possibilities = new String[props.length];
		for (int i = 0; i < props.length; i++) {
			IElementProperty prop = (IElementProperty) props[i];
			possibilities[i] = prop.getName();
		}

		final String selected = (String) JOptionPane.showInputDialog(AppFrame.instance(),
				"What do you want to export ?", "Export table data",
				JOptionPane.QUESTION_MESSAGE, null, possibilities,
				possibilities[0]);

		if (selected == null || selected.isEmpty())
			return;
		
		int propIndex = 0;
		for (int j = 0; j < possibilities.length; j++)
			if (possibilities[j].equals(selected))
				propIndex = j;

		try {
			File file = getSelectedFile();
			if (file == null)
				return;
			
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			
			int rowCount = table.getRowCount();
			int colCount = table.getColumnCount();
			
			//header row
			String line = "\t";
			for (int c = 0; c < colCount; c++) {
				line += table.getColumn(c).toString();
				if (c != colCount-1)
					line += "\t";
			}
			pw.println(line);
			
			for (int r = 0; r < rowCount; r++) {
				for (int c = 0; c < colCount; c++) {
					if (c == 0)
						line = table.getRow(r).toString() + "\t";
					line += table.getCellValue(r, c, propIndex).toString();
					if (c != colCount-1)
						line += "\t";
				}
				pw.println(line);
			}
			
			pw.close();
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
