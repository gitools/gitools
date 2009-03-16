package es.imim.bg.ztools.table;

import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.table.element.IElementProperty;

public class TableUtils {

	public static double doubleValue(Object value) {
		double v = Double.NaN;
		try { v = ((Double) value).doubleValue(); }
		catch (Exception e1) {
			try { v = ((Integer) value).doubleValue(); }
			catch (Exception e2) { }
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
