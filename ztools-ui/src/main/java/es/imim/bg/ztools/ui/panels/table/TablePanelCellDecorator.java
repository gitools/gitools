package es.imim.bg.ztools.ui.panels.table;

import es.imim.bg.ztools.table.decorator.ElementDecoration;

@Deprecated
public interface TablePanelCellDecorator {

	void decorate(ElementDecoration decoration, Object value);
}
