package org.gitools.exporter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.utils.IOUtils;

public class TextMatrixViewExporter {

	public static void exportMatrix(IMatrixView matrixView, int propIndex, File file) throws IOException {
		PrintWriter pw = new PrintWriter(IOUtils.openWriter(file));
		
		int rowCount = matrixView.getRowCount();
		int colCount = matrixView.getColumnCount();
		
		//header

		for (int c = 0; c < colCount; c++)
			pw.print("\t" + matrixView.getColumnLabel(c));
		pw.println();
		
		// body
		
		for (int r = 0; r < rowCount; r++) {
			pw.print(matrixView.getRowLabel(r).toString());
			for (int c = 0; c < colCount; c++)
				pw.print("\t" + matrixView.getCellValue(r, c, propIndex).toString());
			pw.println();
		}
		
		pw.close();
	}
	
	public static void exportTable(IMatrixView matrixView, int[] propIndices, File file) throws IOException {
		PrintWriter pw = new PrintWriter(IOUtils.openWriter(file));
		
		List<IElementAttribute> attributes = matrixView.getCellAttributes();
		
		final int rowCount = matrixView.getRowCount();
		final int colCount = matrixView.getColumnCount();
		
		final int propCount = propIndices.length;
		
		// header

		pw.print("row\tcolumn");
		for (int i = 0; i < propIndices.length; i++) {
			IElementAttribute attr = attributes.get(propIndices[i]);
			pw.print("\t" + attr.getId());
		}
		pw.println();
		
		// body
		
		for (int i = 0; i < rowCount * colCount; i++) {
			final int r = i / colCount;
			final int c = i % colCount;
			
			pw.print(matrixView.getRowLabel(r));
			pw.print("\t" + matrixView.getColumnLabel(c));
			for (int p = 0; p < propCount; p++)
				pw.print("\t" + matrixView.getCellValue(r, c, propIndices[p]).toString());
			pw.println();
		}
		
		pw.close();
	}
}
