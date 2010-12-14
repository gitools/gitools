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

public interface OBOEventTypes {

	static final int UNKNOWN = -1;
	static final int COMMENT = 1;
	static final int DOCUMENT_START = 10;
	static final int DOCUMENT_END = 19;
	static final int HEADER_START = 20;
	static final int HEADER_END = 29;
	static final int STANZA_START = 30;
	static final int STANZA_END = 39;
	static final int TAG_START = 40;
	static final int TAG_END = 49;

	/*static final int DBXREF_START = 50;
	static final int DBXREF_START = 51;
	static final int DBXREF_END = 52;*/
}
