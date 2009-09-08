package org.gitools.ui.actions.file;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;

import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.utils.Options;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class ExportMatrixFigurePictureAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportMatrixFigurePictureAction() {
		super("Export matrix figure as an image ...");
		
		setDesc("Export a matrix figure in an image format");
		setMnemonic(KeyEvent.VK_I);
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof MatrixFigure;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		IMatrixView matrixView = getMatrixView();
		if (matrixView == null)
			return;
		
		try {
			File file = getSelectedFile(
					"Select destination file",
					Options.instance().getLastExportPath());
			if (file == null)
				return;
			
			Options.instance().setLastExportPath(file.getAbsolutePath());
			
			Document doc = new Document(PageSize.A4, 10, 10, 10, 10);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(file));
			doc.open();
			doc.addCreator("GiTools");
			
			PdfContentByte cb = writer.getDirectContent();
            
            // Create the graphics as shapes
            cb.saveState();
            Graphics2D g2 = cb.createGraphicsShapes(500, 500);
            // Print the table to the graphics
            Shape oldClip = g2.getClip();
            g2.clipRect(0, 0, 500, 500);
            ////table.print(g2);
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
		}
		catch (Exception ex) {
			AppFrame.instance().setStatusText("There was an error exporting the data: " + ex.getMessage());
		}
		
		AppFrame.instance().setStatusText("Table exported.");
	}

}
