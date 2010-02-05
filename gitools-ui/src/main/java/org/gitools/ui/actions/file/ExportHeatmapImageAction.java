package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.gitools.heatmap.drawer.HeatmapDrawer;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.progress.JobRunnable;
import org.gitools.ui.dialog.progress.JobThread;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.utils.FileChooserUtils;

public class ExportHeatmapImageAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportHeatmapImageAction() {
		super("Export heatmap as an image ...");
		
		setDesc("Export the heatmap as an image file");
		setMnemonic(KeyEvent.VK_I);
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		AbstractEditor editor = ActionUtils.getSelectedEditor();
		if (editor == null)
			return;

		final Object model = editor.getModel();
		if (!(model instanceof Heatmap))
			return;

		final File file = FileChooserUtils.selectImageFile(
					"Select destination file",
					Settings.getDefault().getLastExportPath(),
					FileChooserUtils.MODE_SAVE);

		if (file == null)
			return;

		Settings.getDefault().setLastExportPath(file.getAbsolutePath());

		final String formatExtension = FileChooserUtils.getExtension(file);

		if (!FileChooserUtils.isImageExtension(formatExtension)) {
			AppFrame.instance().setStatusText("Unsupported export format: " + formatExtension);
			return;
		}

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Exporting to image ...", 1);
					monitor.info("File: " + file.getName());

					Heatmap hm = (Heatmap) model;

					HeatmapDrawer drawer = new HeatmapDrawer(hm);
					drawer.setPictureMode(true);

					Dimension size = drawer.getSize();

					int type = formatExtension.equals(FileChooserUtils.png) ?
						BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

					BufferedImage bi = new BufferedImage(size.width, size.height, type);
					Graphics2D g = bi.createGraphics();
					drawer.draw(g, new Rectangle(new Point(), size), new Rectangle(new Point(), size));

					ImageIO.write(bi, formatExtension, file);

					monitor.end();
				}
				catch (Exception ex) {
					monitor.exception(ex);
				}
			}
		});

		AppFrame.instance().setStatusText("Image created.");
	}

	/*Document doc = new Document(PageSize.A4, 10, 10, 10, 10);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(file));
			doc.open();
			doc.addCreator("GiTools");

			PdfContentByte cb = writer.getDirectContent();

			Dimension size = drawer.getSize();

            // Create the graphics as shapes
            cb.saveState();
            Graphics2D g2 = cb.createGraphicsShapes(size.width, size.height);

			Shape oldClip = g2.getClip();
            g2.clipRect(0, 0, size.width, size.height);
            drawer.draw(g2, new Rectangle(new Point(), size), new Rectangle(new Point(), size));
            g2.setClip(oldClip);

            g2.dispose();
            cb.restoreState();

            doc.newPage();

            // Create the graphics with pdf fonts
            cb.saveState();
            g2 = cb.createGraphics(500, 500);

            // Print the table to the graphics
            oldClip = g2.getClip();
            g2.clipRect(0, 0, 500, 500);
            ////table.print(g2);
            g2.setClip(oldClip);

            g2.dispose();
            cb.restoreState();

			doc.close();
*/
}
