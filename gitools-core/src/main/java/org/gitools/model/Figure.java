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

package org.gitools.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Figure extends Artifact {

	private static final long serialVersionUID = -5908048128953551645L;

	public static final String FOOTER_CHANGED = "footerChanged";

	/** Footer **/
	private String footer;

	public Figure() {
		super();
	}

	public Figure(Figure figure) {
		super(figure);
		
		this.footer = figure.getFooter();
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		String oldValue = this.footer;
		this.footer = footer;
		firePropertyChange(FOOTER_CHANGED, oldValue, footer);
	}
}