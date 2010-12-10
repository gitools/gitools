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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Pattern;

//FIXME On development
public class OBOStreamReader {

	private static class Source {
		private BufferedReader reader;
		private URL baseUrl;

		public Source(BufferedReader reader, URL baseUrl) {
			this.reader = reader;
			this.baseUrl = baseUrl;
		}

		public BufferedReader getReader() {
			return reader;
		}

		public URL getBaseUrl() {
			return baseUrl;
		}
	}

	@Deprecated
	private static class UnknownToken extends OBOEvent {

		public UnknownToken(String line) {
			super(OBOStreamReader.UNKNOWN);
			this.line = line;
		}
	}

	private static final Pattern STANZA_NAME_PATTERN = Pattern.compile("^\\[.*\\]$");

	private static final int UNKNOWN = -1;
	private static final int COMMENT = 1;
	private static final int DOCUMENT_START = 10;
	private static final int DOCUMENT_END = 11;
	private static final int HEADER_START = 20;
	private static final int HEADER_END = 21;
	private static final int STANZA_START = 30;
	private static final int STANZA_END = 31;
	private static final int TAG_START = 40;
	//private static final int TAG_VALUE_FIELD = 41;
	private static final int TAG_TRAILING_MODIFIER = 42;
	private static final int TAG_END = 43;
	/*private static final int DBXREF_START = 50;
	private static final int DBXREF_START = 51;
	private static final int DBXREF_END = 52;*/

	private Source currentSource;
	private Stack<Source> sourceStack;

	private LinkedList<OBOEvent> tokens;

	private boolean headerStarted;
	private boolean headerEnded;

	private String stanzaName;
	private String tagName;
	private String tagValue;
	private String tagTrailingModifiers;
	private String tagFieldText;

	public OBOStreamReader(Reader reader, URL baseUrl) {
		currentSource = new Source(new BufferedReader(reader), baseUrl);
		sourceStack = new Stack<Source>();

		tokens = new LinkedList<OBOEvent>();
		headerStarted = false;
		headerEnded = false;
		stanzaName = null;
	}

	public OBOEvent nextToken() throws IOException {
		if (tokens.size() > 0)
			return tokens.poll();

		while (tokens.size() == 0) {
			String line = nextLine();

			if (line == null) {
				tokens.offer(new OBOEvent(DOCUMENT_END));
				break;
			}

			if (STANZA_NAME_PATTERN.matcher(line).matches()) {
				if (stanzaName == null) {
					if (!headerStarted) {
						tokens.offer(new OBOEvent(HEADER_START));
						headerStarted = true;
					}
					if (!headerEnded) {
						tokens.offer(new OBOEvent(HEADER_END));
						headerEnded = true;
					}
				}
				String stzName = line.substring(1, line.length() - 1);
				tokens.offer(new OBOEvent(STANZA_START, stzName));
				stanzaName = stzName;
			}
			else { // it is a tag
				OBOEvent tag = parseTag(line);
				tokens.offer(tag);
			}
		}

		return tokens.poll();
	}

	/** parses a tag and returns the token representing it */
	// TODO Return a Tag object
	private OBOEvent parseTag(String line) {
		int pos = line.indexOf(':');
		if (pos < 0)
			return new UnknownToken(line);

		String name = line.substring(0, pos);
		String[] value = parseValue(line.substring(pos + 1));

		throw new UnsupportedOperationException("Not yet implemented");
	}

	private String[] parseValue(String value) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/** returns a non empty line or null if EOF */
	private String nextLine() throws IOException {
		String line = readLine();
		while (line != null && isEmptyLine(line))
			line = readLine();
		return line;
	}

	/** returns the next line or null if EOF */
	// TODO comments and escape characters shouldn't be removed yet here
	private String readLine() throws IOException {
		BufferedReader reader = currentSource.getReader();
		StringBuilder completeLine = new StringBuilder();

		String line = reader.readLine();
		if (line != null)
			line = line.trim();

		while (line != null && line.endsWith("\\")) {
			line = line.substring(0, line.length() - 1);
			escapeCharsAndRemoveComments(line, completeLine);
			line = reader.readLine();
			if (line != null)
				line = line.trim();
		}

		if (line != null)
			escapeCharsAndRemoveComments(line, completeLine);

		if (line == null && completeLine.length() == 0)
			return null;

		return completeLine.toString();
	}

	/** replace escape characters and remove comments */
	private void escapeCharsAndRemoveComments(String line, StringBuilder sb) {
		int len = line.length();
		int pos = 0;
		while (pos < len) {
			char c = line.charAt(pos++);
			if (c != '\\' || (c == '\\' && pos == len - 1))
				sb.append(c);
			else if (c == '!')
				pos = len;
			else {
				c = line.charAt(pos++);
				switch (c) {
					case 'n': sb.append('\n'); break;
					case 'W': sb.append(' '); break;
					case 't': sb.append('\t'); break;
					case ':': sb.append(':'); break;
					case ',': sb.append(','); break;
					case '"': sb.append('"'); break;
					case '\\': sb.append('\\'); break;
					case '(': sb.append('('); break;
					case ')': sb.append(')'); break;
					case '[': sb.append('['); break;
					case ']': sb.append(']'); break;
					case '{': sb.append('{'); break;
					case '}': sb.append('}'); break;
					default: sb.append(c); break;
				}
			}
		}
	}

	private boolean isEmptyLine(String line) {
		return line.replaceAll("\\s", "").isEmpty();
	}
}
