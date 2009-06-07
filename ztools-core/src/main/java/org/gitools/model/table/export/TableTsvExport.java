package org.gitools.model.table.export;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.gitools.model.table.ITable;
import org.gitools.utils.CompressedFile;


public class TableTsvExport {

	public static void exportProperty(ITable table, int propIndex, File file) throws IOException {
		PrintWriter pw = new PrintWriter(CompressedFile.openWriter(file));
		
		int rowCount = table.getRowCount();
		int colCount = table.getColumnCount();
		
		//header

		for (int c = 0; c < colCount; c++)
			pw.print("\t" + table.getColumn(c).toString());
		pw.println();
		
		// body
		
		for (int r = 0; r < rowCount; r++) {
			pw.print(table.getRow(r).toString());
			for (int c = 0; c < colCount; c++)
				pw.print("\t" + table.getCellValue(r, c, propIndex).toString());
			pw.println();
		}
		
		pw.close();
	}
}
