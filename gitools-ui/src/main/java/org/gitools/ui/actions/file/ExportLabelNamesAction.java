package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.progress.JobRunnable;
import org.gitools.ui.dialog.progress.JobThread;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;

public class ExportLabelNamesAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportLabelNamesAction() {
		super("Export labels ...");
		
		setDesc("Export row or column labels");
		setMnemonic(KeyEvent.VK_N);
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		final String visibleRows = "Visible row names";
		final String visibleCols = "Visible column names";
		final String hiddenRows = "Hidden row names";
		final String hiddenCols = "Hidden column names";

		final IMatrixView matrixView = ActionUtils.getHeatmapMatrixView();
		if (matrixView == null)
			return;
		
		final IMatrix contents = matrixView.getContents();
		
		String[] possibilities = { 
				visibleRows, visibleCols, hiddenRows, hiddenCols };

		final String selected = (String) JOptionPane.showInputDialog(AppFrame.instance(),
				"What do you want to export ?", "Export names",
				JOptionPane.QUESTION_MESSAGE, null, possibilities,
				"Visible row names");

		if (selected == null || selected.isEmpty())
			return;

		final File file = FileChooserUtils.selectFile(
					"Select destination file",
					Settings.getDefault().getLastExportPath(),
					FileChooserUtils.MODE_SAVE);

		if (file == null)
			return;

		Settings.getDefault().setLastExportPath(file.getParentFile().getAbsolutePath());

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Exporting " + selected.toLowerCase() + " ...", 1);
					monitor.info("File: " + file.getName());

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			
					if (visibleRows.equals(selected)) {
						for (int i = 0; i < matrixView.getRowCount(); i++)
							pw.println(matrixView.getRowLabel(i));
					}
					else if (visibleCols.equals(selected)) {
						for (int i = 0; i < matrixView.getColumnCount(); i++)
							pw.println(matrixView.getColumnLabel(i));
					}
					else if (hiddenRows.equals(selected)) {
						for (int i = 0; i < contents.getRowCount(); i++) {
							if (!inArray(i, matrixView.getVisibleRows()))
									pw.println(contents.getRowLabel(i));
						}
					}
					else if (hiddenCols.equals(selected)) {
						for (int i = 0; i < contents.getColumnCount(); i++) {
							if (!inArray(i, matrixView.getVisibleColumns()))
									pw.println(contents.getColumnLabel(i));
						}
					}

					pw.close();

					monitor.end();
				}
				catch (IOException ex) {
					monitor.exception(ex);
				}
			}
		});
		
		AppFrame.instance().setStatusText(selected + " exported.");
	}

	private boolean inArray(int needle, int[] ary) {
		for (int i = 0; i < ary.length; i++)
			if (needle == ary[i])
				return true;
		return false;
	}
}
