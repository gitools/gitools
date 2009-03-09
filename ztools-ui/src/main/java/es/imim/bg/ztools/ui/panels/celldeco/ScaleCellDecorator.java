package es.imim.bg.ztools.ui.panels.celldeco;

import java.awt.Color;
import java.awt.Component;

import es.imim.bg.colorscale.LogColorScale;
import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.ui.model.celldeco.ITableDecorator;
import es.imim.bg.ztools.ui.model.celldeco.ITableDecoratorContext;
import es.imim.bg.ztools.ui.model.celldeco.ScaleCellDecoratorContext;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.panels.table.CellDecoration;
import es.imim.bg.ztools.ui.utils.TableUtils;

public class ScaleCellDecorator 
		implements ITableDecorator {

	private static final int defaultWidth = 30;
	private static final int defaultHeight = 30;
	
	private ITable table;
	
	private ScaleCellDecoratorContext context;
	
	public ScaleCellDecorator(ITable table) {
		this(table, new ScaleCellDecoratorContext());
		context.setValueIndex(table.getSelectedPropertyIndex());
	}
	
	public ScaleCellDecorator(ITable table, ScaleCellDecoratorContext context) {
		this.table = table;
		this.context = context;
	}
	
	@Override
	public void decorate(CellDecoration decoration, Object element) {
		if (element == null) {
			decoration.setBgColor(Color.WHITE);
			decoration.setToolTip("Void cell");
			return;
		}
		
		IElementAdapter cellAdapter = table.getCellAdapter();
		
		Object value = cellAdapter.getValue(
				element, context.getValueIndex());
		
		double v = TableUtils.doubleValue(value);
		
		double cutoff = context.getCutoff();
		
		boolean isSig = v <= cutoff;
		
		LogColorScale scale = context.getScale();
		
		if (context.isUseCorrectedScale()) {
			int corrIndex = context.getCorrectedValueIndex();
			
			Object corrValue = corrIndex >= 0 ?
					cellAdapter.getValue(element, corrIndex) : 0.0;
					
			double cv = TableUtils.doubleValue(corrValue);
			
			isSig = cv <= cutoff;
		}
		
		Color c = isSig ? scale.getColor(v) : scale.getNonSignificantColor();
		
		decoration.setBgColor(c);
		
		decoration.setToolTip(value.toString());
	}
	
	@Override
	public int getMinimumHeight() {
		return 0;
	}
	
	@Override
	public int getMaximumWidth() {
		return Integer.MAX_VALUE;
	}
	
	public int getPreferredWidth() {
		return defaultWidth;
	}

	@Override
	public int getMinimumWidth() {
		return 0;
	}
	
	@Override
	public int getMaximumHeight() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getPreferredHeight() {
		return defaultHeight;
	}

	@Override
	public Component createConfigurationComponent() {
		return new ScaleCellDecoratorConfigPanel(table);
	}

	@Override
	public ITableDecoratorContext getContext() {
		return context;
	}

	@Override
	public void setContext(ITableDecoratorContext context) {
		this.context = (ScaleCellDecoratorContext) context;
	}
	
	@Override
	public String toString() {
		return "Color scale";
	}
}
