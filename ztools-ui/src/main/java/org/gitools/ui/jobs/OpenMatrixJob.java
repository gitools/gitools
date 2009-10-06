package org.gitools.ui.jobs;

import java.io.File;

import javax.swing.SwingUtilities;

import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.MatrixView;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;
import org.gitools.ui.editor.matrix.MatrixEditor;
import org.gitools.ui.platform.AppFrame;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class OpenMatrixJob implements Job {

	private File selectedPath;
	private IProgressMonitor monitor;
	
	public OpenMatrixJob(
			File selectedPath, IProgressMonitor monitor) {
		
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
			File selectedPath, IProgressMonitor monitor) {
		
		if (selectedPath == null)
			return;
		
		try {
			monitor.begin("Loading matrix ...", 1);
			
			final IResource res = new FileResource(selectedPath);
			final DoubleMatrixTextPersistence pers = 
				new DoubleMatrixTextPersistence();
			final DoubleMatrix matrix = pers.read(res, monitor);
					
			final IMatrixView matrixView = new MatrixView(matrix);
			
			final MatrixFigure figure = new MatrixFigure(matrixView);
			figure.setShowGrid(false);
			
			final MatrixEditor editor = new MatrixEditor(figure);
			
			editor.setName(selectedPath.getName());
			
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					AppFrame.instance().getEditorsPanel().addEditor(editor);
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
