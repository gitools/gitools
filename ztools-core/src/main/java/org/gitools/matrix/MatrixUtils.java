package org.gitools.matrix;

import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.IElementProperty;

public class MatrixUtils {

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
	
	public static int correctedValueIndex(IElementAdapter adapter, IElementProperty prop) {
		int numProps = adapter.getPropertyCount();
		
		String id = "corrected-" + prop.getId();
		
		for (int i = 0; i < numProps; i++)
			if (id.equals(adapter.getProperty(i).getId()))
				return i;
		
		return -1;
	}
}
