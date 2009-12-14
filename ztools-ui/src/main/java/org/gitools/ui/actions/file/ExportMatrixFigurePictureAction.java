package org.gitools.ui.actions.file;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;

import org.gitools.model.figure.heatmap.Heatmap;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.gitools.heatmap.HeatmapDrawer;
import org.gitools.ui.editor.AbstractEditor;

public class ExportMatrixFigurePictureAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportMatrixFigurePictureAction() {
		super("Export matrix figure as an image ...");
		
		setDesc("Export a matrix figure in an image format");
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

		try {
			File file = getSelectedFile(
					"Select destination file",
					Settings.getDefault().getLastExportPath());
			if (file == null)
				return;
			
			Settings.getDefault().setLastExportPath(file.getAbsolutePath());

			Dimension size = drawer.getSize();

			BufferedImage bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bi.createGraphics();
			drawer.draw(g, new Rectangle(new Point(), size), new Rectangle(new Point(), size));
			ImageIO.write(bi, "png", file);

			AppFrame.instance().setStatusText("Table exported.");
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
