package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.utils.FileChooserUtils;

public class OpenHeatmapAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	private static class FF extends FileFilter {
		private String description;
		private String mime;

		public FF(String description, String mime) {
			this.description = description;
			this.mime = mime;
		}

		@Override
		public boolean accept(File f) {
			return true;
		}

		@Override
		public String getDescription() {
			return description;
		}

		public String getMime() {
			return mime;
		}
	}

	public OpenHeatmapAction() {
		super("Heatmap ...");
		setDesc("Open a heatmap from a file");
		setSmallIconFromResource(IconNames.openMatrix16);
		setLargeIconFromResource(IconNames.openMatrix24);
		setMnemonic(KeyEvent.VK_M);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FileFilter[] filters = new FileFilter[] {
			new FF("Results file", MimeTypes.OBJECT_MATRIX),
			new FF("Matrix file", MimeTypes.DOUBLE_MATRIX),
			new FF("Binary matrix file", MimeTypes.DOUBLE_BINARY_MATRIX) /*,
			new FF("Element lists file", MimeTypes.ELEMENT_LISTS)*/
		};

		final Object[] ret = FileChooserUtils.selectFile(
				"Select file", FileChooserUtils.MODE_OPEN, filters);

		final File file = (File) ret[0];
		
		if (file == null)
			return;

		final FF ff = (FF) ret[1];

		Settings.getDefault().setLastPath(file.getParent());
		Settings.getDefault().save();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Loading ...", 1);
					monitor.info("File: " + file.getName());

					final IMatrix matrix = (IMatrix) PersistenceManager.getDefault()
							.load(file, ff.getMime(), monitor);

					final IMatrixView matrixView = new MatrixView(matrix);

					Heatmap figure = HeatmapUtil.createFromMatrixView(matrixView);

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
