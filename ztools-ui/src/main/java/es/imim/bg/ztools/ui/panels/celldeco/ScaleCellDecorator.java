package es.imim.bg.ztools.ui.panels.celldeco;

import java.awt.Color;
import java.awt.Component;

import es.imim.bg.colorscale.LogColorScale;
import es.imim.bg.ztools.model.elements.ElementFacade;
import es.imim.bg.ztools.ui.model.celldeco.ITableDecoratorContext;
import es.imim.bg.ztools.ui.model.celldeco.ScaleCellDecoratorContext;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.panels.table.CellDecoration;

public class ScaleCellDecorator 
		implements ITableDecorator {

	private static final int defaultWidth = 30;
	private static final int defaultHeight = 30;
	
	private ITable table;
	
	private ScaleCellDecoratorContext context;
	
	public ScaleCellDecorator(ITable table) {
		this(table, new ScaleCellDecoratorContext());
	}
	
	public ScaleCellDecorator(ITable table, ScaleCellDecoratorContext context) {
		this.table = table;
		this.context = context;
	}
	
	@Override
	public void decorate(CellDecoration decoration, Object element) {
		ElementFacade cellFacade = table.getCellsFacade();
		
		Object value = cellFacade.getValue(
				element, context.getValueIndex());
		
		double v = getDoubleValue(value);
		
		double cutoff = context.getCutoff();
		
		LogColorScale scale = context.getScale();
		
		if (context.isUseCorrectedScale()) {
			int corrIndex = context.getCorrectedValueIndex();
			
			Object corrValue = corrIndex >= 0 ?
					cellFacade.getValue(element, corrIndex) : 0.0;
					
			cutoff = getDoubleValue(corrValue);
		}
		
		scale.setSigLevel(cutoff);
		Color c = scale.getColor(v);
		
		decoration.setBgColor(c);
		
		decoration.setToolTip(value.toString());
	}

	private double getDoubleValue(Object value) {
		double v = Double.NaN;
		try { v = ((Double) value).doubleValue(); }
		catch (Exception e1) {
			try { v = ((Integer) value).doubleValue(); }
			catch (Exception e2) { }
		}
		return v;
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
