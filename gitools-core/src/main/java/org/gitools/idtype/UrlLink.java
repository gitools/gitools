/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package org.gitools.idtype;

import org.gitools.utils.textpatt.TextPattern;
import org.gitools.utils.textpatt.TextPatternXmlAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UrlLink {

	@XmlAttribute
	protected String name;
	
	@XmlValue
	@XmlJavaTypeAdapter(TextPatternXmlAdapter.class)
	protected TextPattern pattern;

	public UrlLink() {
	}

	public UrlLink(String name, String pattern) {
		this.name = name;
		this.pattern = new TextPattern(pattern);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TextPattern getPattern() {
		return pattern;
	}

	public void setPattern(TextPattern pattern) {
		this.pattern = pattern;
	}
}
