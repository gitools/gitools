package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.jobs.OpenMatrixJob;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import javax.swing.SwingUtilities;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.ui.dialog.progress.JobRunnable;
import org.gitools.ui.dialog.progress.JobThread;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.utils.FileChooserUtils;

public class OpenHeatmapAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public OpenHeatmapAction() {
		super("Heatmap ...");
		setDesc("Open a heatmap from the file system");
		setSmallIconFromResource(IconNames.openMatrix16);
		setLargeIconFromResource(IconNames.openMatrix24);
		setMnemonic(KeyEvent.VK_M);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final File file = FileChooserUtils.selectFile(
				"Select file", FileChooserUtils.MODE_OPEN);
		
		if (file == null)
			return;

		Settings.getDefault().setLastPath(file.getParent());
		Settings.getDefault().save();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Loading matrix ...", 1);
					monitor.info("File: " + file.getName());

					final DoubleMatrixTextPersistence pers = new DoubleMatrixTextPersistence();
					final DoubleMatrix matrix = pers.read(file, monitor);

					final IMatrixView matrixView = new MatrixView(matrix);

					final Heatmap figure = new Heatmap(matrixView);
					figure.setShowGrid(false);

					final HeatmapEditor editor = new HeatmapEditor(figure);

					editor.setName(file.getName());

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							AppFrame.instance().getEditorsPanel().addEditor(editor);
							AppFrame.instance().refresh();
						}
					});

					monitor.end();
				}
				catch (Exception ex) {
					monitor.exception(ex);
				}
			}
		});

		AppFrame.instance().setStatusText("Done.");
	}
}
