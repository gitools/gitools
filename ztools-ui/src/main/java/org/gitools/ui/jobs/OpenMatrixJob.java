package org.gitools.ui.jobs;

import java.io.File;

import javax.swing.SwingUtilities;

import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.model.matrix.IMatrix;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.MatrixView;
import org.gitools.model.matrix.adapter.DataMatrixTableContentsAdapter;
import org.gitools.resources.DataResource;
import org.gitools.ui.AppFrame;
import org.gitools.ui.editor.matrix.MatrixEditor;

import edu.upf.bg.progressmonitor.ProgressMonitor;

public class OpenMatrixJob implements Job {

	private File selectedPath;
	private ProgressMonitor monitor;
	
	public OpenMatrixJob(
			File selectedPath, ProgressMonitor monitor) {
		
		this.selectedPath = selectedPath;
		this.monitor = monitor;
	}
	
	@Override
	public void run() {
        AppFrame.instance()
        	.setStatusText("Opening matrix...");
        
		openAnalysisJob(selectedPath, monitor);
	}
	
	private void openAnalysisJob(
			File selectedPath, ProgressMonitor monitor) {
		
		if (selectedPath == null)
			return;
		
		try {
			monitor.begin("Loading matrix ...", 1);
			
			final DataResource res = new DataResource(selectedPath);
			final DoubleMatrix dm = res.load(monitor);
			
			final IMatrix matrix = new DataMatrixTableContentsAdapter(dm, null);
			
			final IMatrixView matrixView = new MatrixView(matrix);
			
			final MatrixFigure figure = new MatrixFigure(matrixView);
			figure.setShowGrid(false);
			
			final MatrixEditor editor = new MatrixEditor(figure);
			
			editor.setName(selectedPath.getName());
			
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					AppFrame.instance().getWorkspace().addEditor(editor);
					AppFrame.instance().refresh();
				}
			});
			
			monitor.end();
			
			AppFrame.instance().setStatusText("Done.");
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					AppFrame.instance()
						.setStatusText("Error loading matrix.");
				}
			});
		}
	}
}
