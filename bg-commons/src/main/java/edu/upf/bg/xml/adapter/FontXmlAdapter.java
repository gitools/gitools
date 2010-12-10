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

package edu.upf.bg.xml.adapter;

import java.awt.Font;
import javax.xml.bind.annotation.adapters.XmlAdapter;


public class FontXmlAdapter extends XmlAdapter<FontXmlElement, Font> {

	@Override
	public Font unmarshal(FontXmlElement v) throws Exception {
		return new Font(v.name, v.style, v.size);
	}

	@Override
	public FontXmlElement marshal(Font v) throws Exception {
		return new FontXmlElement(v.getName(), v.getStyle(), v.getSize());
	}

}
