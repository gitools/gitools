package org.gitools.ui.actions.file;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.editor.matrix.MatrixEditor;
import org.gitools.ui.panels.matrix.MatrixPanel;
import org.gitools.ui.utils.Options;

import org.gitools.matrix.export.MatrixTsvExporter;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.element.IElementProperty;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class ExportTableToPdfAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportTableToPdfAction() {
		super("Export matrix to pdf");
		
		setDesc("Export the matrix to a pdf");
		setMnemonic(KeyEvent.VK_A);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		IMatrixView matrixView = getTable();
		if (matrixView == null)
			return;
		
		try {
			File file = getSelectedFile();
			if (file == null)
				return;
			
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
	

	private File getSelectedFile() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastExportPath());
		
		fileChooser.setDialogTitle("Select the file where the names will be saved");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Options.instance().setLastExportPath(file.getAbsolutePath());
			return file;
		}
	
		return null;
	}

}
