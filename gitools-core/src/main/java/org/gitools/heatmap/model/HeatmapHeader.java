package org.gitools.heatmap.model;


import java.awt.Color;
import java.awt.Font;
import java.net.URLEncoder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.AbstractModel;
import org.gitools.matrix.model.AnnotationMatrix;
import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import edu.upf.bg.xml.adapter.FontXmlAdapter;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapHeader extends AbstractModel {

	private static final long serialVersionUID = -2580139666999968074L;
	
	public static final String FG_COLOR_CHANGED = "fgColorChanged";
	public static final String BG_COLOR_CHANGED = "bgColorChanged";
	public static final String FONT_CHANGED = "fontChanged";
	public static final String ANNOTATIONS_CHANGED = "annotationsChanged";
	public static final String LABEL_PATTERN_CHANGED = "labelPatternChanged";
	public static final String LINK_NAME_CHANGED = "linkNameChanged";
	public static final String LINK_PATTERN_CHANGED = "linkPatternChanged";
	public static final String COLOR_ANN_CHANGED = "colorAnnChanged";

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color foregroundColor;
	
	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color backgroundColor;

	@XmlJavaTypeAdapter(FontXmlAdapter.class)
	protected Font font;

	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	//@XmlTransient
	protected AnnotationMatrix annotations;

	protected String labelPattern;
	protected String linkName;
	protected String linkPattern;

    protected boolean colorAnnEnabled;

	public HeatmapHeader() {
		foregroundColor = Color.BLACK;
		backgroundColor = Color.WHITE;
		font = new Font(Font.MONOSPACED, Font.PLAIN, 9);
		labelPattern = "${id}";
		linkName = "Google";
		linkPattern = "http://www.google.com/search?q=${url:id}";
	}
	
	public Color getForegroundColor() {
		return foregroundColor;
	}
	
	public void setForegroundColor(Color color) {
		Color old = this.foregroundColor;
		this.foregroundColor = color;
		firePropertyChange(FG_COLOR_CHANGED, old, color);
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(Color color) {
		Color old = this.backgroundColor;
		this.backgroundColor = color;
		firePropertyChange(BG_COLOR_CHANGED, old, color);
	}

	public boolean isColorAnnEnabled() {
		return this.colorAnnEnabled;
	}

	public void setColorAnnEnabled(boolean bool) {
		this.colorAnnEnabled = bool;
		firePropertyChange(COLOR_ANN_CHANGED, !bool, bool);
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		Font old = this.font;
		this.font = font;
		firePropertyChange(FONT_CHANGED, old, font);
	}

	public AnnotationMatrix getAnnotations() {
		return annotations;
	}

	public void setAnnotations(AnnotationMatrix annotations) {
		AnnotationMatrix old = this.annotations;
		this.annotations = annotations;
		firePropertyChange(ANNOTATIONS_CHANGED, old, annotations);
	}

	public String getLabelPattern() {
		return labelPattern;
	}

	public void setLabelPattern(String pattern) {
		//System.out.println("SetLabelPattern: " + this + " " + this.labelPattern + " -> " + pattern);
		String old = this.labelPattern;
		this.labelPattern = pattern;
		firePropertyChange(LABEL_PATTERN_CHANGED, old, pattern);
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		String old = this.linkName;
		this.linkName = linkName;
		firePropertyChange(LINK_NAME_CHANGED, old, linkName);
	}

	public String getLinkPattern() {
		return linkPattern;
	}

	public void setLinkPattern(String linkPattern) {
		String old = this.linkPattern;
		this.linkPattern = linkPattern;
		firePropertyChange(LINK_PATTERN_CHANGED, old, linkPattern);
	}

	public HeatmapHeaderDecoration decorate(HeatmapHeaderDecoration decoration, Object header) {
		decoration.setFgColor(foregroundColor);
		decoration.setBgColor(backgroundColor);
		
		String id = header.toString();

		if (labelPattern == null || labelPattern.isEmpty())
			decoration.setText(id);
		else
			decoration.setText(
					expandPattern(annotations, id, labelPattern));
		
		if (labelPattern == null || labelPattern.isEmpty())
			decoration.setUrl(null);
		else
			decoration.setUrl(
					expandPattern(annotations, id, linkPattern));

		return decoration;
	}

	//TODO: move this function out of here!
	public String expandPattern(
			AnnotationMatrix annotations,
			String id,
			String pattern) {

		int ri = annotations != null ? annotations.getRowIndex(id) : -1;
		
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
				output.append(
						expandVariable(annotations, var.toString(), ri, id));
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
			output.append(
					expandVariable(annotations, var.toString(), ri, id));
			break;
		}

		return output.toString();
	}

	private interface VarEncoder {
		String encode(String string);
	}

	private String expandVariable(
			AnnotationMatrix annotations,
			String var, int ri, String id) {

		VarEncoder encoder = new VarEncoder() {
			@Override public String encode(String string) {
				return string; }
		};

		String[] part = var.split("\\:");
		if (part.length == 2) {
			if (part[0].equalsIgnoreCase("url")) {
				encoder = new VarEncoder() {
					@Override public String encode(String string) {
						try {
							return URLEncoder.encode(string, "UTF-8");
						} catch (Exception ex) {
							return string;
						}
					}
				};
				var = part[1];
			}
		}

		if (var.equalsIgnoreCase("id"))
			return encoder.encode(id);

		if (annotations == null || ri < 0)
			return "";

		/*if (var.equalsIgnoreCase("index"))
			return encoder.encode(String.valueOf(ri));*/
		
		StringBuilder output = new StringBuilder();
		int ci = annotations.getColumnIndex(var);
		if (ci < 0)
			output.append("${").append(var).append('}');
		else
			output.append(encoder.encode(annotations.getCell(ri, ci)));

		return output.toString();
	}
}
