package org.gitools.model.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IndexArrayXmlAdapter extends XmlAdapter<String, int[]> {

	private static final String elemSeparator = ",";

	@Override
	public String marshal(int[] v) throws Exception {

		String output = "";

		if (v.length > 0) {
		
			int i = 0;

			while (i < v.length - 1) {
				output = output + Integer.toString(v[i]) + elemSeparator;
				i++;
			}
			output = output + Integer.toString(v[i]);
		}
		return output;
	}

	@Override
	public int[] unmarshal(String v) throws Exception {

		String[] elems = v.split(elemSeparator);

		int elemsSize = elems.length;

		int[] output = new int[elemsSize];

		for (int i = 0; i < elemsSize; i++)
			output[i] = Integer.parseInt(elems[i]);

		return output;
	}

}
