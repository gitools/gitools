package es.imim.bg.ztools.ui.utils;

import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.ui.model.table.ITable;

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
	
	public static int correctedValueIndex(ITable table, IElementProperty prop) {
		IElementAdapter cellFacade = table.getCellsFacade();
		int numProps = cellFacade.getPropertyCount();
		
		String id = "corrected-" + prop.getId();
		
		for (int i = 0; i < numProps; i++)
			if (id.equals(cellFacade.getProperty(i).getId()))
				return i;
		
		return -1;
	}
}
