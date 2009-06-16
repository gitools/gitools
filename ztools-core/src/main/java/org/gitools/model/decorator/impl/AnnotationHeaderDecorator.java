package org.gitools.model.decorator.impl;

import org.gitools.model.decorator.HeaderDecoration;
import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.matrix.AnnotationMatrix;

public class AnnotationHeaderDecorator extends HeaderDecorator {

	private static final long serialVersionUID = -8529301109846251890L;

	protected AnnotationMatrix annotations;
	protected String namePattern;
	protected String urlPattern;
	
	public AnnotationHeaderDecorator() {
	}

	public AnnotationMatrix getAnnotations() {
		return annotations;
	}
	
	public void setAnnotations(AnnotationMatrix annotations) {
		this.annotations = annotations;
	}
	
	public String getNamePattern() {
		return namePattern;
	}
	
	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public String getUrlPattern() {
		return urlPattern;
	}
	
	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	@Override
	public HeaderDecoration decorate(HeaderDecoration decoration, Object header) {
		super.decorate(decoration, header);
		if (namePattern != null && !namePattern.isEmpty())
			decoration.setText(expandPattern(annotations, header.toString(), namePattern));
		return decoration;
	}

	private String expandPattern(
			AnnotationMatrix annotations,
			String id,
			String pattern) {
		
		if (annotations == null)
			return id;
		
		int ri = annotations.getRowIndex(id);
		if (ri < 0)
			return id;
		
		final StringBuilder output = new StringBuilder();
		final StringBuilder var = new StringBuilder();
				
		char state = 'C';
		
		int pos = 0;
		
		while (pos < pattern.length()) {
		
			char ch = pattern.charAt(pos++);
			
			switch (state) {
			case 'C': // copying normal characters
				if (ch == '$')
					state = '$';
				else
					output.append(ch);
				break;

			case '$': // start of variable
				if (ch == '{')
					state = 'V';
				else {
					output.append('$').append(ch);
					state = 'C';
				}
				break;
				
			case 'V': // reading name of variable
				if (ch == '}')
					state = 'X';
				else
					var.append(ch);
				break;
				
			case 'X': // expand variable
				output.append(expandVariable(
						annotations, var.toString(), ri, id));
				var.setLength(0);
				pos--;
				state = 'C';
				break;
			}
		}
		
		switch (state) {
		case '$': output.append('$'); break;
		case 'V': output.append("${").append(var); break;
		case 'X':
			output.append(expandVariable(
					annotations, var.toString(), ri, id));
			break;
		}
		
		return output.toString();
	}

	private String expandVariable(
			AnnotationMatrix annotations,
			String var, int ri, String id) {
		
		if (var.equalsIgnoreCase("id"))
			return id;
		
		StringBuilder output = new StringBuilder();
		int ci = annotations.getColumnIndex(var);
		if (ci < 0)
			output.append("${").append(var).append('}');
		else
			output.append(annotations.getCell(ri, ci));
		
		return output.toString();
	}
}
