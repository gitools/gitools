package es.imim.bg.ztools.ui.views.table;

import java.awt.Color;

import javax.swing.JPanel;

import es.imim.bg.ztools.model.elements.ElementFacade;
import es.imim.bg.ztools.ui.colormatrix.CellDecoration;
import es.imim.bg.ztools.ui.model.table.ITableDecoratorContext;
import es.imim.bg.ztools.ui.model.table.ScaleCellDecoratorContext;

public class ScaleCellDecorator 
		implements ITableDecorator {

	private static final int defaultWidth = 30;
	private static final int defaultHeight = 30;
	
	private ElementFacade cellFacade;
	
	private ScaleCellDecoratorContext context;
	
	public ScaleCellDecorator(ElementFacade cellFacade) {
		this(cellFacade, new ScaleCellDecoratorContext());
	}
	
	public ScaleCellDecorator(ElementFacade cellFacade, ScaleCellDecoratorContext context) {
		this.cellFacade = cellFacade;
		this.context = context;
	}
	
	@Override
	public void decorate(CellDecoration decoration, Object element) {
		double v = Double.NaN;
		Object value = cellFacade.getValue(
				element, context.getValueIndex());
		
		try { v = ((Double) value).doubleValue(); }
		catch (Exception e1) {
			try { v = ((Integer) value).doubleValue(); }
			catch (Exception e2) { }
		}
		
		Color c = context.getScale().getColor(v);
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
	public JPanel createConfigurationPanel() {
		return null;
	}

	@Override
	public ITableDecoratorContext getContext() {
		return context;
	}

	@Override
	public void setContext(ITableDecoratorContext context) {
		this.context = (ScaleCellDecoratorContext) context;
	}
}
