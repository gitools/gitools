package org.gitools.model.decorator.impl;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;

import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.matrix.element.IElementAdapter;

public class FormattedTextElementDecorator extends ElementDecorator {
	
	private static final long serialVersionUID = -8595819997133940913L;
	
	private int valueIndex = -1;
	private String formatString = null;
	
	public FormattedTextElementDecorator() {
		this.formatString = "%1$s";
	}

	public FormattedTextElementDecorator(IElementAdapter elementAdapter) {
		super(elementAdapter);
		this.formatString = "%1$s";
	}

	public void setFormatString(String formatString) {
		this.formatString = formatString;
	}

	public String getFormatString() {
		return formatString;
	}

	
	public final int getValueIndex() {
		return valueIndex;
	}

	public final void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}

	@Override
	public void decorate(ElementDecoration decoration, Object element) {
		String cellText;
		try {
			if (this.valueIndex >= 0) {
				cellText = String.format(formatString, 
						this.adapter.getValue(element, valueIndex));
			} else
				cellText = String.format(formatString, element);

		} catch (IllegalFormatException e) {
			cellText = element.toString();
		}
		decoration.setText(cellText);
		decoration.setToolTip(cellText);
	}

	@Override
	public Map<String, String> getConfiguration() {
	
		Map<String, String> configuration = new HashMap <String, String>();
		configuration.put("valueIndex", Integer.toString(valueIndex));
		configuration.put("formatString", formatString);
		
		return configuration;
	}

	@Override
	public void setConfiguration(Map<String, String> configuration) {
	
		this.valueIndex = Integer.parseInt((String) configuration.get("valueIndex"));	
		this.formatString = configuration.get("formatString");
	}

}
