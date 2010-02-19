package org.gitools.ui._DEPRECATED.jobs;

import java.io.File;

import javax.swing.SwingUtilities;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;

import edu.upf.bg.progressmonitor.IProgressMonitor;

@Deprecated
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
			
			final DoubleMatrixTextPersistence pers = new DoubleMatrixTextPersistence();
			final DoubleMatrix matrix = pers.read(selectedPath, monitor);
					
			final IMatrixView matrixView = new MatrixView(matrix);
			
			final Heatmap figure = new Heatmap(matrixView);
			figure.setShowGrid(false);
			
			final HeatmapEditor editor = new HeatmapEditor(figure);
			
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
