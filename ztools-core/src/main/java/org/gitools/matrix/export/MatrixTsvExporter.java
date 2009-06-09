package org.gitools.matrix.export;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.utils.CompressedFile;

public class MatrixTsvExporter {

	public static void exportProperty(IMatrixView matrixView, int propIndex, File file) throws IOException {
		PrintWriter pw = new PrintWriter(CompressedFile.openWriter(file));
		
		int rowCount = matrixView.getRowCount();
		int colCount = matrixView.getColumnCount();
		
		//header

		for (int c = 0; c < colCount; c++)
			pw.print("\t" + matrixView.getColumn(c).toString());
		pw.println();
		
		// body
		
		for (int r = 0; r < rowCount; r++) {
			pw.print(matrixView.getRow(r).toString());
			for (int c = 0; c < colCount; c++)
				pw.print("\t" + matrixView.getCellValue(r, c, propIndex).toString());
			pw.println();
		}
		
		pw.close();
	}
	
	public static void exportProperties(IMatrixView matrixView, int[] propIndices, File file) throws IOException {
		PrintWriter pw = new PrintWriter(CompressedFile.openWriter(file));
		
		IElementAdapter adapter = matrixView.getCellAdapter();
		
		final int rowCount = matrixView.getRowCount();
		final int colCount = matrixView.getColumnCount();
		final int propCount = propIndices.length;
		
		//header

		pw.print("row\tcolumn");
		for (int p = 0; p < propCount; p++)
			pw.print("\t" + adapter.getProperty(p).getId());
		pw.println();
		
		// body
		
		for (int i = 0; i < rowCount * colCount; i++) {
			final int r = i / colCount;
			final int c = i % colCount;
			
			pw.print(matrixView.getRow(r).toString());
			pw.print("\t" + matrixView.getColumn(c).toString());
			for (int p = 0; p < propCount; p++)
				pw.print("\t" + matrixView.getCellValue(r, c, p).toString());
			pw.println();
		}
		
		pw.close();
	}
}
