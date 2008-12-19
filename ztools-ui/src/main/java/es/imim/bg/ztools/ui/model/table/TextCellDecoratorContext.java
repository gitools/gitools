package es.imim.bg.ztools.ui.model.table;

import java.text.DecimalFormat;

import es.imim.bg.ztools.ui.colormatrix.CellDecoration;

public class TextCellDecoratorContext
		extends AbstractCellDecoratorContext {
	
	public DecimalFormat format;
	public CellDecoration.TextAlignment align;
	
	public TextCellDecoratorContext() {
		this.format = new DecimalFormat("#.####");
		this.align = CellDecoration.TextAlignment.left;
	}
	
	public DecimalFormat getFormat() {
		return format;
	}
	
	public void setFormat(DecimalFormat format) {
		this.format = format;
	}
	
	public CellDecoration.TextAlignment getAlign() {
		return align;
	}
	
	public void setAlign(CellDecoration.TextAlignment align) {
		this.align = align;
	}
}
