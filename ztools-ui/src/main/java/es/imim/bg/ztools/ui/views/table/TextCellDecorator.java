package es.imim.bg.ztools.ui.views.table;

import javax.swing.JPanel;

import es.imim.bg.ztools.model.elements.ElementFacade;
import es.imim.bg.ztools.ui.colormatrix.CellDecoration;
import es.imim.bg.ztools.ui.model.table.ITableDecoratorContext;
import es.imim.bg.ztools.ui.model.table.TextCellDecoratorContext;

public class TextCellDecorator 
		implements ITableDecorator {
	
	private static final int defaultWidth = 90;
	private static final int defaultHeight = 30;
	
	protected ElementFacade cellFacade;
	
	protected TextCellDecoratorContext context;
	
	public TextCellDecorator(ElementFacade cellFacade) {
		this(cellFacade, new TextCellDecoratorContext());
	}
	
	public TextCellDecorator(ElementFacade cellFacade, TextCellDecoratorContext context) {
		this.cellFacade = cellFacade;
		this.context = context;
	}
	
	@Override
	public void decorate(CellDecoration decoration, Object value) {
		String txt = context.getFormat().format(value.toString());
		decoration.setText(txt);
		decoration.setTextAlign(context.getAlign());
		decoration.setToolTip(txt);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITableDecoratorContext getContext() {
		return context;
	}

	@Override
	public void setContext(ITableDecoratorContext context) {
		this.context = (TextCellDecoratorContext) context;
	}
}
