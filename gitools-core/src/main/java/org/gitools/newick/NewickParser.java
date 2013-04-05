/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.newick;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @noinspection ALL
 */
public class NewickParser<VT>
{

    private static enum TokenType
    {
        SPACE, NAME, NUMBER, PAR_OPEN, PAR_CLOSE, COMMA, TWO_COLON, COLON_COMMA, UNKNOWN, END_OF_STREAM
    }

    private static class Token
    {
        private final TokenType type;
        private final String text;

        public Token(TokenType type, String text)
        {
            this.type = type;
            this.text = text;
        }

        public TokenType getType()
        {
            return type;
        }

        public String getText()
        {
            return text;
        }

        @Override
        public String toString()
        {
            if (text != null)
            {
                return type.toString() + " " + text;
            }
            else
            {
                return type.toString();
            }
        }
    }

    private enum ParserState
    {
        TREE_START, BRANCH_START, BRANCH_ADD, BRANCH_CLOSE, BRANCH_LEAF, LEAF, TREE_END, EOF
    }

    private final Reader reader;

    private final StringBuilder sb;
    private int bufferedChar;
    private int lastRow, lastCol, row, col;

    public NewickParser(String string)
    {
        this(new StringReader(string));
    }

    private NewickParser(Reader reader)
    {
        this.reader = reader;
        sb = new StringBuilder();
        bufferedChar = -1;
        lastRow = lastCol = row = col = 0;
    }

