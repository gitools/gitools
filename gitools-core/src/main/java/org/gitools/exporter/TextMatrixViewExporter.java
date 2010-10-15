package org.gitools.exporter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import edu.upf.bg.fileutils.IOUtils;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.datafilters.ValueTranslator;

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
		
		IElementAttribute attr = matrixView.getContents().getCellAttributes().get(propIndex);

		ValueTranslator vt = null;
		
		if (Double.class.equals(attr.getValueClass()))
			vt = new DoubleTranslator();
		else
			vt = new ValueTranslator() {
				@Override public Object stringToValue(String str) { return null; }
				@Override public String valueToString(Object value) {
					return value.toString(); } };

		for (int r = 0; r < rowCount; r++) {
			pw.print(matrixView.getRowLabel(r).toString());
			for (int c = 0; c < colCount; c++) {
				Object value = matrixView.getCellValue(r, c, propIndex);
				String valueString = vt.valueToString(value);
				pw.print("\t" + valueString);
			}
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

		ValueTranslator[] vt = new ValueTranslator[propCount];

		// header

		pw.print("column\trow");
		for (int i = 0; i < propIndices.length; i++) {
			IElementAttribute attr = attributes.get(propIndices[i]);

			if (Double.class.equals(attr.getValueClass()))
				vt[i] = new DoubleTranslator();
			else
				vt[i] = new ValueTranslator() {
					@Override public Object stringToValue(String str) { return null; }
					@Override public String valueToString(Object value) {
						return value.toString(); } };

			pw.print("\t" + attr.getId());
		}
		pw.println();
		
		// body

		for (int i = 0; i < rowCount * colCount; i++) {
			final int r = i / colCount;
			final int c = i % colCount;

			if (matrixView.getCell(r, c) != null) {
				pw.print(matrixView.getColumnLabel(c));
				pw.print("\t" + matrixView.getRowLabel(r));
				for (int p = 0; p < propCount; p++) {
					Object value = matrixView.getCellValue(r, c, propIndices[p]);
					String valueString = vt[p].valueToString(value);
					pw.print("\t" + valueString);
				}
				pw.println();
			}
		}
		
		pw.close();
	}
}
