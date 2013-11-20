/*
 * #%L
 * gitools-obo
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
package org.gitools.datasources.obo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @noinspection ALL
 */
public class OBOStreamReader implements OBOEventTypes {

    private static final Pattern STANZA_NAME_PATTERN = Pattern.compile("^\\[(.*)\\][ \\t]*(?:!(.*))?$");
    private static final Pattern LINE_COMMENT_PATTERN = Pattern.compile("^\\s*!(.*)$");
    private static final Pattern TAG_NAME_PATTERN = Pattern.compile("^[0-9a-zA-Z_]$");

    private final OBOStream stream;
    private final Stack<OBOStream> streamStack;

    private final LinkedList<OBOEvent> tokens;

    private boolean headerStarted;
    private boolean headerEnded;
    private boolean documentEnded;

    @Nullable
    private String stanzaName;
    private String tagName;

    public OBOStreamReader(Reader reader) {
        this(new OBOStream(new BufferedReader(reader)));
    }

    public OBOStreamReader(URL baseUrl) throws IOException {
        this(new OBOStream(baseUrl));
    }

    private OBOStreamReader(OBOStream stream) {
        this.stream = stream;
        streamStack = new Stack<OBOStream>();

        tokens = new LinkedList<OBOEvent>();
        tokens.offer(new OBOEvent(DOCUMENT_START, 0));

        headerStarted = false;
        headerEnded = false;
        documentEnded = false;
        stanzaName = null;
    }

    @Nullable
    public OBOEvent nextEvent() throws IOException {
        if (tokens.size() > 0) {
            return tokens.poll();
        }

        while (tokens.size() == 0) {
            String line = stream.nextLine();
            int pos = stream.getLinePos();

            if (line == null) {
                if (!documentEnded) {
                    documentEnded = true;
                    tokens.offer(new OBOEvent(DOCUMENT_END, pos));
                    break;
                } else {
                    return null;
                }
            }

            Matcher stanzaMatcher = STANZA_NAME_PATTERN.matcher(line);
            Matcher commentMatcher = LINE_COMMENT_PATTERN.matcher(line);

            if (stanzaMatcher.matches()) {
                if (stanzaName == null) {
                    if (!headerEnded) {
                        tokens.offer(new OBOEvent(HEADER_END, pos));
                        headerEnded = true;
                    }
                }

                String stzName = stanzaMatcher.group(1);
                //String stzName = line.substring(1, line.length() - 1);
                tokens.offer(new OBOEvent(STANZA_START, pos, stzName));
                stanzaName = stzName;
            } else if (commentMatcher.matches()) {
                tokens.offer(new OBOEvent(COMMENT, pos));
            } else {
                if (stanzaName == null && !headerStarted) {
                    tokens.offer(new OBOEvent(HEADER_START, pos));
                    headerStarted = true;
                }

                nextTag(line, pos);
            }
        }

        return tokens.poll();
    }

    public void close() throws IOException {
        stream.close();
        // TODO streamStack
    }

    private void nextTag(@NotNull String line, int linepos) {
        int pos = line.indexOf(':');
        if (pos < 0) {
            tokens.offer(new OBOEvent(UNKNOWN, linepos));
            return;
        }

        tagName = line.substring(0, pos);
        String content = line.substring(pos + 1);
        StringBuilder sb = new StringBuilder();

        //TODO parse contents and generate events
        escapeCharsAndRemoveComments(content, sb);
        content = sb.toString().trim();
        tokens.offer(new OBOEvent(TAG_START, linepos, stanzaName, tagName, content));
        tokens.offer(new OBOEvent(TAG_END, linepos, stanzaName, tagName, content));
    }

    /**
     * replace escape characters and remove comments
     */
    private void escapeCharsAndRemoveComments(@NotNull String line, @NotNull StringBuilder sb) {
        int len = line.length();
        int pos = 0;
        while (pos < len) {
            char c = line.charAt(pos++);
            if (c == '!') {
                pos = len;
            } else if (c != '\\' || (c == '\\' && pos == len - 1)) {
                sb.append(c);
            } else {
                c = line.charAt(pos++);
                switch (c) {
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'W':
                        sb.append(' ');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case ':':
                        sb.append(':');
                        break;
                    case ',':
                        sb.append(',');
                        break;
                    case '"':
                        sb.append('"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case '(':
                        sb.append('(');
                        break;
                    case ')':
                        sb.append(')');
                        break;
                    case '[':
                        sb.append('[');
                        break;
                    case ']':
                        sb.append(']');
                        break;
                    case '{':
                        sb.append('{');
                        break;
                    case '}':
                        sb.append('}');
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }
        }
    }
}
