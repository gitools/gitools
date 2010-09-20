package org.gitools.matrix;

import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;

public class MatrixUtils {

	public static interface DoubleCast {
		double getDoubleValue(Object value);
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
	
	public static int correctedValueIndex(IElementAdapter adapter, IElementAttribute prop) {
		int numProps = adapter.getPropertyCount();
		
		String id = "corrected-" + prop.getId();
		
		for (int i = 0; i < numProps; i++)
			if (id.equals(adapter.getProperty(i).getId()))
				return i;
		
		return -1;
	}
}
