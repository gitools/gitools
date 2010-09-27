package org.gitools.matrix;

import edu.upf.bg.colorscale.IColorScale;
import edu.upf.bg.colorscale.impl.BinaryColorScale;
import edu.upf.bg.colorscale.impl.LinearTwoSidedColorScale;
import edu.upf.bg.colorscale.impl.PValueColorScale;
import edu.upf.bg.cutoffcmp.CutoffCmp;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;

public class MatrixUtils {

	public static interface DoubleCast {
		double getDoubleValue(Object value);
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
		double v = Double.NaN;
		if (value != null) {
			try { v = ((Double) value).doubleValue(); }
			catch (Exception e1) {
				try { v = ((Integer) value).doubleValue(); }
				catch (Exception e2) {
					/*try { v = Double.parseDouble((String) value); }
					catch (Exception e3) { }*/
				}
			}
		}
		return v;
	}

	public static DoubleCast createDoubleCast(Class cls) {
		if (cls.equals(Double.class) || cls.equals(double.class))
			return new DoubleCast() {
				@Override public double getDoubleValue(Object value) {
					return ((Double) value).doubleValue(); }
				};
		else if (cls.equals(Float.class) || cls.equals(float.class))
			return new DoubleCast() {
				@Override public double getDoubleValue(Object value) {
					return ((Float) value).doubleValue(); }
				};
		else if (cls.equals(Integer.class) || cls.equals(int.class))
			return new DoubleCast() {
				@Override public double getDoubleValue(Object value) {
					return ((Integer) value).doubleValue(); }
				};
		else if (cls.equals(Long.class) || cls.equals(long.class))
			return new DoubleCast() {
				@Override public double getDoubleValue(Object value) {
					return ((Long) value).doubleValue(); }
				};
		
		return new DoubleCast() {
			@Override public double getDoubleValue(Object value) {
				return Double.NaN; }
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
				double v = valueCast.getDoubleValue(data.getCellValue(ri, ci, valueIndex));
				min = v < min ? v : min;
				max = v > max ? v : max;
				binary &= (Double.isNaN(v) || v == 0 || v == 1);
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
}
