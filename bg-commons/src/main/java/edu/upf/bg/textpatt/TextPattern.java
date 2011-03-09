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

package edu.upf.bg.textpatt;

import java.util.ArrayList;
import java.util.List;


public class TextPattern {

	public static interface VariableValueResolver {
		String resolveValue(String variableName);
	}

	private static interface Token {
		void generate(VariableValueResolver resolver, StringBuilder sb);
	}

	private static class TextToken implements Token {
		private String text;

		public TextToken(String text) {
			this.text = text;
		}

		@Override
		public void generate(VariableValueResolver resolver, StringBuilder sb) {
			sb.append(text);
		}
	}

	private static class VariableToken implements Token {
		private String variableName;

		public VariableToken(String variableName) {
			this.variableName = variableName;
		}

		@Override
		public void generate(VariableValueResolver resolver, StringBuilder sb) {
			sb.append(resolver.resolveValue(variableName));
		}
	}

	private static List<Token> internalCompile(String pattern) {
		List<Token> tokens = new ArrayList<Token>();

		if (pattern == null)
			return tokens;
		
		final StringBuilder buff = new StringBuilder();
		final StringBuilder varBuff = new StringBuilder();

		char state = 'C';

		int pos = 0;

		while (pos < pattern.length()) {

			char ch = pattern.charAt(pos++);

			switch (state) {
			case 'C': // copying normal characters
				if (ch == '$')
					state = '$';
				else
					buff.append(ch);
				break;

			case '$': // start of variable
				if (ch == '{') {
					state = 'V';
					tokens.add(new TextToken(buff.toString()));
					buff.setLength(0);
				}
				else {
					buff.append('$').append(ch);
					state = 'C';
				}
				break;

			case 'V': // reading name of variable
				if (ch == '}')
					state = 'X';
				else
					varBuff.append(ch);
				break;

			case 'X': // expand variable
				tokens.add(new VariableToken(varBuff.toString()));
				varBuff.setLength(0);
				pos--;
				state = 'C';
				break;
			}
		}

		switch (state) {
		case '$': buff.append('$'); break;
		case 'V': buff.append("${").append(varBuff); break;
		case 'X':
			tokens.add(new VariableToken(varBuff.toString()));
			varBuff.setLength(0);
			break;
		}

		if (buff.length() > 0)
			tokens.add(new TextToken(buff.toString()));

		return tokens;
	}

	public static TextPattern compile(String pattern) {
		return new TextPattern(pattern);
	}

	private String text;
	private List<Token> tokens;

	public TextPattern(String pattern) {
		this.tokens = internalCompile(pattern);
	}

	public String getText() {
		return text;
	}

	public String generate(VariableValueResolver resolver) {
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens)
			token.generate(resolver, sb);
		return sb.toString();
	}
}
