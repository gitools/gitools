/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.utils.xml.adapter;

import java.awt.Color;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ColorXmlAdapter extends XmlAdapter<String, Color> {

	@Override
	public String marshal(Color v) throws Exception {
        String rgb = Integer.toHexString(v.getRGB());
        rgb = rgb.substring(2, rgb.length());
		return "#"+rgb;
	}

	@Override
	public Color unmarshal(String v) throws Exception {
        String sixLetterHex = v.substring(0, 7);
        try {
        Color c = Color.decode(v);
        return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

}
