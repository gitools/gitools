/*
 * #%L
 * gitools-ui-platform
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
package org.gitools.ui.platform.help;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Help {

    private static Help instance;
    private final PropertiesExpansion properties;
    private final List<UrlMap> urlMap;

    Help() {
        this(new Properties(), new ArrayList<UrlMap>());
    }

    private Help(Properties properties, List<UrlMap> urlMap) {
        this.properties = new PropertiesExpansion(properties);
        this.urlMap = urlMap;
    }

    public static Help get() {
        if (instance == null) {
            instance = new Help();
        }

        return instance;
    }

    public void loadProperties(InputStream in) throws IOException {
        properties.load(in);
        in.close();
    }

    public void loadUrlMap(InputStream in) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        int lineNum = 1;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                String[] fields = line.split("\t");

                if (fields.length == 2) {
                    urlMap.add(new UrlMap(Pattern.compile(fields[0]), fields[1]));
                } else {
                    throw new Exception("Error reading help url mappings:" +
                            " Two columns expected at line " + lineNum);
                }
            }
            lineNum++;
        }
        br.close();
    }

    URL getHelpUrl(HelpContext context) throws MalformedURLException {
        String id = context.getId();
        String urlStr = null; // FIXME Use a default url
        for (UrlMap map : urlMap) {
            Matcher matcher = map.getPattern().matcher(id);
            if (matcher.matches()) {
                Properties p = new Properties(properties);
                for (int i = 0; i < matcher.groupCount(); i++)
                    p.setProperty("" + i, matcher.group(i));
                urlStr = expandPattern(properties, map.getUrl());
                break;
            }
        }

        return new URL(urlStr);
    }

    public void showHelp(HelpContext context) throws HelpException {
        try {
            URL url = getHelpUrl(context);
            Desktop.getDesktop().browse(url.toURI());
        } catch (Exception ex) {
            throw new HelpException(ex);
        }
    }

    private String expandPattern(Properties properties, String pattern) {

        final StringBuilder output = new StringBuilder();
        final StringBuilder var = new StringBuilder();

        char state = 'C';

        int pos = 0;

        while (pos < pattern.length()) {

            char ch = pattern.charAt(pos++);

            switch (state) {
                case 'C': // copying normal characters
                    if (ch == '$') {
                        state = '$';
                    } else {
                        output.append(ch);
                    }
                    break;

                case '$': // start of variable
                    if (ch == '{') {
                        state = 'V';
                    } else {
                        output.append('$').append(ch);
                        state = 'C';
                    }
                    break;

                case 'V': // reading name of variable
                    if (ch == '}') {
                        state = 'X';
                    } else {
                        var.append(ch);
                    }
                    break;

                case 'X': // expand variable
                    output.append(properties.getProperty(var.toString()));
                    var.setLength(0);
                    pos--;
                    state = 'C';
                    break;
            }
        }

        switch (state) {
            case '$':
                output.append('$');
                break;
            case 'V':
                output.append("${").append(var);
                break;
            case 'X':
                output.append(properties.getProperty(var.toString()));
                break;
        }

        return output.toString();
    }

    public static class UrlMap {
        private final Pattern pattern;
        private final String url;

        public UrlMap(Pattern pattern, String url) {
            this.pattern = pattern;
            this.url = url;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public String getUrl() {
            return url;
        }
    }
}
