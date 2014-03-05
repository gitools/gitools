/*
 * #%L
 * gitools-utils
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
package org.gitools.utils.textpattern;

import java.util.ArrayList;
import java.util.List;

public class TextPattern {

    private final List<Token> tokens;

    public TextPattern(String pattern) {
        this.tokens = internalCompile(pattern);
    }

    private static List<Token> internalCompile(String pattern) {
        List<Token> tokens = new ArrayList<>();

        if (pattern == null) {
            return tokens;
        }

        final StringBuilder buff = new StringBuilder();
        final StringBuilder varBuff = new StringBuilder();

        char state = 'C';

        int pos = 0;

        while (pos < pattern.length()) {

            char ch = pattern.charAt(pos++);

            switch (state) {
                case 'C': // copying normal characters
                    if (ch == '$') {
                        state = '$';
                    } else {
                        buff.append(ch);
                    }
                    break;

                case '$': // start of variable
                    if (ch == '{') {
                        state = 'V';
                        tokens.add(new TextToken(buff.toString()));
                        buff.setLength(0);
                    } else {
                        buff.append('$').append(ch);
                        state = 'C';
                    }
                    break;

                case 'V': // reading name of variable
                    if (ch == '}') {
                        state = 'X';
                    } else {
                        varBuff.append(ch);
                    }
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
            case '$':
                buff.append('$');
                break;
            case 'V':
                buff.append("${").append(varBuff);
                break;
            case 'X':
                tokens.add(new VariableToken(varBuff.toString()));
                varBuff.setLength(0);
                break;
        }

        if (buff.length() > 0) {
            tokens.add(new TextToken(buff.toString()));
        }

        return tokens;
    }

    public String generate(VariableValueResolver resolver) {
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens)
            token.generate(resolver, sb);
        return sb.toString();
    }

    public static interface VariableValueResolver {
        String resolveValue(String variableName);
    }

    private static interface Token {
        void generate(VariableValueResolver resolver, StringBuilder sb);
    }

    private static class TextToken implements Token {
        private final String text;

        public TextToken(String text) {
            this.text = text;
        }

        @Override
        public void generate(VariableValueResolver resolver, StringBuilder sb) {
            sb.append(text);
        }
    }

    private static class VariableToken implements Token {
        private final String variableName;

        public VariableToken(String variableName) {
            this.variableName = variableName;
        }

        @Override
        public void generate(VariableValueResolver resolver, StringBuilder sb) {
            sb.append(resolver.resolveValue(variableName));
        }
    }

}
