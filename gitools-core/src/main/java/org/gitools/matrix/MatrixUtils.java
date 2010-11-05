package org.gitools.matrix;

import edu.upf.bg.colorscale.IColorScale;
import edu.upf.bg.colorscale.impl.BinaryColorScale;
import edu.upf.bg.colorscale.impl.LinearTwoSidedColorScale;
import edu.upf.bg.colorscale.impl.PValueColorScale;
import edu.upf.bg.cutoffcmp.CutoffCmp;
import java.util.ArrayList;
import java.util.List;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.ModuleMap;

public class MatrixUtils {

	public static interface DoubleCast {
		Double getDoubleValue(Object value);
	}

	public static int intValue(Object value) {
		int v = 0;
		if (value != null) {
			try { v = ((Integer) value).intValue(); }
			catch (Exception e1) {
				try { v = ((Double) value).intValue(); }
				catch (Exception e2) {
					try { v = Integer.parseInt((String) value); }
					catch (Exception e3) { }
				}
			}
		}
		return v;
	}

	@Deprecated // Better use createDoubleCast() when accessing multiple values
	public static double doubleValue(Object value) {
		if (value == null)
			return Double.NaN; //TODO null;
		
		double v = Double.NaN;
		
		try { v = ((Double) value).doubleValue(); }
		catch (Exception e1) {
			try { v = ((Integer) value).doubleValue(); }
			catch (Exception e2) {
				/*try { v = Double.parseDouble((String) value); }
				catch (Exception e3) { }*/
			}
		}
		
		return v;
	}

	public static DoubleCast createDoubleCast(Class cls) {
		if (cls.equals(Double.class) || cls.equals(double.class))
			return new DoubleCast() {
				@Override public Double getDoubleValue(Object value) {
					return value != null ? ((Double) value).doubleValue() : null; }
				};
		else if (cls.equals(Float.class) || cls.equals(float.class))
			return new DoubleCast() {
				@Override public Double getDoubleValue(Object value) {
					return value != null ? ((Float) value).doubleValue() : null; }
				};
		else if (cls.equals(Integer.class) || cls.equals(int.class))
			return new DoubleCast() {
				@Override public Double getDoubleValue(Object value) {
					return value != null ? ((Integer) value).doubleValue() : null; }
				};
		else if (cls.equals(Long.class) || cls.equals(long.class))
			return new DoubleCast() {
				@Override public Double getDoubleValue(Object value) {
					return value != null ? ((Long) value).doubleValue() : null; }
				};
		
		return new DoubleCast() {
			@Override public Double getDoubleValue(Object value) {
				return value != null ? Double.NaN : null; }
			};
	}

	public static IColorScale inferScale(IMatrix data, int valueIndex) {
		final int numRows = data.getRowCount();
		final int numCols = data.getColumnCount();

		boolean binary = true;
		double min = 0;
		double max = 0;

		final MatrixUtils.DoubleCast valueCast = MatrixUtils.createDoubleCast(
				data.getCellAdapter().getProperty(valueIndex).getValueClass());

		for (int ri = 0; ri < numRows; ri++) {
			for (int ci = 0; ci < numCols; ci++) {
				Double v = valueCast.getDoubleValue(data.getCellValue(ri, ci, valueIndex));
				if (v != null) {
					min = v < min ? v : min;
					max = v > max ? v : max;
					binary &= (Double.isNaN(v) || v == 0 || v == 1);
				}
			}
		}

		IColorScale scale = null;

		if (binary) {
			BinaryColorScale bscale = new BinaryColorScale();
			bscale.getCutoff().setValue(1.0);
			bscale.setCutoffCmp(CutoffCmp.EQ);
			scale = bscale;
		} else {
			if (min >= 0 && max <= 1) {
				scale = new PValueColorScale();
			} else {
				LinearTwoSidedColorScale lscale = new LinearTwoSidedColorScale();
				lscale.getMin().setValue(min);
				lscale.getMax().setValue(max);
				// TODO infer middle point value
				scale = lscale;
			}
		}

		return scale;
	}

	public static int correctedValueIndex(IElementAdapter adapter, IElementAttribute prop) {
		int numProps = adapter.getPropertyCount();
		
		String id = "corrected-" + prop.getId();
		
		for (int i = 0; i < numProps; i++)
			if (id.equals(adapter.getProperty(i).getId()))
				return i;
		
		return -1;
	}

	public static BaseMatrix moduleMapToMatrix(ModuleMap mmap) {
		DoubleBinaryMatrix matrix = new DoubleBinaryMatrix();
		String[] columns = mmap.getModuleNames();
		String[] rows = mmap.getItemNames();
		matrix.setColumns(columns);
		matrix.setRows(rows);
		matrix.makeCells(rows.length, columns.length);
		for (int col = 0; col < mmap.getModuleCount(); col++)
			for (int row : mmap.getItemIndices(col))
				matrix.setCellValue(row, col, 0, 1.0);
		return matrix;
	}

	public static ModuleMap matrixToModuleMap(IMatrix matrix) {
		String[] itemNames = new String[matrix.getRowCount()];
		for (int i = 0; i < matrix.getRowCount(); i++)
			itemNames[i] = matrix.getRowLabel(i);

		String[] modNames = new String[matrix.getColumnCount()];
		for (int i = 0; i < matrix.getColumnCount(); i++)
			modNames[i] = matrix.getColumnLabel(i);

		ModuleMap map = new ModuleMap();
		map.setItemNames(itemNames);
		map.setModuleNames(modNames);

		int[][] mapIndices = new int[matrix.getColumnCount()][];
		for (int col = 0; col < matrix.getColumnCount(); col++) {
			List<Integer> indexList = new ArrayList<Integer>();
			for (int row = 0; row < matrix.getRowCount(); row++) {
				double value = MatrixUtils.doubleValue(matrix.getCellValue(row, col, 0));
				if (value == 1.0)
					indexList.add(row);
			}
			int[] indexArray = new int[indexList.size()];
			for (int i = 0; i < indexList.size(); i++)
				indexArray[i] = indexList.get(i);
			mapIndices[col] = indexArray;
		}

		map.setAllItemIndices(mapIndices);

		return map;
	}
}
