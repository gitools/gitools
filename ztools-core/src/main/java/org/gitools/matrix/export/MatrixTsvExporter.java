package org.gitools.matrix.export;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.gitools.model.matrix.IMatrixView;
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
}
