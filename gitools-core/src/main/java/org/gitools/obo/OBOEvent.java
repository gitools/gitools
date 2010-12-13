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

package org.gitools.obo;

//FIXME On development
public class OBOEvent {

	private int type;
	private String stanzaName;
	private String tagName;
	private String tagValue;

	//TODO file line and column
	protected String line;

	public OBOEvent(int type, int line) {
		this.type = type;
	}

	public OBOEvent(int type, int line, String stanzaName) {
		this.type = type;
		this.stanzaName = stanzaName;
	}

	public OBOEvent(int type, int line, String stanzaName, String tagName) {
		this.type = type;
		this.stanzaName = stanzaName;
		this.tagName = tagName;
	}

	public OBOEvent(int type, int line, String stanzaName, String tagName, String tagValue) {
		this.type = type;
		this.stanzaName = stanzaName;
		this.tagName = tagName;
		this.tagValue = tagValue;
	}

	public int getType() {
		return type;
	}

	public String getLine() {
		return line;
	}
	
	public String getStanzaName() {
		return stanzaName;
	}

	public String getTagName() {
		return tagName;
	}

	public String getTagValue() {
		return tagValue;
	}
}
