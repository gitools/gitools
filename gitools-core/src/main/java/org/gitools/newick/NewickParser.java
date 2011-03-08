/*
 *  Copyright 2011 chris.
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

package org.gitools.newick;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;

public class NewickParser<VT> {

	private static enum TokenType {
		SPACE, NAME, NUMBER, PAR_OPEN, PAR_CLOSE, COMMA, TWO_COLON, COLON_COMMA, UNKNOWN
	}

	private static class Token {
		private TokenType type;
		private String text;

		public Token(TokenType type, String text) {
			this.type = type;
			this.text = text;
		}

		public TokenType getType() {
			return type;
		}

		public String getText() {
			return text;
		}

		@Override
		public String toString() {
			return type.toString() + "\t" + text;
		}
	}

	private enum ParserState {
		TREE_START, BRANCH_START, BRANCH_ADD, BRANCH_CLOSE, BRANCH_LEAF, LEAF, TREE_END
	}

	private static class NodeLevel {
		public NewickNode node;
		public int branchIndex;
		public NodeLevel(NewickNode node, int branchIndex) {
			this.node = node;
			this.branchIndex = branchIndex;
		}
	}

	private Reader reader;

	private StringBuilder sb;
	private int bufferedChar;

	public NewickParser(String string) {
		reader = new StringReader(string);
		sb = new StringBuilder();
		bufferedChar = -1;
	}

	public NewickParser(Reader reader) {
		this.reader = reader;
	}

	public NewickTree<VT> parse() throws NewickParserException {
		Token token;
		ParserState state = ParserState.TREE_START;

		NewickTree tree = null;
		NewickNode node = null;
		int branchIndex = 0;
		NewickNode currentNode = null;
		Stack<NodeLevel> nodeStack = new Stack<NodeLevel>();

		try {
			while (state != ParserState.TREE_END) {
				switch (state) {
					case TREE_START:
						tree = new NewickTree();
						currentNode = null;
						branchIndex = 0;
						token = nextToken();
						switch (token.getType()) {
							case SPACE: break;
							case COLON_COMMA: state = ParserState.TREE_END; break;
							case PAR_OPEN: state = ParserState.BRANCH_START; break;
							default:
								throw new NewickParserException("Unexpected token: " + token);
						}
						break;

					case BRANCH_START:
						node = new NewickNode();
						currentNode.setChild(branchIndex, node);
						if (currentNode != null)
							nodeStack.push(new NodeLevel(currentNode, branchIndex));

						currentNode = node;
						branchIndex = 0;
						
						token = nextToken();
						while (token.getType() == TokenType.SPACE)
							token = nextToken();

						switch (token.getType()) {
							case PAR_OPEN: state = ParserState.BRANCH_START; break;
							case COMMA: state = ParserState.BRANCH_ADD; break;
							case PAR_CLOSE: state = ParserState.BRANCH_CLOSE; break;
							case NAME: state = ParserState.BRANCH_LEAF; break;
							case TWO_COLON: state = ParserState.BRANCH_LEAF; break;
							default:
								throw new NewickParserException("Unexpected token: " + token);
						}

						break;

					case BRANCH_ADD:
						// TODO...

						token = nextToken();
						while (token.getType() == TokenType.SPACE)
							token = nextToken();

						switch (token.getType()) {
							case PAR_OPEN: state = ParserState.BRANCH_START; break;
							case COMMA: state = ParserState.BRANCH_ADD; break;
							case PAR_CLOSE: state = ParserState.BRANCH_CLOSE; break;
							case NAME: state = ParserState.BRANCH_LEAF; break;
							case TWO_COLON: state = ParserState.BRANCH_LEAF; break;
							default:
								throw new NewickParserException("Unexpected token: " + token);
						}

						break;

					case BRANCH_LEAF:
						// TODO...

						token = nextToken();
						while (token.getType() == TokenType.SPACE)
							token = nextToken();

						switch (token.getType()) {
							case COMMA: state = ParserState.BRANCH_ADD; break;
							case PAR_CLOSE: state = ParserState.BRANCH_CLOSE; break;
							default:
								throw new NewickParserException("Unexpected token: " + token);
						}

						break;

					case BRANCH_CLOSE:
						// TODO...

						token = nextToken();
						while (token.getType() == TokenType.SPACE)
							token = nextToken();

						switch (token.getType()) {
							case COMMA: state = ParserState.BRANCH_ADD; break;
							case PAR_CLOSE: state = ParserState.BRANCH_CLOSE; break;
							case COLON_COMMA: state = ParserState.TREE_END; break;
							default:
								throw new NewickParserException("Unexpected token: " + token);
						}

						break;
				}
			}
		}
		catch (Exception e) {
			throw new NewickParserException(e);
		}
		
		return tree;
	}

	private Token nextToken() throws IOException, NewickParserException {
		sb.setLength(0);

		int ch;
		char state = 'S';
		boolean done = false;

		while (!done) {
			switch (state) {
				case 'S':	// Start
					ch = nextChar();
					if (Character.isSpaceChar((char) ch))
						return new Token(TokenType.SPACE, "" + (char) ch);
					else if (Character.isDigit((char) ch)) {
						sb.append((char) ch);
						state = 'U';
					}
					else if (isNameChar(ch)) {
						sb.append((char) ch);
						state = 'N';
					}
					else {
						switch (ch) {
							case '(': return new Token(TokenType.PAR_OPEN, "" + (char) ch);
							case ')': return new Token(TokenType.PAR_CLOSE, "" + (char) ch);
							case ',': return new Token(TokenType.COMMA, "" + (char) ch);
							case ':': return new Token(TokenType.TWO_COLON, "" + (char) ch);
							case ';': return new Token(TokenType.COLON_COMMA, "" + (char) ch);
							default:
								throw new NewickParserException("Unexpected character: " + (char) ch);
						}
					}

				case 'U':	// Number
					ch = nextChar();
					while (Character.isDigit((char) ch)) {
						sb.append((char) ch);
						ch = nextChar();
					}

					if (isNameChar(ch)) {
						sb.append((char) ch);
						state = 'N';
					}
					else {
						bufferedChar = ch;
						return new Token(TokenType.NUMBER, sb.toString());
					}
					break;

				case 'N':	// Name
					ch = nextChar();
					while (isNameChar((char) ch)) {
						sb.append((char) ch);
						ch = nextChar();
					}
					bufferedChar = ch;
					return new Token(TokenType.NAME, sb.toString());
			}
		}

		throw new NewickParserException("Unexpected tokenizer end");
	}

	private boolean isSpecialChar(int ch) {
		return ch == '(' || ch == ')' || ch == ',' || ch == ':' || ch == ';';
	}

	private boolean isNameChar(int ch) {
		return !isSpecialChar(ch) && !Character.isSpaceChar((char) ch);
	}
	
	private int nextChar() throws IOException {
		int bufChar = bufferedChar;
		bufferedChar = -1;
		return (bufChar != -1) ? bufChar : reader.read();
	}
}
