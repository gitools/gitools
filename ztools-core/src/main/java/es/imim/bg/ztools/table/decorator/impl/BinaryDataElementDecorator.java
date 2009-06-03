package es.imim.bg.ztools.table.decorator.impl;

import java.awt.Color;

import es.imim.bg.GenericFormatter;
import es.imim.bg.colorscale.util.ColorConstants;
import es.imim.bg.ztools.table.TableUtils;
import es.imim.bg.ztools.table.decorator.ElementDecoration;
import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;

public class BinaryDataElementDecorator extends ElementDecorator {

	private GenericFormatter fmt = new GenericFormatter();

	public BinaryDataElementDecorator(IElementAdapter adapter) {
		super(adapter);

	}

	@Override
	public void decorate(ElementDecoration decoration, Object element) {

		decoration.reset();

		if (element == null) {
			decoration.setBgColor(Color.WHITE);
			decoration.setToolTip("Void cell");
			return;
		}

		Object value = adapter.getValue(element, 0);

		double v = TableUtils.doubleValue(value);

		final Color color = (v == 0.0) ? ColorConstants.nonSignificantColor
				: ColorConstants.pvalueMaxColor;

		decoration.setBgColor(color);
		decoration.setToolTip(fmt.format(v));
	}
}