    /**
     * @noinspection UnusedDeclaration
     */
    public static void main(String[] args)
    {
        try
        {
            NewickParser p = new NewickParser(" ( ( ), (1,2, )B:0.2 ,(:0.4,E):3)A:0.01;");
            NewickTree t = p.parse();
            System.out.println(t);
            System.out.println(t.getDepth());
            for (NewickNode n : (List<NewickNode>) t.getRoot().getLeaves())
                System.out.println(n);

        } catch (NewickParserException ex)
        {
            Logger.getLogger(NewickParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void close() throws NewickParserException
    {
        try
        {
            reader.close();
        } catch (IOException e)
        {
            throw new NewickParserException(e);
        }
    }

    @Nullable
    public NewickTree<VT> parse() throws NewickParserException
    {
        Token token = null;
        ParserState state = ParserState.TREE_START;

        Set<String> names = new HashSet<String>();

        NewickTree tree = null;
        NewickNode node = null;
        NewickNode root = null;
        int level = 0;
        Stack<NewickNode> nodeStack = new Stack<NewickNode>();

        while (state != ParserState.EOF)
        {
            switch (state)
            {
                case TREE_START:
                    tree = new NewickTree();
                    root = null;
                    node = null;
                    level = 0;
                    nodeStack.clear();
                    token = nextTokenNoSpace();
                    switch (token.getType())
                    {
                        case PAR_OPEN:
                            state = ParserState.BRANCH_START;
                            break;
                        case COLON_COMMA:
                            state = ParserState.TREE_END;
                            break;
                        default:
                            exception("Unexpected token: " + token);
                    }
                    break;

                case BRANCH_START:
                    if (root != null)
                    {
                        nodeStack.push(root);
                    }

                    level++;
                    root = new NewickNode();
                    node = new NewickNode();
                    root.addChild(node);

                    token = nextTokenNoSpace();

                    switch (token.getType())
                    {
                        case PAR_OPEN:
                            state = ParserState.BRANCH_START;
                            break;
                        case COMMA:
                            state = ParserState.BRANCH_ADD;
                            break;
                        case PAR_CLOSE:
                            state = ParserState.BRANCH_CLOSE;
                            break;
                        case NAME:
                        case NUMBER:
                        case TWO_COLON:
                            state = ParserState.BRANCH_LEAF;
                            break;
                        default:
                            exception("Unexpected token: " + token);
                    }

                    break;

                case BRANCH_ADD:
                    node = new NewickNode();
                    root.addChild(node);

                    token = nextTokenNoSpace();

                    switch (token.getType())
                    {
                        case PAR_OPEN:
                            state = ParserState.BRANCH_START;
                            break;
                        case COMMA:
                            state = ParserState.BRANCH_ADD;
                            break;
                        case PAR_CLOSE:
                            state = ParserState.BRANCH_CLOSE;
                            break;
                        case NAME:
                        case NUMBER:
                        case TWO_COLON:
                            state = ParserState.BRANCH_LEAF;
                            break;
                        default:
                            exception("Unexpected token: " + token);
                    }

                    break;

                case BRANCH_LEAF:
                    token = parseNodeName(node, token, names);

                    switch (token.getType())
                    {
                        case COMMA:
                            state = ParserState.BRANCH_ADD;
                            break;
                        case PAR_CLOSE:
                            state = ParserState.BRANCH_CLOSE;
                            break;
                        default:
                            exception("Unexpected token: " + token);
                    }

                    break;

                case BRANCH_CLOSE:
                    level--;
                    if (level < 0)
                    {
                        exception("Unexpected branch closing");
                    }

                    token = nextToken();

                    if (token.getType() == TokenType.NAME || token.getType() == TokenType.TWO_COLON)
                    {
                        token = parseNodeName(root, token, names);
                    }

                    if (!nodeStack.isEmpty())
                    {
                        node = root;
                        root = nodeStack.pop();
                        root.setChild(root.getChildren().size() - 1, node);
                    }

                    switch (token.getType())
                    {
                        case COMMA:
                            state = ParserState.BRANCH_ADD;
                            break;
                        case PAR_CLOSE:
                            state = ParserState.BRANCH_CLOSE;
                            break;
                        case COLON_COMMA:
                            state = ParserState.TREE_END;
                            break;
                        default:
                            exception("Unexpected token: " + token);
                    }

                    break;

                case TREE_END:
                    if (level != 0)
                    {
                        exception("Unexpected end");
                    }

                    tree.setRoot(root);

                    token = nextTokenNoSpace();
                    switch (token.getType())
                    {
                        case END_OF_STREAM:
                            state = ParserState.EOF;
                            break;
                        default:
                            exception("End of stream expected but found " + token);
                    }

                    break;
            }
        }

        return tree;
    }

    @NotNull
    private Token parseNodeName(@NotNull NewickNode node, @NotNull Token token, @NotNull Set<String> names) throws NewickParserException
    {
        if (token.getType() == TokenType.NAME || token.getType() == TokenType.NUMBER)
        {
            if (names.contains(token.getText()))
            {
                exception("Node name already defined");
            }

            node.setName(token.getText());
            names.add(token.getText());
            token = nextToken();
        }

        if (token.getType() == TokenType.TWO_COLON)
        {
            token = nextToken();
            if (token.getType() != TokenType.NUMBER)
            {
                exception("Number expected but found " + token);
            }
            node.setValue(Double.parseDouble(token.getText()));
            token = nextTokenNoSpace();
        }

        return token;
    }

    private void exception(String msg) throws NewickParserException
    {
        throw new NewickParserException("Line " + lastRow + " column " + lastCol + " -> " + msg);
    }

    @Nullable
    private Token nextTokenNoSpace() throws NewickParserException
    {
        Token token = nextToken();
        while (token.getType() == TokenType.SPACE)
            token = nextToken();
        return token;
    }

    @Nullable
    private Token nextToken() throws NewickParserException
    {
        sb.setLength(0);

        int ch;
        char state = 'S';
        boolean done = false;

        try
        {
            while (!done)
            {
                switch (state)
                {
                    case 'S':    // Start
                        ch = nextChar();
                        if (ch == -1)
                        {
                            return new Token(TokenType.END_OF_STREAM, null);
                        }
                        else if (isWhitespace((char) ch))
                        {
                            return new Token(TokenType.SPACE, "" + (char) ch);
                        }
                        else if (ch == '-')
                        {
                            sb.append((char) ch);
                            state = 'Z';
                        }
                        else if (Character.isDigit((char) ch))
                        {
                            sb.append((char) ch);
                            state = 'I';
                        }
                        else if (isNameChar(ch))
                        {
                            sb.append((char) ch);
                            state = 'N';
                        }
                        else
                        {
                            switch (ch)
                            {
                                case '(':
                                    return new Token(TokenType.PAR_OPEN, "" + (char) ch);
                                case ')':
                                    return new Token(TokenType.PAR_CLOSE, "" + (char) ch);
                                case ',':
                                    return new Token(TokenType.COMMA, "" + (char) ch);
                                case ':':
                                    return new Token(TokenType.TWO_COLON, "" + (char) ch);
                                case ';':
                                    return new Token(TokenType.COLON_COMMA, "" + (char) ch);
                                default:
                                    exception("Unexpected character: " + (char) ch);
                            }
                        }
                        break;

                    case 'Z':    // sign character
                        ch = nextChar();

                        if (Character.isDigit((char) ch))
                        {
                            sb.append((char) ch);
                            state = 'I';
                        }
                        else if (isNameChar(ch))
                        {
                            sb.append((char) ch);
                            state = 'N';
                        }
                        else
                        {
                            bufferedChar = ch;
                            return new Token(TokenType.NAME, sb.toString());
                        }
                        break;

                    case 'I':    // Integer number
                        ch = nextChar();
                        while (ch != -1 && Character.isDigit((char) ch))
                        {
                            sb.append((char) ch);
                            ch = nextChar();
                        }

                        if (ch == '.')
                        {
                            sb.append((char) ch);
                            state = 'F';
                        }
                        else if (isNameChar(ch))
                        {
                            sb.append((char) ch);
                            state = 'N';
                        }
                        else
                        {
                            bufferedChar = ch;
                            return new Token(TokenType.NUMBER, sb.toString());
                        }
                        break;

                    case 'F':    // Floating number
                        ch = nextChar();
                        while (ch != -1 && Character.isDigit((char) ch))
                        {
                            sb.append((char) ch);
                            ch = nextChar();
                        }

                        if (isNameChar(ch))
                        {
                            sb.append((char) ch);
                            state = 'N';
                        }
                        else
                        {
                            bufferedChar = ch;
                            return new Token(TokenType.NUMBER, sb.toString());
                        }
                        break;

                    case 'N':    // Name
                        ch = nextChar();
                        while (ch != -1 && isNameChar((char) ch))
                        {
                            sb.append((char) ch);
                            ch = nextChar();
                        }
                        bufferedChar = ch;
                        return new Token(TokenType.NAME, sb.toString());

                    default:
                        throw new NewickParserException("Internal error: Unknown tokenizer state: " + state);
                }
            }
        } catch (Exception e)
        {
            throw new NewickParserException(e);
        }

        throw new NewickParserException("Unexpected tokenizer end");
    }

    private int nextChar() throws IOException
    {
        lastRow = row;
        lastCol = col;

        if (bufferedChar != -1)
        {
            int bufChar = bufferedChar;
            bufferedChar = -1;
            return bufChar;
        }

        int ch = reader.read();
        if (ch == '\n')
        {
            row++;
            col = 0;
        }
        else
        {
            col++;
        }
        return ch;
    }

    private boolean isWhitespace(char ch)
    {
        return Character.isWhitespace(ch);
    }

    private boolean isSpecialChar(int ch)
    {
        return ch == '(' || ch == ')' || ch == ',' || ch == ':' || ch == ';';
    }

    private boolean isNameChar(int ch)
    {
        return ch != -1 && !isSpecialChar(ch) && !isWhitespace((char) ch);
    }
}
