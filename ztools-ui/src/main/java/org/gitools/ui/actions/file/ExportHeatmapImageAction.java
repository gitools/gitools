package org.gitools.ui.actions.file;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.model.figure.heatmap.Heatmap;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.gitools.heatmap.drawer.HeatmapDrawer;
import org.gitools.ui.editor.AbstractEditor;
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

		AbstractEditor editor = getSelectedEditor();
		if (editor == null)
			return;

		Heatmap hm = null;

		Object model = editor.getModel();
		if (!(model instanceof Heatmap))
			return;

		hm = (Heatmap) model;

		HeatmapDrawer drawer = new HeatmapDrawer(hm);
		drawer.setPictureMode(true);

		try {
			File file = FileChooserUtils.getSelectedImageFile(
					"Select destination file",
					Settings.getDefault().getLastExportPath());
			if (file == null)
				return;
			
			Settings.getDefault().setLastExportPath(file.getAbsolutePath());

			String formatExtension = FileChooserUtils.getExtension(file);

			if (FileChooserUtils.isImageExtension(formatExtension)) {
				Dimension size = drawer.getSize();

				int type = formatExtension.equals(FileChooserUtils.png) ?
					BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
				
				BufferedImage bi = new BufferedImage(size.width, size.height, type);
				Graphics2D g = bi.createGraphics();
				drawer.draw(g, new Rectangle(new Point(), size), new Rectangle(new Point(), size));

				ImageIO.write(bi, formatExtension, file);

				AppFrame.instance().setStatusText("Image created.");
			}
			else
				AppFrame.instance().setStatusText("Unsupported export format: " + formatExtension);
		}
		catch (Exception ex) {
			AppFrame.instance().setStatusText("There was an error exporting the data: " + ex.getMessage());
		}
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
