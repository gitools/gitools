package org.gitools.model.xml;

import java.awt.Color;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ColorXmlAdapter extends XmlAdapter<String, Color> {

	@Override
	public String marshal(Color v) throws Exception {
		return "#"+Integer.toHexString(v.getRGB());
	}

	@Override
	public Color unmarshal(String v) throws Exception {
		return Color.decode(v);
	}

}
